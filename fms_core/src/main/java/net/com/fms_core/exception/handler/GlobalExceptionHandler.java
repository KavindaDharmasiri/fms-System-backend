/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.exception.handler;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.ErrorDetailDTO;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.exception.ConstraintViolationExceptions;
import net.com.fms_core.exception.HttpMessageNotReadableExceptions;
import net.com.fms_core.exception.ValidationExceptions;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.query.sqm.UnknownPathException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleGlobalException(Exception ex) {
        log.error("Exception: {}", ex.getMessage(), ex);
        System.err.println(ex.getMessage());
        System.err.println(ex);
        if (ex instanceof EntityNotFoundException) {
            List<ErrorDetailDTO> errors = List.of(new ErrorDetailDTO(ErrorCode.ENTITY_NOT_FOUND, ex.getMessage(), null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.error(errors));
        } else if (ex instanceof UnknownPathException){
            List<ErrorDetailDTO> errors = List.of(new ErrorDetailDTO(ErrorCode.UNKNOWN_PATH, ex.getMessage(), null));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.error(errors));
        } else if (ex instanceof MethodArgumentNotValidException){
            List<ErrorDetailDTO> errors = ValidationExceptions.getErrors((MethodArgumentNotValidException) ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.error(errors));
        } else if (ex instanceof ConstraintViolationException) {
            List<ErrorDetailDTO> errors = ConstraintViolationExceptions.getErrors((ConstraintViolationException) ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.error(errors));
        } else if (ex instanceof HttpMessageNotReadableException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.error(HttpMessageNotReadableExceptions.getError((HttpMessageNotReadableException) ex)));
        }  else if (ex instanceof NoResourceFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.error(HttpMessageNotReadableExceptions.getError((HttpMessageNotReadableException) ex)));
        } else if (ex instanceof NoResourceFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.error(HttpMessageNotReadableExceptions.getError((HttpMessageNotReadableException) ex)));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDTO.error(new ErrorDetailDTO(ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", null)));
        }
    }
}

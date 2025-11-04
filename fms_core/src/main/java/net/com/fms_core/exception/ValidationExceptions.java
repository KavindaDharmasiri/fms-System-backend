/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.exception;
import net.com.fms_core.dto.ErrorDetailDTO;
import net.com.fms_core.enums.ErrorCode;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.List;

public class ValidationExceptions {
    public static List<ErrorDetailDTO> getErrors(MethodArgumentNotValidException ex) {
        List<ErrorDetailDTO> errors = List.of();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorDetailDTO(ErrorCode.INVALID_INPUT, error.getDefaultMessage(), error.getField()));
        }
        return errors;
    }
}

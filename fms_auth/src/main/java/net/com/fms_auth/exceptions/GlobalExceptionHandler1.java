/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.exceptions;
import net.com.fms_auth.dto.common.StandardResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler1 extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardResponse> handleUnauthorized(
            UnauthorizedException ex, WebRequest request) {
        return new ResponseEntity<StandardResponse>(new StandardResponse(false, ex.getMessage()),HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<StandardResponse> handleUserAccountLockedException(
            AccountLockedException ex, WebRequest request) {
        return new ResponseEntity<StandardResponse>(new StandardResponse(false, ex.getMessage()),HttpStatus.LOCKED);
    }
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<StandardResponse> handleInternalServerErrorException(
            InternalServerErrorException ex, WebRequest request) {
        return new ResponseEntity<StandardResponse>(new StandardResponse(false, ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(StatusNotFoundException.class)
    public ResponseEntity<StandardResponse> handleStatusNotFoundException(
            StatusNotFoundException ex, WebRequest request) {
        return new ResponseEntity<StandardResponse>(new StandardResponse(false, ex.getMessage()),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AlreadyReportedException.class)
    public ResponseEntity<StandardResponse> handleAlreadyReportedException(
            AlreadyReportedException ex, WebRequest request) {
        return new ResponseEntity<StandardResponse>(new StandardResponse(false, ex.getMessage()),HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<StandardResponse> handleEmailNotVerifiedException(
            EmailNotVerifiedException ex, WebRequest request) {
        return new ResponseEntity<StandardResponse>(new StandardResponse(false, ex.getMessage()),HttpStatus.NOT_ACCEPTABLE);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

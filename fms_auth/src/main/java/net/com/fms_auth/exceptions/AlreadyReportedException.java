/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class AlreadyReportedException extends RuntimeException{
    public AlreadyReportedException(String message){
        super(message);
    }
}

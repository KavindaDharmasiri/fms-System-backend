/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.exception;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import net.com.fms_core.dto.ErrorDetailDTO;
import net.com.fms_core.enums.ErrorCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.Arrays;

public class HttpMessageNotReadableExceptions {
    public static ErrorDetailDTO getError(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
            Class<?> targetType = invalidFormatException.getTargetType();
            if (targetType.isEnum()) {
                String validValues = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .reduce((a, b) -> a + ", " + b) // Concatenates with a comma separator
                        .orElse("No valid values found");
                String errorMessage = String.format(
                        "Invalid value '%s'. Valid values are: [%s]",
                        invalidFormatException.getValue(),
                        validValues
                );
                return new ErrorDetailDTO(ErrorCode.INVALID_INPUT, errorMessage, null);
            }
        }
        return new ErrorDetailDTO(ErrorCode.INVALID_INPUT, "Invalid request", null);
    }
}

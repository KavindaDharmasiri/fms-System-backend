/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.exception;
import net.com.fms_core.dto.ErrorDetailDTO;
import net.com.fms_core.enums.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import java.util.List;

public class ConstraintViolationExceptions {
    public static List<ErrorDetailDTO> getErrors(ConstraintViolationException ex) {
        List<ErrorDetailDTO> errors = List.of();
        ex.getConstraintViolations().forEach(violation -> {
            errors.add(new ErrorDetailDTO(ErrorCode.INVALID_INPUT, violation.getPropertyPath().toString(), violation.getMessage()));
        });
        return errors;
    }
}

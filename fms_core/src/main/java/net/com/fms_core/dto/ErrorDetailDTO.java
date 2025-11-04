/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.*;
import net.com.fms_core.enums.ErrorCode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailDTO {
    private ErrorCode code;
    private String message;
    private String field;
}

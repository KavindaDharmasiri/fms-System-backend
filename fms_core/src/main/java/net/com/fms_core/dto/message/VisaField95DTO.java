/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto.message;
import lombok.Data;

@Data
public class VisaField95DTO {
    private String reserved; // Subfield 1 (Not Present)
    private String responseSourceReasonCode; // Subfield 2
}

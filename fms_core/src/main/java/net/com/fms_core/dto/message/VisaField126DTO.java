/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto.message;
import lombok.Data;

@Data
public class VisaField126DTO {
    private String visaMerchantIdentifier; // Subfield 5
    private String xid; // Subfield 8
    private String cavv; // Subfield 9
    private String cvv2Data; // Subfield 10
    private String serviceIndicator1; // Subfield 12
    private String serviceIndicator2; // Subfield 13
    private String threeDSecureIndicator; // Subfield 20
}

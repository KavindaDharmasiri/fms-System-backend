/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto.message;
import lombok.Data;

@Data
public class VisaField44DTO {
    private String responseSourceReasonCode; // Subfield 1
    private String avsResultCode; // Subfield 2
    private String tvcResultCode; // Subfield 3
    private String cardProductType; // Subfield 4
    private String cvvResultCode; // Subfield 5
    private String pacmDiversionLevel; // Subfield 6
    private String pacmDiversionReasonCode; // Subfield 7
    private String cardAuthenticationResultCode; // Subfield 8
    private String laAdditionalResponseData; // Subfield 9
    private String cvv2ResultCode; // Subfield 10
    private String originalResponseCode; // Subfield 11
    private String checkSettlementCode; // Subfield 12
    private String cavvResultCode; // Subfield 13
}

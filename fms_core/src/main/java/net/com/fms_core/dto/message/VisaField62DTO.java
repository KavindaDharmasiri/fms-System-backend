/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto.message;
import lombok.Data;

@Data
public class VisaField62DTO {
    private String customerName; // Subfield 1
    private String customerAddress; // Subfield 2
    private String billerAddress; // Subfield 3
    private String billerTelephoneNumber; // Subfield 4
    private String processByDate; // Subfield 5
    private String cardholderCertSerialNumber; // Subfield 6
    private String merchantCertSerialNumber; // Subfield 7
    private String transactionId; // Subfield 8
    private String transStain; // Subfield 9
    private String cvv2RequestData; // Subfield 10
    private String mvv; // Subfield 20
    private String customerName2; // Subfield 21
    private String customerName3; // Subfield 22
    private String productId; // Subfield 23
    private String programIdentifier; // Subfield 24
    private String spendQualifiedIndicator; // Subfield 25
}

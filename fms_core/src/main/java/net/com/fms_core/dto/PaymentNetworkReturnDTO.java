/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentNetworkReturnDTO {
    private Integer paymentNetworkId;
    private String networkName;
    private Double bin;
    private Double binLength;
    private String status;
    private String createdBy;
    private String updatedBy;
    private Date createdAt;
    private Date updatedAt;
}

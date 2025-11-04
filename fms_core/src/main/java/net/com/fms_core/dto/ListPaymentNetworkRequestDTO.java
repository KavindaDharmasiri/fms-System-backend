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
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListPaymentNetworkRequestDTO {
    private Integer paymentNetworkID;
    private String paymentNetworkName;
    private Double bin;
    private Double binLength;
    private String status;
    private int pageNo;
    private int pageSize;
}

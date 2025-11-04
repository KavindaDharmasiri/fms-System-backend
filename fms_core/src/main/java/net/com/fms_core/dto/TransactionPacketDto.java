/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kie.api.definition.rule.All;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionPacketDto {
    private Date timeStamp;
    private Integer tranId;
    private String cardNumber;
    private String acquirerBin;
    private Double tran_amount;
    private String currencyCode;
    private String merch_name;
    private String status;
}

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
@Getter
@Setter
public class TransactionFilterDto {
    private Integer page;
    private Integer size;
    private Integer transactionHistoryId;
    private String cardNumber;
    private String acquirerBin;
    private Double tran_amount;
    private String currencyCode;
    private Date fromDate;
    private Date toDate;
    private String merch_name;
    private String fromRiskScore;
    private String toRiskScore;
    private String paymentNetwork;
    private String ruleGroup;
    private String rule;
    private String status;
}

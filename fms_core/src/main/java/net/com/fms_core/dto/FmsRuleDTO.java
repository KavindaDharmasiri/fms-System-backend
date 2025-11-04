/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.com.fms_core.entity.*;
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FmsRuleDTO {
    private Integer fmsRuleId;
    private String ruleUuid;
    private String ruleName;
    private String description;
    private Date fromDate;
    private Date toDate;
    private String status;
    private Double finalRiskScore;
    private String finalRule;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer paymentNetworkId;
    private PaymentNetworkDTO paymentNetwork;
    private Collection<FmsRuleConditionDTO> fmsRuleConditionCollection;
    private Collection<TransactionFlaggedRulesDTO> transactionFlaggedRulesCollection;
    private Collection<RuleGroupRuleDTO> ruleGroupRuleCollection;
    private Collection<TestTransactionFlaggedRulesDTO> testTransactionFlaggedRulesCollection;
}

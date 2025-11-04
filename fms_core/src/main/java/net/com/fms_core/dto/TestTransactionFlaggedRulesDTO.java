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
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestTransactionFlaggedRulesDTO {
    private Integer testTransactionFlaggedRulesId;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private Double riskScore;
    private String flag;
    private String createdBy;
    private String updatedBy;
    private Integer fmsRuleId;
    private FmsRuleDTO FmsRule;
    private Integer ruleGroupId;
    private RuleGroupDTO ruleGroup;
    private Integer testTransactionId;
    private TestTransactionDTO testTransaction;
}

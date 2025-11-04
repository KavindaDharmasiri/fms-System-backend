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
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleGroupDTO {
    private Integer ruleGroupId;
    private String ruleGroupUuid;
    private String groupName;
    private String verdict;
    private Date fromDate;
    private Date toDate;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer ruleCount;
    private Collection<TransactionFlaggedRulesDTO> transactionFlaggedRulesCollection;
    private Collection<RuleGroupRuleDTO> ruleGroupRuleCollection;
    private Collection<RuleGroupRoleDTO> ruleGroupRoleCollection;
    private Collection<TestTransactionFlaggedRulesDTO> testTransactionFlaggedRulesCollection;
    private Integer paymentNetworkId;
    private PaymentNetworkDTO paymentNetwork;
    private Integer reactionTemplateId;
    private ReactionTemplateDTO reactionTemplate;
    private Integer ruleGroupRuleId;
    private Integer ruleGroupRoleId;
}

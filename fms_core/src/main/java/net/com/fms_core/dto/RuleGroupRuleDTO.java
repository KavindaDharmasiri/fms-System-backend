/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.com.fms_core.entity.FmsRule;
import net.com.fms_core.entity.RuleGroup;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleGroupRuleDTO {
    private Integer ruleGroupRuleId;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer fmsRuleId;
    private FmsRuleDTO FmsRule;
    private Integer ruleGroupId;
    private RuleGroupDTO ruleGroup;
}

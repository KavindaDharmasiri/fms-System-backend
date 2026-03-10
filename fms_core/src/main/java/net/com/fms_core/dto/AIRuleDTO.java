package net.com.fms_core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRuleDTO {
    
    private Integer aiRuleId;
    private String ruleName;
    private String ruleCode;
    private String description;
    private String droolRule;
    private Integer priority;
    private Boolean status;
    private Boolean isDeployed;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer aiRuleGroupId;
    private String aiRuleGroupName;
    private List<AIRuleConditionDTO> conditions;
}
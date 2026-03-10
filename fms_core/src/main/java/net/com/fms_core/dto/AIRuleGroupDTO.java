package net.com.fms_core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRuleGroupDTO {
    
    private Integer aiRuleGroupId;
    private String groupName;
    private String groupCode;
    private String description;
    private Integer priority;
    private Boolean status;
    private Boolean isDeployed;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AIRuleDTO> aiRules;
    private Integer ruleCount;
}
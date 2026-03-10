package net.com.fms_core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRuleConditionDTO {
    
    private Integer aiRuleConditionId;
    private String fieldName;
    private String operator;
    private String value;
    private String logicalOperator;
    private Integer conditionOrder;
    private Boolean status;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer aiRuleId;
}
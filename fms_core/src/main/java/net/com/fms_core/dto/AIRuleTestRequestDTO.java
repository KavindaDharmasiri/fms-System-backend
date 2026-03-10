package net.com.fms_core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRuleTestRequestDTO {
    
    private Integer aiRuleGroupId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
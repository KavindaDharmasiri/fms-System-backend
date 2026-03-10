package net.com.fms_core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRuleTestResultDTO {
    
    private Long transactionId;
    private String transactionUuid;
    private Double amount;
    private String pan;
    private String originalRiskLevel;
    private String newRiskLevel;
    private List<String> firedAIRules;
    private String status;
    private LocalDateTime transactionDate;
    private AIRuleTestDetailDTO details;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AIRuleTestDetailDTO {
        private String transactionPacket;
        private List<String> allFiredRules;
        private String blockReason;
        private Double riskScore;
        private Double fraudPercentage;
        private String actionStatus;
    }
}
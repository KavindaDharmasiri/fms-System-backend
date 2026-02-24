package net.com.fms_core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTestResultDTO {
    private String transactionId;
    private double amount;
    private String pan;
    private String riskLevel;
    private Double riskScore;
    private Double fraudPercentage;
    private List<String> firedRules;
    private List<TriggeredAction> triggeredActions;
    private String blockReason;
    private String status;
    private DistanceAnalysis distanceAnalysis;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistanceAnalysis {
        private double distanceKm;
        private long timeDifferenceMinutes;
        private double requiredSpeedKmh;
        private boolean impossible;
        private String riskLevel;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TriggeredAction {
        private String actionType;
        private String actionName;
        private String description;
        private String reactionTemplate;
    }
}

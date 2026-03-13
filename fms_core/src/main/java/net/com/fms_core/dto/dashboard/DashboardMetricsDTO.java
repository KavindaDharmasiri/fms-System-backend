package net.com.fms_core.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMetricsDTO {
    
    // Real-time metrics
    private RealTimeMetrics realTimeMetrics;
    
    // Transaction analytics
    private TransactionAnalytics transactionAnalytics;
    
    // Fraud detection metrics
    private FraudDetectionMetrics fraudDetectionMetrics;
    
    // Rule performance
    private RulePerformanceMetrics rulePerformanceMetrics;
    
    // Risk analysis
    private RiskAnalysisMetrics riskAnalysisMetrics;
    
    // System health
    private SystemHealthMetrics systemHealthMetrics;
    
    // Trends and forecasting
    private TrendAnalysisMetrics trendAnalysisMetrics;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RealTimeMetrics {
        private Long transactionsPerSecond;
        private Long transactionsPerMinute;
        private Long transactionsPerHour;
        private Long activeTransactions;
        private Double averageProcessingTime;
        private Long flaggedTransactionsToday;
        private Double fraudRateToday;
        private LocalDateTime lastUpdated;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionAnalytics {
        private Long totalTransactionsToday;
        private Long totalTransactionsWeek;
        private Long totalTransactionsMonth;
        private Double totalAmountToday;
        private Double totalAmountWeek;
        private Double totalAmountMonth;
        private Double averageTransactionAmount;
        private Map<String, Long> transactionsByHour;
        private Map<String, Long> transactionsByDay;
        private Map<String, Double> amountDistribution;
        private List<TopMerchantDTO> topMerchants;
        private List<TransactionVolumeDTO> volumeTrends;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FraudDetectionMetrics {
        private Long totalFraudDetected;
        private Long fraudDetectedToday;
        private Long fraudDetectedWeek;
        private Long fraudDetectedMonth;
        private Double fraudDetectionRate;
        private Double falsePositiveRate;
        private Double truePositiveRate;
        private Map<String, Long> fraudByRiskLevel;
        private Map<String, Long> fraudByAmount;
        private List<FraudTrendDTO> fraudTrends;
        private Double totalFraudAmount;
        private Double averageFraudAmount;
        private Map<String, Long> fraudByTimeOfDay;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RulePerformanceMetrics {
        private Long totalActiveRules;
        private Long totalRuleGroups;
        private Map<String, Long> ruleFireCount;
        private Map<String, Double> ruleEffectiveness;
        private Map<String, Double> ruleFalsePositiveRate;
        private List<RulePerformanceDTO> topPerformingRules;
        private List<RulePerformanceDTO> underPerformingRules;
        private Double overallRuleEfficiency;
        private LocalDateTime lastRuleUpdate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskAnalysisMetrics {
        private Map<String, Long> riskScoreDistribution;
        private Map<String, Long> fraudPercentageDistribution;
        private Double averageRiskScore;
        private Double averageFraudPercentage;
        private Long highRiskTransactions;
        private Long mediumRiskTransactions;
        private Long lowRiskTransactions;
        private List<RiskTrendDTO> riskTrends;
        private Map<String, Double> riskByMerchantCategory;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemHealthMetrics {
        private Double cpuUsage;
        private Double memoryUsage;
        private Double diskUsage;
        private Long activeConnections;
        private Double responseTime;
        private Long errorCount;
        private Double uptime;
        private String systemStatus;
        private List<AlertDTO> activeAlerts;
        private Map<String, String> serviceStatus;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendAnalysisMetrics {
        private List<PredictionDTO> fraudPredictions;
        private List<PredictionDTO> volumePredictions;
        private Map<String, Double> seasonalPatterns;
        private List<AnomalyDTO> detectedAnomalies;
        private Double trendAccuracy;
        private LocalDateTime lastAnalysisUpdate;
    }
    
    // Supporting DTOs
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopMerchantDTO {
        private String merchantId;
        private String merchantName;
        private Long transactionCount;
        private Double totalAmount;
        private Double fraudRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionVolumeDTO {
        private LocalDateTime timestamp;
        private Long volume;
        private Double amount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FraudTrendDTO {
        private LocalDateTime timestamp;
        private Long fraudCount;
        private Double fraudRate;
        private Double fraudAmount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RulePerformanceDTO {
        private String ruleName;
        private String ruleGroup;
        private Long fireCount;
        private Double effectiveness;
        private Double falsePositiveRate;
        private Double accuracy;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskTrendDTO {
        private LocalDateTime timestamp;
        private Double averageRiskScore;
        private Double averageFraudPercentage;
        private Long highRiskCount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlertDTO {
        private String alertId;
        private String alertType;
        private String severity;
        private String message;
        private LocalDateTime timestamp;
        private String status;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PredictionDTO {
        private LocalDateTime timestamp;
        private Double predictedValue;
        private Double confidence;
        private String predictionType;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnomalyDTO {
        private LocalDateTime timestamp;
        private String anomalyType;
        private Double severity;
        private String description;
        private Map<String, Object> details;
    }
}
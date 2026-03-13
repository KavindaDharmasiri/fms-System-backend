package net.com.fms_core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.ErrorDetailDTO;
import net.com.fms_core.dto.dashboard.DashboardMetricsDTO;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.repository.TransactionRepository;
import net.com.fms_core.repository.AIRuleRepository;
import net.com.fms_core.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    
    private final TransactionRepository transactionRepository;
    private final AIRuleRepository aiRuleRepository;
    
    @Override
    public ResponseEntity<ApiResponseDTO> getDashboardMetrics() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
            LocalDateTime startOfWeek = now.minusDays(7);
            LocalDateTime startOfMonth = now.minusDays(30);
            
            DashboardMetricsDTO metrics = DashboardMetricsDTO.builder()
                .realTimeMetrics(buildRealTimeMetrics())
                .transactionAnalytics(buildTransactionAnalytics(startOfDay, now))
                .fraudDetectionMetrics(buildFraudDetectionMetrics(startOfMonth, now))
                .rulePerformanceMetrics(buildRulePerformanceMetrics())
                .riskAnalysisMetrics(buildRiskAnalysisMetrics(startOfWeek, now))
                .systemHealthMetrics(buildSystemHealthMetrics())
                .trendAnalysisMetrics(buildTrendAnalysisMetrics())
                .build();
            
            return ResponseEntity.ok(ApiResponseDTO.success(metrics));
            
        } catch (Exception e) {
            log.error("Error getting dashboard metrics", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve dashboard metrics")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getRealTimeMetrics() {
        try {
            DashboardMetricsDTO.RealTimeMetrics metrics = buildRealTimeMetrics();
            return ResponseEntity.ok(ApiResponseDTO.success(metrics));
        } catch (Exception e) {
            log.error("Error getting real-time metrics", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve real-time metrics")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getTransactionAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            DashboardMetricsDTO.TransactionAnalytics analytics = buildTransactionAnalytics(startDate, endDate);
            return ResponseEntity.ok(ApiResponseDTO.success(analytics));
        } catch (Exception e) {
            log.error("Error getting transaction analytics", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve transaction analytics")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getFraudDetectionMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            DashboardMetricsDTO.FraudDetectionMetrics metrics = buildFraudDetectionMetrics(startDate, endDate);
            return ResponseEntity.ok(ApiResponseDTO.success(metrics));
        } catch (Exception e) {
            log.error("Error getting fraud detection metrics", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve fraud detection metrics")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getRulePerformanceMetrics() {
        try {
            DashboardMetricsDTO.RulePerformanceMetrics metrics = buildRulePerformanceMetrics();
            return ResponseEntity.ok(ApiResponseDTO.success(metrics));
        } catch (Exception e) {
            log.error("Error getting rule performance metrics", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve rule performance metrics")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getRiskAnalysisMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            DashboardMetricsDTO.RiskAnalysisMetrics metrics = buildRiskAnalysisMetrics(startDate, endDate);
            return ResponseEntity.ok(ApiResponseDTO.success(metrics));
        } catch (Exception e) {
            log.error("Error getting risk analysis metrics", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve risk analysis metrics")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getSystemHealthMetrics() {
        try {
            DashboardMetricsDTO.SystemHealthMetrics metrics = buildSystemHealthMetrics();
            return ResponseEntity.ok(ApiResponseDTO.success(metrics));
        } catch (Exception e) {
            log.error("Error getting system health metrics", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve system health metrics")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getTrendAnalysisMetrics() {
        try {
            DashboardMetricsDTO.TrendAnalysisMetrics metrics = buildTrendAnalysisMetrics();
            return ResponseEntity.ok(ApiResponseDTO.success(metrics));
        } catch (Exception e) {
            log.error("Error getting trend analysis metrics", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve trend analysis metrics")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getCustomAnalytics(Map<String, Object> filters) {
        try {
            // Implementation for custom analytics based on filters
            Map<String, Object> customMetrics = new HashMap<>();
            customMetrics.put("message", "Custom analytics implementation");
            customMetrics.put("filters", filters);
            
            return ResponseEntity.ok(ApiResponseDTO.success(customMetrics));
        } catch (Exception e) {
            log.error("Error getting custom analytics", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve custom analytics")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getComparativeAnalysis(LocalDateTime period1Start, LocalDateTime period1End,
                                                               LocalDateTime period2Start, LocalDateTime period2End) {
        try {
            Map<String, Object> comparison = new HashMap<>();
            
            // Get metrics for both periods
            DashboardMetricsDTO.TransactionAnalytics period1 = buildTransactionAnalytics(period1Start, period1End);
            DashboardMetricsDTO.TransactionAnalytics period2 = buildTransactionAnalytics(period2Start, period2End);
            
            comparison.put("period1", period1);
            comparison.put("period2", period2);
            comparison.put("comparison", calculateComparison(period1, period2));
            
            return ResponseEntity.ok(ApiResponseDTO.success(comparison));
        } catch (Exception e) {
            log.error("Error getting comparative analysis", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve comparative analysis")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getExecutiveSummary(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            Map<String, Object> summary = new HashMap<>();
            
            // Key executive metrics
            Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
            
            List<TransactionHistory> transactions = transactionRepository.findByCreatedAtBetween(start, end);
            
            long totalTransactions = transactions.size();
            long fraudTransactions = transactions.stream()
                .filter(t -> "FLAGGED".equals(t.getStatus()) || "BLOCKED".equals(t.getActionStatus()))
                .count();
            
            double fraudRate = totalTransactions > 0 ? (fraudTransactions * 100.0 / totalTransactions) : 0;
            
            summary.put("totalTransactions", totalTransactions);
            summary.put("fraudTransactions", fraudTransactions);
            summary.put("fraudRate", fraudRate);
            summary.put("period", Map.of("start", startDate, "end", endDate));
            
            return ResponseEntity.ok(ApiResponseDTO.success(summary));
        } catch (Exception e) {
            log.error("Error getting executive summary", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve executive summary")
                    .build()));
        }
    }
    
    // Private helper methods for building metrics
    
    private DashboardMetricsDTO.RealTimeMetrics buildRealTimeMetrics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteAgo = now.minusMinutes(1);
        LocalDateTime oneHourAgo = now.minusHours(1);
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date oneMinuteAgoDate = Date.from(oneMinuteAgo.atZone(ZoneId.systemDefault()).toInstant());
        Date oneHourAgoDate = Date.from(oneHourAgo.atZone(ZoneId.systemDefault()).toInstant());
        Date startOfDayDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
        
        long transactionsLastMinute = transactionRepository.countByCreatedAtBetween(oneMinuteAgoDate, nowDate);
        long transactionsLastHour = transactionRepository.countByCreatedAtBetween(oneHourAgoDate, nowDate);
        long transactionsToday = transactionRepository.countByCreatedAtBetween(startOfDayDate, nowDate);
        
        long flaggedToday = transactionRepository.countByStatusAndCreatedAtBetween("FLAGGED", startOfDayDate, nowDate);
        double fraudRateToday = transactionsToday > 0 ? (flaggedToday * 100.0 / transactionsToday) : 0;
        
        return DashboardMetricsDTO.RealTimeMetrics.builder()
            .transactionsPerSecond(transactionsLastMinute / 60)
            .transactionsPerMinute(transactionsLastMinute)
            .transactionsPerHour(transactionsLastHour)
            .activeTransactions(transactionsLastMinute)
            .averageProcessingTime(calculateAverageProcessingTime())
            .flaggedTransactionsToday(flaggedToday)
            .fraudRateToday(fraudRateToday)
            .lastUpdated(now)
            .build();
    }
    
    private DashboardMetricsDTO.TransactionAnalytics buildTransactionAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        
        List<TransactionHistory> transactions = transactionRepository.findByCreatedAtBetween(start, end);
        
        // Calculate basic metrics
        long totalTransactions = transactions.size();
        
        // Calculate amounts from transaction packets
        double totalAmount = transactions.stream()
            .mapToDouble(this::extractAmountFromTransaction)
            .sum();
        
        double averageAmount = totalTransactions > 0 ? totalAmount / totalTransactions : 0;
        
        // Build hourly distribution
        Map<String, Long> transactionsByHour = transactions.stream()
            .collect(Collectors.groupingBy(
                t -> String.valueOf(t.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).getHour()),
                Collectors.counting()
            ));
        
        // Build daily distribution for the period
        Map<String, Long> transactionsByDay = transactions.stream()
            .collect(Collectors.groupingBy(
                t -> t.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                Collectors.counting()
            ));
        
        // Amount distribution
        Map<String, Double> amountDistribution = buildAmountDistribution(transactions);
        
        // Top merchants (mock data for now)
        List<DashboardMetricsDTO.TopMerchantDTO> topMerchants = buildTopMerchants(transactions);
        
        // Volume trends
        List<DashboardMetricsDTO.TransactionVolumeDTO> volumeTrends = buildVolumeTrends(transactions);
        
        return DashboardMetricsDTO.TransactionAnalytics.builder()
            .totalTransactionsToday(totalTransactions)
            .totalTransactionsWeek(totalTransactions) // Simplified for demo
            .totalTransactionsMonth(totalTransactions) // Simplified for demo
            .totalAmountToday(totalAmount)
            .totalAmountWeek(totalAmount) // Simplified for demo
            .totalAmountMonth(totalAmount) // Simplified for demo
            .averageTransactionAmount(averageAmount)
            .transactionsByHour(transactionsByHour)
            .transactionsByDay(transactionsByDay)
            .amountDistribution(amountDistribution)
            .topMerchants(topMerchants)
            .volumeTrends(volumeTrends)
            .build();
    }
    
    private DashboardMetricsDTO.FraudDetectionMetrics buildFraudDetectionMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        
        List<TransactionHistory> allTransactions = transactionRepository.findByCreatedAtBetween(start, end);
        List<TransactionHistory> fraudTransactions = allTransactions.stream()
            .filter(t -> "FLAGGED".equals(t.getStatus()) || "BLOCKED".equals(t.getActionStatus()))
            .collect(Collectors.toList());
        
        long totalTransactions = allTransactions.size();
        long fraudCount = fraudTransactions.size();
        double fraudRate = totalTransactions > 0 ? (fraudCount * 100.0 / totalTransactions) : 0;
        
        // Calculate fraud amounts
        double totalFraudAmount = fraudTransactions.stream()
            .mapToDouble(this::extractAmountFromTransaction)
            .sum();
        
        double averageFraudAmount = fraudCount > 0 ? totalFraudAmount / fraudCount : 0;
        
        // Fraud by risk level
        Map<String, Long> fraudByRiskLevel = fraudTransactions.stream()
            .collect(Collectors.groupingBy(
                t -> extractRiskLevelFromTransaction(t),
                Collectors.counting()
            ));
        
        // Fraud by amount ranges
        Map<String, Long> fraudByAmount = buildFraudByAmountRanges(fraudTransactions);
        
        // Fraud trends
        List<DashboardMetricsDTO.FraudTrendDTO> fraudTrends = buildFraudTrends(fraudTransactions);
        
        // Fraud by time of day
        Map<String, Long> fraudByTimeOfDay = fraudTransactions.stream()
            .collect(Collectors.groupingBy(
                t -> String.valueOf(t.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).getHour()),
                Collectors.counting()
            ));
        
        return DashboardMetricsDTO.FraudDetectionMetrics.builder()
            .totalFraudDetected(fraudCount)
            .fraudDetectedToday(fraudCount) // Simplified for demo
            .fraudDetectedWeek(fraudCount) // Simplified for demo
            .fraudDetectedMonth(fraudCount) // Simplified for demo
            .fraudDetectionRate(fraudRate)
            .falsePositiveRate(calculateFalsePositiveRate(allTransactions))
            .truePositiveRate(calculateTruePositiveRate(allTransactions))
            .fraudByRiskLevel(fraudByRiskLevel)
            .fraudByAmount(fraudByAmount)
            .fraudTrends(fraudTrends)
            .totalFraudAmount(totalFraudAmount)
            .averageFraudAmount(averageFraudAmount)
            .fraudByTimeOfDay(fraudByTimeOfDay)
            .build();
    }
    
    private DashboardMetricsDTO.RulePerformanceMetrics buildRulePerformanceMetrics() {
        // Get rule performance data
        long totalActiveRules = aiRuleRepository.countByStatusTrue();
        
        // Mock rule performance data (in real implementation, this would come from rule execution logs)
        Map<String, Long> ruleFireCount = Map.of(
            "Velocity Rule", 150L,
            "Amount Threshold Rule", 89L,
            "Geographic Risk Rule", 67L,
            "Time-based Rule", 45L,
            "Merchant Category Rule", 23L
        );
        
        Map<String, Double> ruleEffectiveness = Map.of(
            "Velocity Rule", 85.5,
            "Amount Threshold Rule", 78.2,
            "Geographic Risk Rule", 92.1,
            "Time-based Rule", 67.8,
            "Merchant Category Rule", 71.4
        );
        
        Map<String, Double> ruleFalsePositiveRate = Map.of(
            "Velocity Rule", 12.5,
            "Amount Threshold Rule", 18.7,
            "Geographic Risk Rule", 8.9,
            "Time-based Rule", 25.3,
            "Merchant Category Rule", 15.2
        );
        
        List<DashboardMetricsDTO.RulePerformanceDTO> topPerformingRules = buildTopPerformingRules();
        List<DashboardMetricsDTO.RulePerformanceDTO> underPerformingRules = buildUnderPerformingRules();
        
        double overallEfficiency = ruleEffectiveness.values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
        
        return DashboardMetricsDTO.RulePerformanceMetrics.builder()
            .totalActiveRules(totalActiveRules)
            .totalRuleGroups(5L) // Mock data
            .ruleFireCount(ruleFireCount)
            .ruleEffectiveness(ruleEffectiveness)
            .ruleFalsePositiveRate(ruleFalsePositiveRate)
            .topPerformingRules(topPerformingRules)
            .underPerformingRules(underPerformingRules)
            .overallRuleEfficiency(overallEfficiency)
            .lastRuleUpdate(LocalDateTime.now().minusHours(2))
            .build();
    }
    
    private DashboardMetricsDTO.RiskAnalysisMetrics buildRiskAnalysisMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        
        List<TransactionHistory> transactions = transactionRepository.findByCreatedAtBetween(start, end);
        
        // Risk score distribution
        Map<String, Long> riskScoreDistribution = transactions.stream()
            .collect(Collectors.groupingBy(
                this::categorizeRiskScore,
                Collectors.counting()
            ));
        
        // Fraud percentage distribution
        Map<String, Long> fraudPercentageDistribution = transactions.stream()
            .filter(t -> t.getFraudPercentage() != null)
            .collect(Collectors.groupingBy(
                this::categorizeFraudPercentage,
                Collectors.counting()
            ));
        
        // Calculate averages
        double averageRiskScore = transactions.stream()
            .mapToDouble(this::extractRiskScoreFromTransaction)
            .average()
            .orElse(0.0);
        
        double averageFraudPercentage = transactions.stream()
            .filter(t -> t.getFraudPercentage() != null)
            .mapToDouble(TransactionHistory::getFraudPercentage)
            .average()
            .orElse(0.0);
        
        // Risk level counts
        long highRiskTransactions = transactions.stream()
            .filter(t -> extractRiskScoreFromTransaction(t) > 70)
            .count();
        
        long mediumRiskTransactions = transactions.stream()
            .filter(t -> {
                double score = extractRiskScoreFromTransaction(t);
                return score >= 30 && score <= 70;
            })
            .count();
        
        long lowRiskTransactions = transactions.stream()
            .filter(t -> extractRiskScoreFromTransaction(t) < 30)
            .count();
        
        List<DashboardMetricsDTO.RiskTrendDTO> riskTrends = buildRiskTrends(transactions);
        
        Map<String, Double> riskByMerchantCategory = buildRiskByMerchantCategory(transactions);
        
        return DashboardMetricsDTO.RiskAnalysisMetrics.builder()
            .riskScoreDistribution(riskScoreDistribution)
            .fraudPercentageDistribution(fraudPercentageDistribution)
            .averageRiskScore(averageRiskScore)
            .averageFraudPercentage(averageFraudPercentage)
            .highRiskTransactions(highRiskTransactions)
            .mediumRiskTransactions(mediumRiskTransactions)
            .lowRiskTransactions(lowRiskTransactions)
            .riskTrends(riskTrends)
            .riskByMerchantCategory(riskByMerchantCategory)
            .build();
    }
    
    private DashboardMetricsDTO.SystemHealthMetrics buildSystemHealthMetrics() {
        // Mock system health data (in real implementation, this would come from system monitoring)
        List<DashboardMetricsDTO.AlertDTO> activeAlerts = Arrays.asList(
            DashboardMetricsDTO.AlertDTO.builder()
                .alertId("ALT001")
                .alertType("HIGH_FRAUD_RATE")
                .severity("HIGH")
                .message("Fraud rate exceeded 15% threshold")
                .timestamp(LocalDateTime.now().minusMinutes(30))
                .status("ACTIVE")
                .build(),
            DashboardMetricsDTO.AlertDTO.builder()
                .alertId("ALT002")
                .alertType("SYSTEM_PERFORMANCE")
                .severity("MEDIUM")
                .message("Response time increased by 20%")
                .timestamp(LocalDateTime.now().minusHours(1))
                .status("ACKNOWLEDGED")
                .build()
        );
        
        Map<String, String> serviceStatus = Map.of(
            "fraud-detection", "HEALTHY",
            "rule-engine", "HEALTHY",
            "transaction-processor", "WARNING",
            "notification-service", "HEALTHY",
            "database", "HEALTHY"
        );
        
        return DashboardMetricsDTO.SystemHealthMetrics.builder()
            .cpuUsage(65.5)
            .memoryUsage(78.2)
            .diskUsage(45.8)
            .activeConnections(234L)
            .responseTime(125.5)
            .errorCount(12L)
            .uptime(99.8)
            .systemStatus("HEALTHY")
            .activeAlerts(activeAlerts)
            .serviceStatus(serviceStatus)
            .build();
    }
    
    private DashboardMetricsDTO.TrendAnalysisMetrics buildTrendAnalysisMetrics() {
        // Mock trend analysis data
        List<DashboardMetricsDTO.PredictionDTO> fraudPredictions = Arrays.asList(
            DashboardMetricsDTO.PredictionDTO.builder()
                .timestamp(LocalDateTime.now().plusHours(1))
                .predictedValue(12.5)
                .confidence(85.2)
                .predictionType("FRAUD_RATE")
                .build(),
            DashboardMetricsDTO.PredictionDTO.builder()
                .timestamp(LocalDateTime.now().plusHours(2))
                .predictedValue(14.1)
                .confidence(82.7)
                .predictionType("FRAUD_RATE")
                .build()
        );
        
        List<DashboardMetricsDTO.PredictionDTO> volumePredictions = Arrays.asList(
            DashboardMetricsDTO.PredictionDTO.builder()
                .timestamp(LocalDateTime.now().plusHours(1))
                .predictedValue(1250.0)
                .confidence(91.5)
                .predictionType("TRANSACTION_VOLUME")
                .build()
        );
        
        Map<String, Double> seasonalPatterns = Map.of(
            "MORNING_PEAK", 1.25,
            "LUNCH_PEAK", 1.45,
            "EVENING_PEAK", 1.65,
            "WEEKEND_PATTERN", 0.85,
            "HOLIDAY_PATTERN", 0.65
        );
        
        List<DashboardMetricsDTO.AnomalyDTO> detectedAnomalies = Arrays.asList(
            DashboardMetricsDTO.AnomalyDTO.builder()
                .timestamp(LocalDateTime.now().minusMinutes(45))
                .anomalyType("TRANSACTION_SPIKE")
                .severity(75.5)
                .description("Unusual transaction volume spike detected")
                .details(Map.of("expected", 100, "actual", 250, "deviation", 150))
                .build()
        );
        
        return DashboardMetricsDTO.TrendAnalysisMetrics.builder()
            .fraudPredictions(fraudPredictions)
            .volumePredictions(volumePredictions)
            .seasonalPatterns(seasonalPatterns)
            .detectedAnomalies(detectedAnomalies)
            .trendAccuracy(87.3)
            .lastAnalysisUpdate(LocalDateTime.now().minusMinutes(15))
            .build();
    }
    
    // Additional helper methods
    
    private double calculateAverageProcessingTime() {
        // Mock calculation - in real implementation, this would be based on actual processing metrics
        return 125.5; // milliseconds
    }
    
    private double extractAmountFromTransaction(TransactionHistory transaction) {
        // Extract amount from transaction packet JSON
        try {
            // This is a simplified extraction - in real implementation, parse the JSON packet
            return Math.random() * 1000; // Mock amount for demo
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private String extractRiskLevelFromTransaction(TransactionHistory transaction) {
        // Extract risk level from transaction data
        if (transaction.getFraudPercentage() != null) {
            double fraudPercentage = transaction.getFraudPercentage();
            if (fraudPercentage > 70) return "HIGH";
            if (fraudPercentage > 30) return "MEDIUM";
            return "LOW";
        }
        return "UNKNOWN";
    }
    
    private double extractRiskScoreFromTransaction(TransactionHistory transaction) {
        // Extract risk score from transaction data
        if (transaction.getFraudPercentage() != null) {
            return transaction.getFraudPercentage();
        }
        return Math.random() * 100; // Mock score for demo
    }
    
    private String categorizeRiskScore(TransactionHistory transaction) {
        double score = extractRiskScoreFromTransaction(transaction);
        if (score > 80) return "Critical (80-100)";
        if (score > 60) return "High (60-80)";
        if (score > 40) return "Medium (40-60)";
        if (score > 20) return "Low (20-40)";
        return "Very Low (0-20)";
    }
    
    private String categorizeFraudPercentage(TransactionHistory transaction) {
        if (transaction.getFraudPercentage() == null) return "Unknown";
        double percentage = transaction.getFraudPercentage();
        if (percentage > 75) return "Very High (75-100%)";
        if (percentage > 50) return "High (50-75%)";
        if (percentage > 25) return "Medium (25-50%)";
        return "Low (0-25%)";
    }
    
    private Map<String, Double> buildAmountDistribution(List<TransactionHistory> transactions) {
        Map<String, Double> distribution = new HashMap<>();
        
        long smallAmounts = transactions.stream()
            .filter(t -> extractAmountFromTransaction(t) < 100)
            .count();
        
        long mediumAmounts = transactions.stream()
            .filter(t -> {
                double amount = extractAmountFromTransaction(t);
                return amount >= 100 && amount < 1000;
            })
            .count();
        
        long largeAmounts = transactions.stream()
            .filter(t -> extractAmountFromTransaction(t) >= 1000)
            .count();
        
        long total = transactions.size();
        if (total > 0) {
            distribution.put("Small (<$100)", (smallAmounts * 100.0) / total);
            distribution.put("Medium ($100-$1000)", (mediumAmounts * 100.0) / total);
            distribution.put("Large (>$1000)", (largeAmounts * 100.0) / total);
        }
        
        return distribution;
    }
    
    private List<DashboardMetricsDTO.TopMerchantDTO> buildTopMerchants(List<TransactionHistory> transactions) {
        // Mock top merchants data
        return Arrays.asList(
            DashboardMetricsDTO.TopMerchantDTO.builder()
                .merchantId("MERCH001")
                .merchantName("Amazon")
                .transactionCount(1250L)
                .totalAmount(125000.0)
                .fraudRate(2.5)
                .build(),
            DashboardMetricsDTO.TopMerchantDTO.builder()
                .merchantId("MERCH002")
                .merchantName("Walmart")
                .transactionCount(980L)
                .totalAmount(89500.0)
                .fraudRate(1.8)
                .build(),
            DashboardMetricsDTO.TopMerchantDTO.builder()
                .merchantId("MERCH003")
                .merchantName("Target")
                .transactionCount(756L)
                .totalAmount(67800.0)
                .fraudRate(3.2)
                .build()
        );
    }
    
    private List<DashboardMetricsDTO.TransactionVolumeDTO> buildVolumeTrends(List<TransactionHistory> transactions) {
        // Build hourly volume trends
        Map<Integer, Long> hourlyVolume = transactions.stream()
            .collect(Collectors.groupingBy(
                t -> t.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).getHour(),
                Collectors.counting()
            ));
        
        return hourlyVolume.entrySet().stream()
            .map(entry -> DashboardMetricsDTO.TransactionVolumeDTO.builder()
                .timestamp(LocalDateTime.now().withHour(entry.getKey()).withMinute(0).withSecond(0))
                .volume(entry.getValue())
                .amount(entry.getValue() * 150.0) // Mock amount calculation
                .build())
            .collect(Collectors.toList());
    }
    
    private Map<String, Long> buildFraudByAmountRanges(List<TransactionHistory> fraudTransactions) {
        Map<String, Long> ranges = new HashMap<>();
        
        ranges.put("Small (<$100)", fraudTransactions.stream()
            .filter(t -> extractAmountFromTransaction(t) < 100)
            .count());
        
        ranges.put("Medium ($100-$1000)", fraudTransactions.stream()
            .filter(t -> {
                double amount = extractAmountFromTransaction(t);
                return amount >= 100 && amount < 1000;
            })
            .count());
        
        ranges.put("Large ($1000-$10000)", fraudTransactions.stream()
            .filter(t -> {
                double amount = extractAmountFromTransaction(t);
                return amount >= 1000 && amount < 10000;
            })
            .count());
        
        ranges.put("Very Large (>$10000)", fraudTransactions.stream()
            .filter(t -> extractAmountFromTransaction(t) >= 10000)
            .count());
        
        return ranges;
    }
    
    private List<DashboardMetricsDTO.FraudTrendDTO> buildFraudTrends(List<TransactionHistory> fraudTransactions) {
        // Build daily fraud trends
        Map<String, List<TransactionHistory>> dailyFraud = fraudTransactions.stream()
            .collect(Collectors.groupingBy(
                t -> t.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()
            ));
        
        return dailyFraud.entrySet().stream()
            .map(entry -> {
                List<TransactionHistory> dayTransactions = entry.getValue();
                double totalAmount = dayTransactions.stream()
                    .mapToDouble(this::extractAmountFromTransaction)
                    .sum();
                
                return DashboardMetricsDTO.FraudTrendDTO.builder()
                    .timestamp(LocalDateTime.parse(entry.getKey() + "T00:00:00"))
                    .fraudCount((long) dayTransactions.size())
                    .fraudRate(15.5) // Mock fraud rate
                    .fraudAmount(totalAmount)
                    .build();
            })
            .collect(Collectors.toList());
    }
    
    private double calculateFalsePositiveRate(List<TransactionHistory> transactions) {
        // Mock calculation - in real implementation, this would be based on manual review results
        return 12.5;
    }
    
    private double calculateTruePositiveRate(List<TransactionHistory> transactions) {
        // Mock calculation - in real implementation, this would be based on confirmed fraud cases
        return 87.5;
    }
    
    private List<DashboardMetricsDTO.RulePerformanceDTO> buildTopPerformingRules() {
        return Arrays.asList(
            DashboardMetricsDTO.RulePerformanceDTO.builder()
                .ruleName("Geographic Risk Rule")
                .ruleGroup("Location Rules")
                .fireCount(67L)
                .effectiveness(92.1)
                .falsePositiveRate(8.9)
                .accuracy(91.2)
                .build(),
            DashboardMetricsDTO.RulePerformanceDTO.builder()
                .ruleName("Velocity Rule")
                .ruleGroup("Behavioral Rules")
                .fireCount(150L)
                .effectiveness(85.5)
                .falsePositiveRate(12.5)
                .accuracy(87.3)
                .build()
        );
    }
    
    private List<DashboardMetricsDTO.RulePerformanceDTO> buildUnderPerformingRules() {
        return Arrays.asList(
            DashboardMetricsDTO.RulePerformanceDTO.builder()
                .ruleName("Time-based Rule")
                .ruleGroup("Temporal Rules")
                .fireCount(45L)
                .effectiveness(67.8)
                .falsePositiveRate(25.3)
                .accuracy(72.5)
                .build()
        );
    }
    
    private List<DashboardMetricsDTO.RiskTrendDTO> buildRiskTrends(List<TransactionHistory> transactions) {
        // Build daily risk trends
        Map<String, List<TransactionHistory>> dailyTransactions = transactions.stream()
            .collect(Collectors.groupingBy(
                t -> t.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()
            ));
        
        return dailyTransactions.entrySet().stream()
            .map(entry -> {
                List<TransactionHistory> dayTransactions = entry.getValue();
                
                double avgRiskScore = dayTransactions.stream()
                    .mapToDouble(this::extractRiskScoreFromTransaction)
                    .average()
                    .orElse(0.0);
                
                double avgFraudPercentage = dayTransactions.stream()
                    .filter(t -> t.getFraudPercentage() != null)
                    .mapToDouble(TransactionHistory::getFraudPercentage)
                    .average()
                    .orElse(0.0);
                
                long highRiskCount = dayTransactions.stream()
                    .filter(t -> extractRiskScoreFromTransaction(t) > 70)
                    .count();
                
                return DashboardMetricsDTO.RiskTrendDTO.builder()
                    .timestamp(LocalDateTime.parse(entry.getKey() + "T00:00:00"))
                    .averageRiskScore(avgRiskScore)
                    .averageFraudPercentage(avgFraudPercentage)
                    .highRiskCount(highRiskCount)
                    .build();
            })
            .collect(Collectors.toList());
    }
    
    private Map<String, Double> buildRiskByMerchantCategory(List<TransactionHistory> transactions) {
        // Mock merchant category risk analysis
        return Map.of(
            "Online Retail", 15.5,
            "Gas Stations", 8.2,
            "Restaurants", 12.1,
            "ATM Withdrawals", 25.8,
            "Grocery Stores", 5.3,
            "Entertainment", 18.7
        );
    }
    
    private Map<String, Object> calculateComparison(DashboardMetricsDTO.TransactionAnalytics period1, 
                                                   DashboardMetricsDTO.TransactionAnalytics period2) {
        Map<String, Object> comparison = new HashMap<>();
        
        // Calculate percentage changes
        double transactionChange = calculatePercentageChange(
            period1.getTotalTransactionsToday(), 
            period2.getTotalTransactionsToday()
        );
        
        double amountChange = calculatePercentageChange(
            period1.getTotalAmountToday(), 
            period2.getTotalAmountToday()
        );
        
        comparison.put("transactionVolumeChange", transactionChange);
        comparison.put("transactionAmountChange", amountChange);
        comparison.put("trend", transactionChange > 0 ? "INCREASING" : "DECREASING");
        
        return comparison;
    }
    
    private double calculatePercentageChange(Number current, Number previous) {
        if (previous.doubleValue() == 0) return 0.0;
        return ((current.doubleValue() - previous.doubleValue()) / previous.doubleValue()) * 100;
    }
}
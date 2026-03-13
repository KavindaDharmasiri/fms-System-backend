package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    /**
     * Get comprehensive dashboard metrics
     */
    @GetMapping("/metrics")
    public ResponseEntity<ApiResponseDTO> getDashboardMetrics() {
        log.info("Fetching comprehensive dashboard metrics");
        return dashboardService.getDashboardMetrics();
    }
    
    /**
     * Get real-time metrics for live dashboard updates
     */
    @GetMapping("/real-time")
    public ResponseEntity<ApiResponseDTO> getRealTimeMetrics() {
        log.info("Fetching real-time metrics");
        return dashboardService.getRealTimeMetrics();
    }
    
    /**
     * Get transaction analytics for specified time period
     */
    @GetMapping("/analytics/transactions")
    public ResponseEntity<ApiResponseDTO> getTransactionAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.info("Fetching transaction analytics from {} to {}", startDate, endDate);
        return dashboardService.getTransactionAnalytics(startDate, endDate);
    }
    
    /**
     * Get fraud detection metrics and trends
     */
    @GetMapping("/analytics/fraud")
    public ResponseEntity<ApiResponseDTO> getFraudDetectionMetrics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.info("Fetching fraud detection metrics from {} to {}", startDate, endDate);
        return dashboardService.getFraudDetectionMetrics(startDate, endDate);
    }
    
    /**
     * Get rule performance analytics
     */
    @GetMapping("/analytics/rules")
    public ResponseEntity<ApiResponseDTO> getRulePerformanceMetrics() {
        log.info("Fetching rule performance metrics");
        return dashboardService.getRulePerformanceMetrics();
    }
    
    /**
     * Get risk analysis and distribution
     */
    @GetMapping("/analytics/risk")
    public ResponseEntity<ApiResponseDTO> getRiskAnalysisMetrics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.info("Fetching risk analysis metrics from {} to {}", startDate, endDate);
        return dashboardService.getRiskAnalysisMetrics(startDate, endDate);
    }
    
    /**
     * Get system health and performance metrics
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponseDTO> getSystemHealthMetrics() {
        log.info("Fetching system health metrics");
        return dashboardService.getSystemHealthMetrics();
    }
    
    /**
     * Get trend analysis and predictions
     */
    @GetMapping("/analytics/trends")
    public ResponseEntity<ApiResponseDTO> getTrendAnalysisMetrics() {
        log.info("Fetching trend analysis metrics");
        return dashboardService.getTrendAnalysisMetrics();
    }
    
    /**
     * Get custom analytics based on filters
     */
    @PostMapping("/analytics/custom")
    public ResponseEntity<ApiResponseDTO> getCustomAnalytics(@RequestBody Map<String, Object> filters) {
        log.info("Fetching custom analytics with filters: {}", filters);
        return dashboardService.getCustomAnalytics(filters);
    }
    
    /**
     * Get comparative analysis between time periods
     */
    @GetMapping("/analytics/compare")
    public ResponseEntity<ApiResponseDTO> getComparativeAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime period1Start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime period1End,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime period2Start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime period2End) {
        
        log.info("Fetching comparative analysis: Period 1 ({} to {}) vs Period 2 ({} to {})", 
                period1Start, period1End, period2Start, period2End);
        return dashboardService.getComparativeAnalysis(period1Start, period1End, period2Start, period2End);
    }
    
    /**
     * Get executive summary for management reporting
     */
    @GetMapping("/executive-summary")
    public ResponseEntity<ApiResponseDTO> getExecutiveSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.info("Fetching executive summary from {} to {}", startDate, endDate);
        return dashboardService.getExecutiveSummary(startDate, endDate);
    }
    
    /**
     * Get quick stats for dashboard widgets
     */
    @GetMapping("/quick-stats")
    public ResponseEntity<ApiResponseDTO> getQuickStats() {
        log.info("Fetching quick stats");
        
        // Get today's metrics
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        
        return dashboardService.getTransactionAnalytics(startOfDay, now);
    }
    
    /**
     * Get fraud alerts summary
     */
    @GetMapping("/alerts")
    public ResponseEntity<ApiResponseDTO> getFraudAlerts(
            @RequestParam(defaultValue = "24") int hours) {
        
        log.info("Fetching fraud alerts for last {} hours", hours);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusHours(hours);
        
        return dashboardService.getFraudDetectionMetrics(startTime, now);
    }
    
    /**
     * Get performance summary
     */
    @GetMapping("/performance")
    public ResponseEntity<ApiResponseDTO> getPerformanceSummary() {
        log.info("Fetching performance summary");
        return dashboardService.getSystemHealthMetrics();
    }
    
    /**
     * Get transaction volume by time periods
     */
    @GetMapping("/volume/{period}")
    public ResponseEntity<ApiResponseDTO> getTransactionVolume(@PathVariable String period) {
        log.info("Fetching transaction volume for period: {}", period);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime;
        
        switch (period.toLowerCase()) {
            case "hour":
                startTime = now.minusHours(1);
                break;
            case "day":
                startTime = now.minusDays(1);
                break;
            case "week":
                startTime = now.minusWeeks(1);
                break;
            case "month":
                startTime = now.minusMonths(1);
                break;
            default:
                startTime = now.minusDays(1);
        }
        
        return dashboardService.getTransactionAnalytics(startTime, now);
    }
    
    /**
     * Get fraud detection summary by risk level
     */
    @GetMapping("/fraud-summary")
    public ResponseEntity<ApiResponseDTO> getFraudSummary(
            @RequestParam(defaultValue = "7") int days) {
        
        log.info("Fetching fraud summary for last {} days", days);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(days);
        
        return dashboardService.getFraudDetectionMetrics(startTime, now);
    }
}
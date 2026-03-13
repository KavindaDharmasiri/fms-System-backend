package net.com.fms_core.service;

import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.dashboard.DashboardMetricsDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

public interface DashboardService {
    
    /**
     * Get comprehensive dashboard metrics
     */
    ResponseEntity<ApiResponseDTO> getDashboardMetrics();
    
    /**
     * Get real-time metrics for live dashboard updates
     */
    ResponseEntity<ApiResponseDTO> getRealTimeMetrics();
    
    /**
     * Get transaction analytics for specified time period
     */
    ResponseEntity<ApiResponseDTO> getTransactionAnalytics(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get fraud detection metrics and trends
     */
    ResponseEntity<ApiResponseDTO> getFraudDetectionMetrics(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get rule performance analytics
     */
    ResponseEntity<ApiResponseDTO> getRulePerformanceMetrics();
    
    /**
     * Get risk analysis and distribution
     */
    ResponseEntity<ApiResponseDTO> getRiskAnalysisMetrics(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get system health and performance metrics
     */
    ResponseEntity<ApiResponseDTO> getSystemHealthMetrics();
    
    /**
     * Get trend analysis and predictions
     */
    ResponseEntity<ApiResponseDTO> getTrendAnalysisMetrics();
    
    /**
     * Get custom analytics based on filters
     */
    ResponseEntity<ApiResponseDTO> getCustomAnalytics(Map<String, Object> filters);
    
    /**
     * Get comparative analysis between time periods
     */
    ResponseEntity<ApiResponseDTO> getComparativeAnalysis(LocalDateTime period1Start, LocalDateTime period1End,
                                                         LocalDateTime period2Start, LocalDateTime period2End);
    
    /**
     * Get executive summary for management reporting
     */
    ResponseEntity<ApiResponseDTO> getExecutiveSummary(LocalDateTime startDate, LocalDateTime endDate);
}
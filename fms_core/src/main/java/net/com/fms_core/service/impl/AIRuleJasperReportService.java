package net.com.fms_core.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.AIRuleTestResultDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AIRuleJasperReportService {

    private static final String REPORT_TEMPLATE = "reports/ai_rule_test_report.jrxml";
    private static final String EXECUTIVE_TEMPLATE = "reports/ai_rule_executive_summary.jrxml";
    private static final String SIMPLE_TEMPLATE = "reports/ai_rule_simple_summary.jrxml";
    private static final String DETAILED_TEMPLATE = "reports/ai_rule_detailed_analysis.jrxml";
    private static final String COMPREHENSIVE_TEMPLATE = "reports/ai_rule_comprehensive_analysis.jrxml";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Generate comprehensive PDF report using JasperReports
     */
    public byte[] generateJasperPDFReport(List<AIRuleTestResultDTO> testResults, String reportTitle) {
        try {
            log.info("Starting JasperReports PDF generation for {} test results", testResults.size());

            // Load and compile the report template
            InputStream reportStream = new ClassPathResource(REPORT_TEMPLATE).getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Prepare data source
            List<Map<String, Object>> reportData = prepareReportData(testResults);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

            // Prepare parameters
            Map<String, Object> parameters = prepareReportParameters(testResults, reportTitle);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export to PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            log.info("JasperReports PDF generation completed successfully. PDF size: {} bytes", pdfBytes.length);
            return pdfBytes;

        } catch (Exception e) {
            log.error("Error generating JasperReports PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate PDF report using JasperReports", e);
        }
    }

    /**
     * Prepare data for the report
     */
    private List<Map<String, Object>> prepareReportData(List<AIRuleTestResultDTO> testResults) {
        return testResults.stream().map(result -> {
            Map<String, Object> data = new HashMap<>();
            data.put("transactionId", result.getTransactionId());
            data.put("transactionUuid", result.getTransactionUuid());
            data.put("amount", result.getAmount());
            data.put("pan", maskPAN(result.getPan()));
            data.put("originalRiskLevel", result.getOriginalRiskLevel());
            data.put("newRiskLevel", result.getNewRiskLevel());
            data.put("firedAIRules", formatFiredRules(result.getFiredAIRules()));
            data.put("status", result.getStatus());
            data.put("transactionDate", formatDate(result.getTransactionDate()));
            
            // Extract details if available
            if (result.getDetails() != null) {
                data.put("riskScore", result.getDetails().getRiskScore());
                data.put("fraudPercentage", result.getDetails().getFraudPercentage());
                data.put("actionStatus", result.getDetails().getActionStatus());
            } else {
                data.put("riskScore", 0.0);
                data.put("fraudPercentage", 0.0);
                data.put("actionStatus", "N/A");
            }
            
            return data;
        }).collect(Collectors.toList());
    }

    /**
     * Prepare report parameters
     */
    private Map<String, Object> prepareReportParameters(List<AIRuleTestResultDTO> testResults, String reportTitle) {
        Map<String, Object> parameters = new HashMap<>();
        
        parameters.put("reportTitle", reportTitle != null ? reportTitle : "AI Rule Testing & Validation Report");
        parameters.put("generatedDate", LocalDateTime.now().format(DATE_FORMATTER));
        parameters.put("totalTransactions", testResults.size());
        
        // Calculate summary statistics
        long flaggedTransactions = testResults.stream()
                .filter(result -> result.getFiredAIRules() != null && !result.getFiredAIRules().isEmpty())
                .count();
        parameters.put("flaggedTransactions", (int) flaggedTransactions);
        
        // Calculate average risk score
        double averageRiskScore = testResults.stream()
                .filter(result -> result.getDetails() != null && result.getDetails().getRiskScore() != null)
                .mapToDouble(result -> result.getDetails().getRiskScore())
                .average()
                .orElse(0.0);
        parameters.put("averageRiskScore", Math.round(averageRiskScore * 100.0) / 100.0);
        
        // Count risk levels
        Map<String, Long> riskCounts = testResults.stream()
                .filter(result -> result.getNewRiskLevel() != null)
                .collect(Collectors.groupingBy(
                        AIRuleTestResultDTO::getNewRiskLevel,
                        Collectors.counting()
                ));
        
        parameters.put("highRiskCount", riskCounts.getOrDefault("HIGH", 0L).intValue());
        parameters.put("mediumRiskCount", riskCounts.getOrDefault("MEDIUM", 0L).intValue());
        parameters.put("lowRiskCount", riskCounts.getOrDefault("LOW", 0L).intValue());
        
        return parameters;
    }

    /**
     * Mask PAN for security
     */
    private String maskPAN(String pan) {
        if (pan == null || pan.length() < 8) {
            return "****";
        }
        return pan.substring(0, 4) + "****" + pan.substring(pan.length() - 4);
    }

    /**
     * Format fired rules list
     */
    private String formatFiredRules(List<String> firedRules) {
        if (firedRules == null || firedRules.isEmpty()) {
            return "None";
        }
        return String.join(", ", firedRules);
    }

    /**
     * Format date for display
     */
    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DATE_FORMATTER);
    }

    /**
     * Generate detailed analysis report with charts and insights
     */
    public byte[] generateDetailedAnalysisReport(List<AIRuleTestResultDTO> testResults) {
        try {
            log.info("Starting JasperReports comprehensive analysis generation for {} test results", testResults.size());

            // Use the comprehensive template for the most detailed report
            return generateDetailedReportWithTemplate(COMPREHENSIVE_TEMPLATE, testResults, "Comprehensive AI Rule Analysis Report");
            
        } catch (Exception e) {
            log.warn("Comprehensive template failed, trying detailed template: {}", e.getMessage());
            try {
                // Fallback to detailed template
                return generateDetailedReportWithTemplate(DETAILED_TEMPLATE, testResults, "Detailed AI Rule Analysis Report");
            } catch (Exception fallbackException) {
                log.error("Both detailed templates failed. Comprehensive: {}, Detailed: {}", e.getMessage(), fallbackException.getMessage());
                throw new RuntimeException("Failed to generate detailed analysis using JasperReports", fallbackException);
            }
        }
    }

    /**
     * Generate report with specified detailed template
     */
    private byte[] generateDetailedReportWithTemplate(String templatePath, List<AIRuleTestResultDTO> testResults, String reportTitle) throws Exception {
        // Load and compile the template
        InputStream reportStream = new ClassPathResource(templatePath).getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Prepare detailed data source with all transaction information
        List<Map<String, Object>> reportData = prepareDetailedReportData(testResults);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        // Prepare parameters for detailed analysis
        Map<String, Object> parameters = prepareDetailedReportParameters(testResults, reportTitle);

        // Fill the report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export to PDF
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        log.info("JasperReports detailed generation completed successfully using template: {}. PDF size: {} bytes", templatePath, pdfBytes.length);
        return pdfBytes;
    }

    /**
     * Prepare detailed data for the report with all transaction information
     */
    private List<Map<String, Object>> prepareDetailedReportData(List<AIRuleTestResultDTO> testResults) {
        return testResults.stream().map(result -> {
            Map<String, Object> data = new HashMap<>();
            data.put("transactionId", result.getTransactionId());
            data.put("transactionUuid", result.getTransactionUuid());
            data.put("amount", result.getAmount());
            data.put("pan", result.getPan()); // Will be masked in template
            data.put("originalRiskLevel", result.getOriginalRiskLevel());
            data.put("newRiskLevel", result.getNewRiskLevel());
            data.put("firedAIRules", formatFiredRulesDetailed(result.getFiredAIRules()));
            data.put("status", result.getStatus());
            data.put("transactionDate", formatDate(result.getTransactionDate()));
            
            // Extract detailed information
            if (result.getDetails() != null) {
                data.put("riskScore", result.getDetails().getRiskScore());
                data.put("fraudPercentage", result.getDetails().getFraudPercentage());
                data.put("actionStatus", result.getDetails().getActionStatus());
                data.put("blockReason", result.getDetails().getBlockReason());
                data.put("allFiredRules", formatAllFiredRules(result.getDetails().getAllFiredRules()));
            } else {
                data.put("riskScore", 0.0);
                data.put("fraudPercentage", 0.0);
                data.put("actionStatus", "N/A");
                data.put("blockReason", "No details available");
                data.put("allFiredRules", "No additional rules");
            }
            
            return data;
        }).collect(Collectors.toList());
    }

    /**
     * Prepare parameters for detailed analysis report
     */
    private Map<String, Object> prepareDetailedReportParameters(List<AIRuleTestResultDTO> testResults, String reportTitle) {
        Map<String, Object> parameters = new HashMap<>();
        
        parameters.put("reportTitle", reportTitle);
        parameters.put("generatedDate", LocalDateTime.now().format(DATE_FORMATTER));
        parameters.put("totalTransactions", testResults.size());
        
        // Calculate summary statistics
        long flaggedTransactions = testResults.stream()
                .filter(result -> "FLAGGED".equals(result.getStatus()))
                .count();
        parameters.put("flaggedTransactions", (int) flaggedTransactions);
        
        // Calculate average risk score
        double averageRiskScore = testResults.stream()
                .filter(result -> result.getDetails() != null && result.getDetails().getRiskScore() != null)
                .mapToDouble(result -> result.getDetails().getRiskScore())
                .average()
                .orElse(0.0);
        parameters.put("averageRiskScore", Math.round(averageRiskScore * 100.0) / 100.0);
        
        // Count risk levels
        Map<String, Long> riskCounts = testResults.stream()
                .filter(result -> result.getNewRiskLevel() != null)
                .collect(Collectors.groupingBy(
                        AIRuleTestResultDTO::getNewRiskLevel,
                        Collectors.counting()
                ));
        
        parameters.put("highRiskCount", riskCounts.getOrDefault("HIGH", 0L).intValue());
        parameters.put("mediumRiskCount", riskCounts.getOrDefault("MEDIUM", 0L).intValue());
        parameters.put("lowRiskCount", riskCounts.getOrDefault("LOW", 0L).intValue());
        
        return parameters;
    }

    /**
     * Format fired rules with detailed information
     */
    private String formatFiredRulesDetailed(List<String> firedRules) {
        if (firedRules == null || firedRules.isEmpty()) {
            return "No rules fired";
        }
        
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < firedRules.size(); i++) {
            if (i > 0) formatted.append(" | ");
            formatted.append(firedRules.get(i));
        }
        
        return formatted.toString();
    }

    /**
     * Format all fired rules including additional details
     */
    private String formatAllFiredRules(List<String> allFiredRules) {
        if (allFiredRules == null || allFiredRules.isEmpty()) {
            return "No additional rules available";
        }
        
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < allFiredRules.size(); i++) {
            if (i > 0) formatted.append(", ");
            formatted.append(allFiredRules.get(i));
        }
        
        return formatted.toString();
    }

    /**
     * Generate executive summary report
     */
    public byte[] generateExecutiveSummaryReport(List<AIRuleTestResultDTO> testResults) {
        try {
            log.info("Starting JasperReports executive summary generation for {} test results", testResults.size());

            // Try the executive template first
            return generateReportWithTemplate(EXECUTIVE_TEMPLATE, testResults, "Executive Summary - AI Rule Testing");
            
        } catch (Exception e) {
            log.warn("Executive template failed, trying simple template: {}", e.getMessage());
            try {
                // Fallback to simple template
                return generateReportWithTemplate(SIMPLE_TEMPLATE, testResults, "AI Rule Testing Summary");
            } catch (Exception fallbackException) {
                log.error("Both templates failed. Executive: {}, Simple: {}", e.getMessage(), fallbackException.getMessage());
                throw new RuntimeException("Failed to generate executive summary using JasperReports", fallbackException);
            }
        }
    }

    /**
     * Generate report with specified template
     */
    private byte[] generateReportWithTemplate(String templatePath, List<AIRuleTestResultDTO> testResults, String reportTitle) throws Exception {
        // Load and compile the template
        InputStream reportStream = new ClassPathResource(templatePath).getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Prepare parameters
        Map<String, Object> parameters = prepareExecutiveSummaryParameters(testResults, reportTitle);

        // Use empty data source since this is a summary report
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.emptyList());

        // Fill the report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export to PDF
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        log.info("JasperReports generation completed successfully using template: {}. PDF size: {} bytes", templatePath, pdfBytes.length);
        return pdfBytes;
    }

    /**
     * Prepare parameters for executive summary report
     */
    private Map<String, Object> prepareExecutiveSummaryParameters(List<AIRuleTestResultDTO> testResults, String reportTitle) {
        Map<String, Object> parameters = new HashMap<>();
        
        parameters.put("reportTitle", reportTitle);
        parameters.put("generatedDate", LocalDateTime.now().format(DATE_FORMATTER));
        parameters.put("totalTransactions", testResults.size());
        
        // Calculate summary statistics
        long flaggedTransactions = testResults.stream()
                .filter(result -> result.getFiredAIRules() != null && !result.getFiredAIRules().isEmpty())
                .count();
        parameters.put("flaggedTransactions", (int) flaggedTransactions);
        
        // Calculate flag percentage
        double flaggedPercentage = testResults.size() > 0 ? 
            (flaggedTransactions * 100.0 / testResults.size()) : 0.0;
        parameters.put("flaggedPercentage", Math.round(flaggedPercentage * 100.0) / 100.0);
        
        // Calculate average risk score
        double averageRiskScore = testResults.stream()
                .filter(result -> result.getDetails() != null && result.getDetails().getRiskScore() != null)
                .mapToDouble(result -> result.getDetails().getRiskScore())
                .average()
                .orElse(0.0);
        parameters.put("averageRiskScore", Math.round(averageRiskScore * 100.0) / 100.0);
        
        // Count risk levels
        Map<String, Long> riskCounts = testResults.stream()
                .filter(result -> result.getNewRiskLevel() != null)
                .collect(Collectors.groupingBy(
                        AIRuleTestResultDTO::getNewRiskLevel,
                        Collectors.counting()
                ));
        
        parameters.put("highRiskCount", riskCounts.getOrDefault("HIGH", 0L).intValue());
        parameters.put("mediumRiskCount", riskCounts.getOrDefault("MEDIUM", 0L).intValue());
        parameters.put("lowRiskCount", riskCounts.getOrDefault("LOW", 0L).intValue());
        
        // Calculate financial metrics
        double totalAmount = testResults.stream()
                .filter(result -> result.getAmount() != null)
                .mapToDouble(AIRuleTestResultDTO::getAmount)
                .sum();
        parameters.put("totalAmount", totalAmount);
        
        double averageAmount = testResults.stream()
                .filter(result -> result.getAmount() != null)
                .mapToDouble(AIRuleTestResultDTO::getAmount)
                .average()
                .orElse(0.0);
        parameters.put("averageAmount", Math.round(averageAmount * 100.0) / 100.0);
        
        return parameters;
    }
}
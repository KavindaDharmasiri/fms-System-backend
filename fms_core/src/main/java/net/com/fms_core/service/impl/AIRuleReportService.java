package net.com.fms_core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.AIRuleTestResultDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIRuleReportService {

    @Autowired
    private AIRulePDFReportService pdfReportService;

    public byte[] generateDetailedExcelReport(List<AIRuleTestResultDTO> testResults, 
                                            String ruleGroupName, 
                                            LocalDateTime startDate, 
                                            LocalDateTime endDate) throws IOException {
        
        try (Workbook workbook = new XSSFWorkbook()) {
            
            // Create Summary Sheet
            createSummarySheet(workbook, testResults, ruleGroupName, startDate, endDate);
            
            // Create Detailed Results Sheet
            createDetailedResultsSheet(workbook, testResults);
            
            // Create Analytics Sheet
            createAnalyticsSheet(workbook, testResults);
            
            // Create Flagged Transactions Sheet
            createFlaggedTransactionsSheet(workbook, testResults);
            
            // Create Rule Performance Sheet
            createRulePerformanceSheet(workbook, testResults);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            log.error("Error generating Excel report", e);
            throw new IOException("Failed to generate Excel report", e);
        }
    }
    
    public byte[] generateComprehensivePDFReport(List<AIRuleTestResultDTO> testResults, 
                                               String ruleGroupName, 
                                               LocalDateTime startDate, 
                                               LocalDateTime endDate) throws IOException {
        return pdfReportService.generateComprehensiveTextReport(testResults, ruleGroupName, startDate, endDate);
    }
    
    private void createSummarySheet(Workbook workbook, List<AIRuleTestResultDTO> testResults, 
                                  String ruleGroupName, LocalDateTime startDate, LocalDateTime endDate) {
        Sheet sheet = workbook.createSheet("Executive Summary");
        
        // Create styles
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle titleStyle = createTitleStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        // Title
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("AI Rule Testing & Validation Report");
        titleCell.setCellStyle(titleStyle);
        
        rowNum++; // Empty row
        
        // Report Details
        createDataRow(sheet, rowNum++, "Rule Group:", ruleGroupName, headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Test Period:", 
            startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " to " + 
            endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Report Generated:", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), headerStyle, dataStyle);
        
        rowNum++; // Empty row
        
        // Key Metrics
        long totalTransactions = testResults.size();
        long flaggedTransactions = testResults.stream().filter(r -> "FLAGGED".equals(r.getStatus())).count();
        long passedTransactions = testResults.stream().filter(r -> "PASSED".equals(r.getStatus())).count();
        double flaggedPercentage = totalTransactions > 0 ? (flaggedTransactions * 100.0 / totalTransactions) : 0;
        
        createDataRow(sheet, rowNum++, "Total Transactions Tested:", String.valueOf(totalTransactions), headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Flagged Transactions:", String.valueOf(flaggedTransactions), headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Passed Transactions:", String.valueOf(passedTransactions), headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Flag Rate:", String.format("%.2f%%", flaggedPercentage), headerStyle, dataStyle);
        
        rowNum++; // Empty row
        
        // Risk Level Distribution
        Map<String, Long> riskDistribution = testResults.stream()
            .collect(Collectors.groupingBy(r -> r.getNewRiskLevel() != null ? r.getNewRiskLevel() : "UNKNOWN", 
                    Collectors.counting()));
        
        Row riskHeaderRow = sheet.createRow(rowNum++);
        riskHeaderRow.createCell(0).setCellValue("Risk Level Distribution:");
        riskHeaderRow.getCell(0).setCellStyle(headerStyle);
        
        for (Map.Entry<String, Long> entry : riskDistribution.entrySet()) {
            createDataRow(sheet, rowNum++, "  " + entry.getKey() + ":", String.valueOf(entry.getValue()), headerStyle, dataStyle);
        }
        
        // Auto-size columns
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createDetailedResultsSheet(Workbook workbook, List<AIRuleTestResultDTO> testResults) {
        Sheet sheet = workbook.createSheet("Detailed Results");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Transaction ID", "Amount", "PAN", "Original Risk", "New Risk", 
                           "Status", "Fired AI Rules", "Risk Score", "Fraud %", "Block Reason", "Transaction Date"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (AIRuleTestResultDTO result : testResults) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(result.getTransactionId() != null ? result.getTransactionId().toString() : "");
            row.createCell(1).setCellValue(result.getAmount() != null ? result.getAmount() : 0);
            row.createCell(2).setCellValue(result.getPan() != null ? result.getPan() : "");
            row.createCell(3).setCellValue(result.getOriginalRiskLevel() != null ? result.getOriginalRiskLevel() : "");
            row.createCell(4).setCellValue(result.getNewRiskLevel() != null ? result.getNewRiskLevel() : "");
            row.createCell(5).setCellValue(result.getStatus() != null ? result.getStatus() : "");
            row.createCell(6).setCellValue(result.getFiredAIRules() != null ? String.join(", ", result.getFiredAIRules()) : "");
            
            if (result.getDetails() != null) {
                row.createCell(7).setCellValue(result.getDetails().getRiskScore() != null ? result.getDetails().getRiskScore() : 0);
                row.createCell(8).setCellValue(result.getDetails().getFraudPercentage() != null ? result.getDetails().getFraudPercentage() : 0);
                row.createCell(9).setCellValue(result.getDetails().getBlockReason() != null ? result.getDetails().getBlockReason() : "");
            } else {
                row.createCell(7).setCellValue(0);
                row.createCell(8).setCellValue(0);
                row.createCell(9).setCellValue("");
            }
            
            row.createCell(10).setCellValue(result.getTransactionDate() != null ? 
                result.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            
            // Apply data style to all cells
            for (int i = 0; i < headers.length; i++) {
                if (row.getCell(i) != null) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createAnalyticsSheet(Workbook workbook, List<AIRuleTestResultDTO> testResults) {
        Sheet sheet = workbook.createSheet("Analytics");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        // Amount-based Analysis
        Row titleRow = sheet.createRow(rowNum++);
        titleRow.createCell(0).setCellValue("Transaction Amount Analysis");
        titleRow.getCell(0).setCellStyle(headerStyle);
        
        rowNum++; // Empty row
        
        // Calculate amount statistics
        double totalAmount = testResults.stream().mapToDouble(r -> r.getAmount() != null ? r.getAmount() : 0).sum();
        double avgAmount = testResults.stream().mapToDouble(r -> r.getAmount() != null ? r.getAmount() : 0).average().orElse(0);
        double maxAmount = testResults.stream().mapToDouble(r -> r.getAmount() != null ? r.getAmount() : 0).max().orElse(0);
        double minAmount = testResults.stream().mapToDouble(r -> r.getAmount() != null ? r.getAmount() : 0).min().orElse(0);
        
        createDataRow(sheet, rowNum++, "Total Transaction Amount:", String.format("$%.2f", totalAmount), headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Average Transaction Amount:", String.format("$%.2f", avgAmount), headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Maximum Transaction Amount:", String.format("$%.2f", maxAmount), headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Minimum Transaction Amount:", String.format("$%.2f", minAmount), headerStyle, dataStyle);
        
        rowNum++; // Empty row
        
        // Risk Level Changes
        Row riskTitleRow = sheet.createRow(rowNum++);
        riskTitleRow.createCell(0).setCellValue("Risk Level Changes");
        riskTitleRow.getCell(0).setCellStyle(headerStyle);
        
        rowNum++; // Empty row
        
        long riskIncreased = testResults.stream()
            .filter(r -> isRiskIncreased(r.getOriginalRiskLevel(), r.getNewRiskLevel()))
            .count();
        long riskDecreased = testResults.stream()
            .filter(r -> isRiskDecreased(r.getOriginalRiskLevel(), r.getNewRiskLevel()))
            .count();
        long riskUnchanged = testResults.stream()
            .filter(r -> isRiskUnchanged(r.getOriginalRiskLevel(), r.getNewRiskLevel()))
            .count();
        
        createDataRow(sheet, rowNum++, "Risk Level Increased:", String.valueOf(riskIncreased), headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Risk Level Decreased:", String.valueOf(riskDecreased), headerStyle, dataStyle);
        createDataRow(sheet, rowNum++, "Risk Level Unchanged:", String.valueOf(riskUnchanged), headerStyle, dataStyle);
        
        // Auto-size columns
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createFlaggedTransactionsSheet(Workbook workbook, List<AIRuleTestResultDTO> testResults) {
        Sheet sheet = workbook.createSheet("Flagged Transactions");
        
        List<AIRuleTestResultDTO> flaggedTransactions = testResults.stream()
            .filter(r -> "FLAGGED".equals(r.getStatus()))
            .collect(Collectors.toList());
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        CellStyle alertStyle = createAlertStyle(workbook);
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Transaction ID", "Amount", "PAN", "Risk Level", "Fired Rules", 
                           "Risk Score", "Fraud %", "Block Reason", "Action Required"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        for (AIRuleTestResultDTO result : flaggedTransactions) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(result.getTransactionId() != null ? result.getTransactionId().toString() : "");
            row.createCell(1).setCellValue(result.getAmount() != null ? result.getAmount() : 0);
            row.createCell(2).setCellValue(result.getPan() != null ? result.getPan() : "");
            row.createCell(3).setCellValue(result.getNewRiskLevel() != null ? result.getNewRiskLevel() : "");
            row.createCell(4).setCellValue(result.getFiredAIRules() != null ? String.join(", ", result.getFiredAIRules()) : "");
            
            if (result.getDetails() != null) {
                row.createCell(5).setCellValue(result.getDetails().getRiskScore() != null ? result.getDetails().getRiskScore() : 0);
                row.createCell(6).setCellValue(result.getDetails().getFraudPercentage() != null ? result.getDetails().getFraudPercentage() : 0);
                row.createCell(7).setCellValue(result.getDetails().getBlockReason() != null ? result.getDetails().getBlockReason() : "");
            }
            
            // Action Required
            String actionRequired = determineActionRequired(result);
            Cell actionCell = row.createCell(8);
            actionCell.setCellValue(actionRequired);
            if ("IMMEDIATE REVIEW".equals(actionRequired)) {
                actionCell.setCellStyle(alertStyle);
            } else {
                actionCell.setCellStyle(dataStyle);
            }
            
            // Apply styles
            for (int i = 0; i < headers.length - 1; i++) {
                if (row.getCell(i) != null) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void createRulePerformanceSheet(Workbook workbook, List<AIRuleTestResultDTO> testResults) {
        Sheet sheet = workbook.createSheet("Rule Performance");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        // Collect rule statistics
        Map<String, Long> ruleFireCount = testResults.stream()
            .filter(r -> r.getFiredAIRules() != null)
            .flatMap(r -> r.getFiredAIRules().stream())
            .collect(Collectors.groupingBy(rule -> rule, Collectors.counting()));
        
        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Rule Name", "Times Fired", "Effectiveness %", "Impact Level"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int rowNum = 1;
        long totalTransactions = testResults.size();
        
        for (Map.Entry<String, Long> entry : ruleFireCount.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            
            String ruleName = entry.getKey();
            Long fireCount = entry.getValue();
            double effectiveness = totalTransactions > 0 ? (fireCount * 100.0 / totalTransactions) : 0;
            String impactLevel = determineImpactLevel(effectiveness);
            
            row.createCell(0).setCellValue(ruleName);
            row.createCell(1).setCellValue(fireCount);
            row.createCell(2).setCellValue(String.format("%.2f%%", effectiveness));
            row.createCell(3).setCellValue(impactLevel);
            
            // Apply styles
            for (int i = 0; i < headers.length; i++) {
                if (row.getCell(i) != null) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    // Helper methods for styling
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createAlertStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    // Helper methods
    private void createDataRow(Sheet sheet, int rowNum, String label, String value, CellStyle labelStyle, CellStyle valueStyle) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);
        
        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(valueStyle);
    }
    
    private boolean isRiskIncreased(String original, String newRisk) {
        int originalLevel = getRiskLevel(original);
        int newLevel = getRiskLevel(newRisk);
        return newLevel > originalLevel;
    }
    
    private boolean isRiskDecreased(String original, String newRisk) {
        int originalLevel = getRiskLevel(original);
        int newLevel = getRiskLevel(newRisk);
        return newLevel < originalLevel;
    }
    
    private boolean isRiskUnchanged(String original, String newRisk) {
        return getRiskLevel(original) == getRiskLevel(newRisk);
    }
    
    private int getRiskLevel(String risk) {
        if (risk == null) return 0;
        switch (risk.toUpperCase()) {
            case "LOW": return 1;
            case "MID": case "MEDIUM": return 2;
            case "HIGH": return 3;
            default: return 0;
        }
    }
    
    private String determineActionRequired(AIRuleTestResultDTO result) {
        if (result.getDetails() != null) {
            Double riskScore = result.getDetails().getRiskScore();
            Double fraudPercentage = result.getDetails().getFraudPercentage();
            
            if ((riskScore != null && riskScore > 80) || (fraudPercentage != null && fraudPercentage > 70)) {
                return "IMMEDIATE REVIEW";
            } else if ((riskScore != null && riskScore > 50) || (fraudPercentage != null && fraudPercentage > 40)) {
                return "MANUAL REVIEW";
            }
        }
        return "MONITOR";
    }
    
    private String determineImpactLevel(double effectiveness) {
        if (effectiveness > 20) return "HIGH";
        else if (effectiveness > 10) return "MEDIUM";
        else if (effectiveness > 5) return "LOW";
        else return "MINIMAL";
    }
}
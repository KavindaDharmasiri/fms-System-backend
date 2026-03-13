package net.com.fms_core.util;

import net.com.fms_core.dto.AIRuleTestResultDTO;
import net.com.fms_core.service.impl.AIRuleJasperReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Example utility class demonstrating JasperReports usage
 * This class shows how to create sample data and generate reports
 */
@Component
public class JasperReportExample {

    @Autowired
    private AIRuleJasperReportService jasperReportService;

    /**
     * Generate sample test data for demonstration
     */
    public List<AIRuleTestResultDTO> createSampleTestData() {
        // High-risk transaction
        AIRuleTestResultDTO.AIRuleTestDetailDTO highRiskDetails = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        highRiskDetails.setRiskScore(92.5);
        highRiskDetails.setFraudPercentage(88.3);
        highRiskDetails.setActionStatus("BLOCKED");
        highRiskDetails.setBlockReason("Multiple fraud indicators detected");

        AIRuleTestResultDTO highRiskTransaction = new AIRuleTestResultDTO();
        highRiskTransaction.setTransactionId(1001L);
        highRiskTransaction.setTransactionUuid("TXN-HR-001");
        highRiskTransaction.setAmount(5000.00);
        highRiskTransaction.setPan("4532123456789012");
        highRiskTransaction.setOriginalRiskLevel("MEDIUM");
        highRiskTransaction.setNewRiskLevel("HIGH");
        highRiskTransaction.setFiredAIRules(Arrays.asList("VELOCITY_CHECK", "AMOUNT_THRESHOLD", "LOCATION_ANOMALY", "TIME_PATTERN"));
        highRiskTransaction.setStatus("FLAGGED");
        highRiskTransaction.setTransactionDate(LocalDateTime.now().minusHours(1));
        highRiskTransaction.setDetails(highRiskDetails);

        // Medium-risk transaction
        AIRuleTestResultDTO.AIRuleTestDetailDTO mediumRiskDetails = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        mediumRiskDetails.setRiskScore(65.2);
        mediumRiskDetails.setFraudPercentage(55.7);
        mediumRiskDetails.setActionStatus("REVIEW");
        mediumRiskDetails.setBlockReason("Unusual spending pattern");

        AIRuleTestResultDTO mediumRiskTransaction = new AIRuleTestResultDTO();
        mediumRiskTransaction.setTransactionId(1002L);
        mediumRiskTransaction.setTransactionUuid("TXN-MR-002");
        mediumRiskTransaction.setAmount(1200.00);
        mediumRiskTransaction.setPan("5555444433332222");
        mediumRiskTransaction.setOriginalRiskLevel("LOW");
        mediumRiskTransaction.setNewRiskLevel("MEDIUM");
        mediumRiskTransaction.setFiredAIRules(Arrays.asList("MERCHANT_CATEGORY", "SPENDING_PATTERN"));
        mediumRiskTransaction.setStatus("FLAGGED");
        mediumRiskTransaction.setTransactionDate(LocalDateTime.now().minusMinutes(45));
        mediumRiskTransaction.setDetails(mediumRiskDetails);

        // Low-risk transaction (approved)
        AIRuleTestResultDTO.AIRuleTestDetailDTO lowRiskDetails = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        lowRiskDetails.setRiskScore(15.8);
        lowRiskDetails.setFraudPercentage(8.2);
        lowRiskDetails.setActionStatus("APPROVED");

        AIRuleTestResultDTO lowRiskTransaction = new AIRuleTestResultDTO();
        lowRiskTransaction.setTransactionId(1003L);
        lowRiskTransaction.setTransactionUuid("TXN-LR-003");
        lowRiskTransaction.setAmount(89.99);
        lowRiskTransaction.setPan("4111111111111111");
        lowRiskTransaction.setOriginalRiskLevel("LOW");
        lowRiskTransaction.setNewRiskLevel("LOW");
        lowRiskTransaction.setFiredAIRules(Arrays.asList());
        lowRiskTransaction.setStatus("PASSED");
        lowRiskTransaction.setTransactionDate(LocalDateTime.now().minusMinutes(15));
        lowRiskTransaction.setDetails(lowRiskDetails);

        // Another approved transaction
        AIRuleTestResultDTO.AIRuleTestDetailDTO approvedDetails = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        approvedDetails.setRiskScore(22.1);
        approvedDetails.setFraudPercentage(12.5);
        approvedDetails.setActionStatus("APPROVED");

        AIRuleTestResultDTO approvedTransaction = new AIRuleTestResultDTO();
        approvedTransaction.setTransactionId(1004L);
        approvedTransaction.setTransactionUuid("TXN-AP-004");
        approvedTransaction.setAmount(245.50);
        approvedTransaction.setPan("3782822463100005");
        approvedTransaction.setOriginalRiskLevel("LOW");
        approvedTransaction.setNewRiskLevel("LOW");
        approvedTransaction.setFiredAIRules(Arrays.asList());
        approvedTransaction.setStatus("PASSED");
        approvedTransaction.setTransactionDate(LocalDateTime.now().minusMinutes(5));
        approvedTransaction.setDetails(approvedDetails);

        // Flagged transaction with multiple rules
        AIRuleTestResultDTO.AIRuleTestDetailDTO flaggedDetails = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        flaggedDetails.setRiskScore(78.9);
        flaggedDetails.setFraudPercentage(72.3);
        flaggedDetails.setActionStatus("BLOCKED");
        flaggedDetails.setBlockReason("Suspicious merchant and location combination");

        AIRuleTestResultDTO flaggedTransaction = new AIRuleTestResultDTO();
        flaggedTransaction.setTransactionId(1005L);
        flaggedTransaction.setTransactionUuid("TXN-FL-005");
        flaggedTransaction.setAmount(2500.00);
        flaggedTransaction.setPan("6011111111111117");
        flaggedTransaction.setOriginalRiskLevel("LOW");
        flaggedTransaction.setNewRiskLevel("HIGH");
        flaggedTransaction.setFiredAIRules(Arrays.asList("MERCHANT_RISK", "LOCATION_CHECK", "DEVICE_FINGERPRINT"));
        flaggedTransaction.setStatus("FLAGGED");
        flaggedTransaction.setTransactionDate(LocalDateTime.now().minusHours(2));
        flaggedTransaction.setDetails(flaggedDetails);

        return Arrays.asList(
            highRiskTransaction, 
            mediumRiskTransaction, 
            lowRiskTransaction, 
            approvedTransaction, 
            flaggedTransaction
        );
    }

    /**
     * Example method to generate all types of reports
     */
    public void generateExampleReports() {
        List<AIRuleTestResultDTO> sampleData = createSampleTestData();

        try {
            // Generate standard detailed report
            byte[] detailedReport = jasperReportService.generateJasperPDFReport(
                sampleData, "Sample AI Rule Test - Production Environment"
            );
            System.out.println("Generated detailed report: " + detailedReport.length + " bytes");

            // Generate comprehensive detailed analysis (NEW - Most detailed)
            byte[] comprehensiveReport = jasperReportService.generateDetailedAnalysisReport(sampleData);
            System.out.println("Generated comprehensive detailed analysis: " + comprehensiveReport.length + " bytes");

            // Generate executive summary
            byte[] executiveSummary = jasperReportService.generateExecutiveSummaryReport(sampleData);
            System.out.println("Generated executive summary: " + executiveSummary.length + " bytes");

        } catch (Exception e) {
            System.err.println("Error generating example reports: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get sample request data for API testing
     */
    public String getSampleRequestJson() {
        return """
            {
              "testResults": [
                {
                  "transactionId": 1001,
                  "transactionUuid": "TXN-HR-001",
                  "amount": 5000.00,
                  "pan": "4532123456789012",
                  "originalRiskLevel": "MEDIUM",
                  "newRiskLevel": "HIGH",
                  "firedAIRules": ["VELOCITY_CHECK", "AMOUNT_THRESHOLD", "LOCATION_ANOMALY"],
                  "status": "FLAGGED",
                  "transactionDate": "2026-03-13T10:30:00",
                  "details": {
                    "riskScore": 92.5,
                    "fraudPercentage": 88.3,
                    "actionStatus": "BLOCKED",
                    "blockReason": "Multiple fraud indicators detected"
                  }
                },
                {
                  "transactionId": 1002,
                  "transactionUuid": "TXN-LR-002",
                  "amount": 89.99,
                  "pan": "4111111111111111",
                  "originalRiskLevel": "LOW",
                  "newRiskLevel": "LOW",
                  "firedAIRules": [],
                  "status": "PASSED",
                  "transactionDate": "2026-03-13T11:15:00",
                  "details": {
                    "riskScore": 15.8,
                    "fraudPercentage": 8.2,
                    "actionStatus": "APPROVED"
                  }
                }
              ],
              "ruleGroupName": "Production Rules v1.2",
              "startDate": "2026-03-13T09:00:00",
              "endDate": "2026-03-13T17:00:00"
            }
            """;
    }
}
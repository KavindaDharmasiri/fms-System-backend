package net.com.fms_core.service;

import net.com.fms_core.dto.AIRuleTestResultDTO;
import net.com.fms_core.service.impl.AIRulePDFReportService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AIRulePDFReportServiceTest {

    @Test
    public void testPDFReportGeneration() {
        AIRulePDFReportService pdfReportService = new AIRulePDFReportService();
        
        // Create sample test data
        List<AIRuleTestResultDTO> testResults = createSampleTestResults();
        
        try {
            byte[] pdfData = pdfReportService.generateComprehensiveTextReport(
                testResults, 
                "Test Rule Group", 
                LocalDateTime.now().minusDays(1), 
                LocalDateTime.now()
            );
            
            assertNotNull(pdfData);
            assertTrue(pdfData.length > 0);
            
            // Verify PDF header (PDF files start with %PDF)
            String pdfHeader = new String(Arrays.copyOfRange(pdfData, 0, 4));
            assertEquals("%PDF", pdfHeader);
            
        } catch (Exception e) {
            fail("PDF generation should not throw exception: " + e.getMessage());
        }
    }
    
    private List<AIRuleTestResultDTO> createSampleTestResults() {
        AIRuleTestResultDTO result1 = new AIRuleTestResultDTO();
        result1.setTransactionId(12345L);
        result1.setAmount(1500.00);
        result1.setPan("****1234");
        result1.setOriginalRiskLevel("LOW");
        result1.setNewRiskLevel("HIGH");
        result1.setFiredAIRules(Arrays.asList("Velocity Rule", "Amount Rule"));
        result1.setStatus("FLAGGED");
        result1.setTransactionDate(LocalDateTime.now());
        
        AIRuleTestResultDTO.AIRuleTestDetailDTO details1 = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        details1.setRiskScore(85.5);
        details1.setFraudPercentage(92.3);
        details1.setBlockReason("High velocity and unusual amount");
        details1.setActionStatus("BLOCKED");
        result1.setDetails(details1);
        
        AIRuleTestResultDTO result2 = new AIRuleTestResultDTO();
        result2.setTransactionId(12346L);
        result2.setAmount(50.00);
        result2.setPan("****5678");
        result2.setOriginalRiskLevel("LOW");
        result2.setNewRiskLevel("LOW");
        result2.setStatus("PASSED");
        result2.setTransactionDate(LocalDateTime.now());
        
        AIRuleTestResultDTO.AIRuleTestDetailDTO details2 = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        details2.setRiskScore(15.2);
        details2.setFraudPercentage(8.1);
        details2.setActionStatus("APPROVED");
        result2.setDetails(details2);
        
        return Arrays.asList(result1, result2);
    }
}
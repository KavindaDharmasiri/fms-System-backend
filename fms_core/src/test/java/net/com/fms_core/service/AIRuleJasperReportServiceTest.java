package net.com.fms_core.service;

import net.com.fms_core.dto.AIRuleTestResultDTO;
import net.com.fms_core.service.impl.AIRuleJasperReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AIRuleJasperReportServiceTest {

    @InjectMocks
    private AIRuleJasperReportService jasperReportService;

    private List<AIRuleTestResultDTO> testResults;

    @BeforeEach
    void setUp() {
        // Create sample test data
        AIRuleTestResultDTO.AIRuleTestDetailDTO details1 = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        details1.setRiskScore(85.5);
        details1.setFraudPercentage(75.2);
        details1.setActionStatus("BLOCKED");
        details1.setBlockReason("High risk transaction");

        AIRuleTestResultDTO result1 = new AIRuleTestResultDTO();
        result1.setTransactionId(1001L);
        result1.setTransactionUuid("uuid-1001");
        result1.setAmount(1500.00);
        result1.setPan("1234567890123456");
        result1.setOriginalRiskLevel("MEDIUM");
        result1.setNewRiskLevel("HIGH");
        result1.setFiredAIRules(Arrays.asList("VELOCITY_CHECK", "AMOUNT_THRESHOLD"));
        result1.setStatus("FLAGGED");
        result1.setTransactionDate(LocalDateTime.now().minusHours(2));
        result1.setDetails(details1);

        AIRuleTestResultDTO.AIRuleTestDetailDTO details2 = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        details2.setRiskScore(25.3);
        details2.setFraudPercentage(15.8);
        details2.setActionStatus("APPROVED");

        AIRuleTestResultDTO result2 = new AIRuleTestResultDTO();
        result2.setTransactionId(1002L);
        result2.setTransactionUuid("uuid-1002");
        result2.setAmount(250.00);
        result2.setPan("9876543210987654");
        result2.setOriginalRiskLevel("LOW");
        result2.setNewRiskLevel("LOW");
        result2.setFiredAIRules(Arrays.asList());
        result2.setStatus("PASSED");
        result2.setTransactionDate(LocalDateTime.now().minusHours(1));
        result2.setDetails(details2);

        AIRuleTestResultDTO.AIRuleTestDetailDTO details3 = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
        details3.setRiskScore(55.7);
        details3.setFraudPercentage(45.2);
        details3.setActionStatus("REVIEW");
        details3.setBlockReason("Medium risk - manual review required");

        AIRuleTestResultDTO result3 = new AIRuleTestResultDTO();
        result3.setTransactionId(1003L);
        result3.setTransactionUuid("uuid-1003");
        result3.setAmount(750.00);
        result3.setPan("5555444433332222");
        result3.setOriginalRiskLevel("LOW");
        result3.setNewRiskLevel("MEDIUM");
        result3.setFiredAIRules(Arrays.asList("LOCATION_CHECK"));
        result3.setStatus("FLAGGED");
        result3.setTransactionDate(LocalDateTime.now().minusMinutes(30));
        result3.setDetails(details3);

        testResults = Arrays.asList(result1, result2, result3);
    }

    @Test
    void testGenerateJasperPDFReport() {
        // Test basic JasperReports PDF generation
        byte[] pdfBytes = jasperReportService.generateJasperPDFReport(testResults, "Test Rule Group");
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Check PDF header (PDF files start with %PDF)
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateDetailedAnalysisReport() {
        // Test detailed analysis report generation
        byte[] pdfBytes = jasperReportService.generateDetailedAnalysisReport(testResults);
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Check PDF header
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateExecutiveSummaryReport() {
        // Test executive summary report generation
        byte[] pdfBytes = jasperReportService.generateExecutiveSummaryReport(testResults);
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Check PDF header
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateReportWithEmptyData() {
        // Test report generation with empty data
        List<AIRuleTestResultDTO> emptyResults = Arrays.asList();
        
        byte[] pdfBytes = jasperReportService.generateJasperPDFReport(emptyResults, "Empty Test");
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Check PDF header
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateReportWithNullTitle() {
        // Test report generation with null title
        byte[] pdfBytes = jasperReportService.generateJasperPDFReport(testResults, null);
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Check PDF header
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }
}
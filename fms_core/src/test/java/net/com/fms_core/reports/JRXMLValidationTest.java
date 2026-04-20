package net.com.fms_core.reports;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to validate JRXML template files
 */
class JRXMLValidationTest {

    @Test
    void testExecutiveSummaryTemplateIsValid() {
        try {
            ClassPathResource resource = new ClassPathResource("reports/ai_rule_executive_summary.jrxml");
            InputStream inputStream = resource.getInputStream();
            
            // This will throw an exception if the JRXML is invalid
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            
            assertNotNull(jasperReport);
            assertNotNull(jasperReport.getName());
            
        } catch (Exception e) {
            fail("Executive summary JRXML template is invalid: " + e.getMessage());
        }
    }

    @Test
    void testSimpleSummaryTemplateIsValid() {
        try {
            ClassPathResource resource = new ClassPathResource("reports/ai_rule_simple_summary.jrxml");
            InputStream inputStream = resource.getInputStream();
            
            // This will throw an exception if the JRXML is invalid
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            
            assertNotNull(jasperReport);
            assertNotNull(jasperReport.getName());
            
        } catch (Exception e) {
            fail("Simple summary JRXML template is invalid: " + e.getMessage());
        }
    }

    @Test
    void testComprehensiveAnalysisTemplateIsValid() {
        try {
            ClassPathResource resource = new ClassPathResource("reports/ai_rule_comprehensive_analysis.jrxml");
            InputStream inputStream = resource.getInputStream();
            
            // This will throw an exception if the JRXML is invalid
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            
            assertNotNull(jasperReport);
            assertNotNull(jasperReport.getName());
            
        } catch (Exception e) {
            fail("Comprehensive analysis JRXML template is invalid: " + e.getMessage());
        }
    }

    @Test
    void testDetailedAnalysisTemplateIsValid() {
        try {
            ClassPathResource resource = new ClassPathResource("reports/ai_rule_detailed_analysis.jrxml");
            InputStream inputStream = resource.getInputStream();
            
            // This will throw an exception if the JRXML is invalid
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            
            assertNotNull(jasperReport);
            assertNotNull(jasperReport.getName());
            
        } catch (Exception e) {
            fail("Detailed analysis JRXML template is invalid: " + e.getMessage());
        }
    }

    @Test
    void testTestReportTemplateIsValid() {
        try {
            ClassPathResource resource = new ClassPathResource("reports/ai_rule_test_report.jrxml");
            InputStream inputStream = resource.getInputStream();
            
            // This will throw an exception if the JRXML is invalid
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            
            assertNotNull(jasperReport);
            assertNotNull(jasperReport.getName());
            
        } catch (Exception e) {
            fail("Test report JRXML template is invalid: " + e.getMessage());
        }
    }
}
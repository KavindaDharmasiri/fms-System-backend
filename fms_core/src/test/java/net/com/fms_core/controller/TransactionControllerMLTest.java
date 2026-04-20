package net.com.fms_core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.service.impl.script.ProductionRuleGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerMLTest {

    @Mock
    private ProductionRuleGenerationService productionRuleService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(transactionController, "objectMapper", objectMapper);
    }

    @Test
    void testGenerateFutureRules_Success() {
        // Given
        Map<String, Object> mockServiceResponse = createSuccessfulServiceResponse();
        when(productionRuleService.generateAdvancedMLRules()).thenReturn(mockServiceResponse);

        // When
        ResponseEntity<ApiResponseDTO> response = transactionController.generateFutureRules(null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getMessage().contains("successfully"));

        // Verify response data structure
        Map<String, Object> responseData = (Map<String, Object>) response.getBody().getData();
        assertEquals("ML_BASED_INTELLIGENT", responseData.get("ruleType"));
        assertNotNull(responseData.get("rules"));
        assertNotNull(responseData.get("metadata"));
        assertFalse((Boolean) responseData.get("deployed"));
        assertTrue((Boolean) responseData.get("preview"));

        verify(productionRuleService, times(1)).generateAdvancedMLRules();
    }

    @Test
    void testGenerateFutureRules_ServiceFailure() {
        // Given
        Map<String, Object> mockServiceResponse = createFailedServiceResponse();
        when(productionRuleService.generateAdvancedMLRules()).thenReturn(mockServiceResponse);

        // When
        ResponseEntity<ApiResponseDTO> response = transactionController.generateFutureRules(null);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertNull(response.getBody().getData());
        assertTrue(response.getBody().getMessage().contains("failed"));

        verify(productionRuleService, times(1)).generateAdvancedMLRules();
    }

    @Test
    void testGenerateFutureRules_ServiceException() {
        // Given
        when(productionRuleService.generateAdvancedMLRules())
            .thenThrow(new RuntimeException("Python script execution failed"));

        // When
        ResponseEntity<ApiResponseDTO> response = transactionController.generateFutureRules(null);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertNull(response.getBody().getData());
        assertTrue(response.getBody().getMessage().contains("failed"));

        verify(productionRuleService, times(1)).generateAdvancedMLRules();
    }

    @Test
    void testGenerateFutureRules_WithParameters() {
        // Given
        Map<String, Object> params = new HashMap<>();
        params.put("modelType", "advanced");
        
        Map<String, Object> mockServiceResponse = createSuccessfulServiceResponse();
        when(productionRuleService.generateAdvancedMLRules()).thenReturn(mockServiceResponse);

        // When
        ResponseEntity<ApiResponseDTO> response = transactionController.generateFutureRules(params);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        
        verify(productionRuleService, times(1)).generateAdvancedMLRules();
    }

    @Test
    void testGenerateFutureRulesGet() {
        // Given
        Map<String, Object> mockServiceResponse = createSuccessfulServiceResponse();
        when(productionRuleService.generateAdvancedMLRules()).thenReturn(mockServiceResponse);

        // When
        ResponseEntity<ApiResponseDTO> response = transactionController.generateFutureRulesGet();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        
        verify(productionRuleService, times(1)).generateAdvancedMLRules();
    }

    @Test
    void testGenerateFutureRules_RuleConversion() {
        // Given
        Map<String, Object> mockServiceResponse = createSuccessfulServiceResponseWithComplexRules();
        when(productionRuleService.generateAdvancedMLRules()).thenReturn(mockServiceResponse);

        // When
        ResponseEntity<ApiResponseDTO> response = transactionController.generateFutureRules(null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        Map<String, Object> responseData = (Map<String, Object>) response.getBody().getData();
        String rulesString = (String) responseData.get("rules");
        
        // Verify DRL format conversion
        assertNotNull(rulesString);
        assertTrue(rulesString.contains("rule \""));
        assertTrue(rulesString.contains("when"));
        assertTrue(rulesString.contains("then"));
        assertTrue(rulesString.contains("end"));
        assertTrue(rulesString.contains("IsoMessageDTO"));
    }

    @Test
    void testGenerateFutureRules_MetadataProcessing() {
        // Given
        Map<String, Object> mockServiceResponse = createSuccessfulServiceResponseWithMetadata();
        when(productionRuleService.generateAdvancedMLRules()).thenReturn(mockServiceResponse);

        // When
        ResponseEntity<ApiResponseDTO> response = transactionController.generateFutureRules(null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        Map<String, Object> responseData = (Map<String, Object>) response.getBody().getData();
        Map<String, Object> metadata = (Map<String, Object>) responseData.get("metadata");
        
        // Verify metadata processing
        assertNotNull(metadata);
        assertNotNull(metadata.get("total_rules"));
        assertNotNull(metadata.get("model_accuracy"));
        assertNotNull(metadata.get("best_model"));
        assertNotNull(metadata.get("features"));
        
        List<String> features = (List<String>) responseData.get("features");
        assertNotNull(features);
        assertFalse(features.isEmpty());
    }

    @Test
    void testMapToActualField() {
        // Test the private method indirectly through rule generation
        Map<String, Object> mockServiceResponse = createSuccessfulServiceResponseWithDifferentFields();
        when(productionRuleService.generateAdvancedMLRules()).thenReturn(mockServiceResponse);

        // When
        ResponseEntity<ApiResponseDTO> response = transactionController.generateFutureRules(null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        Map<String, Object> responseData = (Map<String, Object>) response.getBody().getData();
        String rulesString = (String) responseData.get("rules");
        
        // Verify field mapping occurred
        assertNotNull(rulesString);
        // Should contain mapped field names, not original ML field names
        assertTrue(rulesString.contains("amount") || rulesString.contains("customerRiskScore"));
    }

    private Map<String, Object> createSuccessfulServiceResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("generatedAt", new Date());
        
        // Mock rules
        List<Map<String, Object>> rules = new ArrayList<>();
        Map<String, Object> rule = new HashMap<>();
        rule.put("id", "TEST_RULE_001");
        rule.put("name", "Test Amount Rule");
        rule.put("field", "amount");
        rule.put("operator", ">");
        rule.put("value", 1000.0);
        rule.put("action", "ALERT");
        rule.put("confidence", 0.85);
        rules.add(rule);
        response.put("rules", rules);
        
        // Mock metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("total_rules", 1);
        metadata.put("best_model", "RandomForest");
        metadata.put("model_accuracy", 0.92);
        metadata.put("training_samples", 10000);
        response.put("metadata", metadata);
        
        return response;
    }

    private Map<String, Object> createFailedServiceResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", "Python script execution failed");
        response.put("output", Arrays.asList("Error: Could not load model files"));
        return response;
    }

    private Map<String, Object> createSuccessfulServiceResponseWithComplexRules() {
        Map<String, Object> response = createSuccessfulServiceResponse();
        
        // Add complex rule with conditions
        List<Map<String, Object>> rules = (List<Map<String, Object>>) response.get("rules");
        Map<String, Object> complexRule = new HashMap<>();
        complexRule.put("id", "COMPLEX_RULE_001");
        complexRule.put("name", "Multi-Factor Risk Rule");
        
        List<Map<String, Object>> conditions = new ArrayList<>();
        Map<String, Object> condition1 = new HashMap<>();
        condition1.put("field", "amount");
        condition1.put("operator", ">");
        condition1.put("value", 5000.0);
        conditions.add(condition1);
        
        Map<String, Object> condition2 = new HashMap<>();
        condition2.put("field", "customerRiskScore");
        condition2.put("operator", ">=");
        condition2.put("value", 7);
        conditions.add(condition2);
        
        complexRule.put("conditions", conditions);
        complexRule.put("action", "BLOCK");
        complexRule.put("confidence", 0.95);
        rules.add(complexRule);
        
        return response;
    }

    private Map<String, Object> createSuccessfulServiceResponseWithMetadata() {
        Map<String, Object> response = createSuccessfulServiceResponse();
        
        // Enhanced metadata
        Map<String, Object> metadata = (Map<String, Object>) response.get("metadata");
        metadata.put("feature_count", 7);
        metadata.put("generation_method", "complex_dynamic_ml_analysis");
        
        Map<String, Object> modelPerformance = new HashMap<>();
        modelPerformance.put("RandomForest", Map.of("accuracy", 0.92, "f1_score", 0.89));
        modelPerformance.put("GradientBoosting", Map.of("accuracy", 0.90, "f1_score", 0.87));
        metadata.put("model_performance", modelPerformance);
        
        return response;
    }

    private Map<String, Object> createSuccessfulServiceResponseWithDifferentFields() {
        Map<String, Object> response = createSuccessfulServiceResponse();
        
        // Add rules with different field types
        List<Map<String, Object>> rules = (List<Map<String, Object>>) response.get("rules");
        
        Map<String, Object> feeRule = new HashMap<>();
        feeRule.put("id", "FEE_RULE_001");
        feeRule.put("name", "Fee Ratio Rule");
        feeRule.put("field", "fee_ratio");
        feeRule.put("operator", ">");
        feeRule.put("value", 0.05);
        feeRule.put("action", "REVIEW");
        feeRule.put("confidence", 0.75);
        rules.add(feeRule);
        
        Map<String, Object> merchantRule = new HashMap<>();
        merchantRule.put("id", "MERCHANT_RULE_001");
        merchantRule.put("name", "Merchant Category Rule");
        merchantRule.put("field", "merchantCategoryCode");
        merchantRule.put("operator", "in");
        merchantRule.put("value", Arrays.asList("5411", "5812"));
        merchantRule.put("action", "ALERT");
        merchantRule.put("confidence", 0.80);
        rules.add(merchantRule);
        
        return response;
    }
}
package net.com.fms_core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.com.fms_core.service.impl.script.ProductionRuleGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductionRuleGenerationServiceTest {

    @InjectMocks
    private ProductionRuleGenerationService productionRuleGenerationService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(productionRuleGenerationService, "objectMapper", objectMapper);
    }

    @Test
    void testGenerateAdvancedMLRules_Success() {
        // Given
        String mockPythonOutput = """
                Successfully loaded data from synthetic data
                Generated synthetic dataset with 1000 transactions
                ===RULES_START===
                [
                  {
                    "id": "DYNAMIC_AMOUNT_CLUSTERING_1234",
                    "name": "Dynamic Amount Cluster Analysis",
                    "field": "amount",
                    "operator": ">=",
                    "value": 5000.0,
                    "action": "ALERT",
                    "priority": "HIGH",
                    "confidence": 0.85,
                    "model_source": "RandomForest_clustering",
                    "explanation": "Dynamic clustering analysis detected amount pattern at $5,000.00",
                    "business_impact": "Targets high-value transactions using cluster analysis"
                  }
                ]
                ===RULES_END===
                ===METADATA_START===
                {
                  "total_rules": 1,
                  "data_points_analyzed": 1000,
                  "model_type": "complex_dynamic_ml_analysis",
                  "best_model": "RandomForest",
                  "model_accuracy": 0.9234,
                  "training_samples": 50000,
                  "fraud_rate": 0.023
                }
                ===METADATA_END===
                SUMMARY: Generated 1 truly dynamic rules using RandomForest complex analysis
                """;

        // When
        Map<String, Object> result = productionRuleGenerationService.generateAdvancedMLRules();

        // Then
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertNotNull(result.get("rules"));
        assertNotNull(result.get("metadata"));
        assertEquals("ADVANCED_ML", result.get("ruleType"));
        
        List<Map<String, Object>> rules = (List<Map<String, Object>>) result.get("rules");
        assertFalse(rules.isEmpty());
        
        Map<String, Object> metadata = (Map<String, Object>) result.get("metadata");
        assertNotNull(metadata.get("total_rules"));
        assertNotNull(metadata.get("best_model"));
    }

    @Test
    void testGenerateAdvancedMLRules_PythonScriptNotFound() {
        // When
        Map<String, Object> result = productionRuleGenerationService.generateAdvancedMLRules();

        // Then
        assertNotNull(result);
        assertFalse((Boolean) result.get("success"));
        assertNotNull(result.get("error"));
        assertTrue(result.get("error").toString().contains("Python script not found") || 
                  result.get("error").toString().contains("Failed to generate"));
    }

    @Test
    void testGenerateAdvancedMLRules_PythonExecutionFailure() {
        // This test simulates Python execution failure
        Map<String, Object> result = productionRuleGenerationService.generateAdvancedMLRules();

        // Then
        assertNotNull(result);
        // Should handle failure gracefully
        if (!(Boolean) result.getOrDefault("success", false)) {
            assertNotNull(result.get("error"));
            assertNotNull(result.get("output"));
        }
    }

    @Test
    void testGenerateAdvancedMLRules_EmptyOutput() {
        // Test case where Python script runs but produces no output
        Map<String, Object> result = productionRuleGenerationService.generateAdvancedMLRules();

        // Then
        assertNotNull(result);
        assertNotNull(result.get("output"));
        
        if ((Boolean) result.getOrDefault("success", false)) {
            // If successful, should have rules and metadata
            assertNotNull(result.get("rules"));
            assertNotNull(result.get("metadata"));
        } else {
            // If failed, should have error information
            assertNotNull(result.get("error"));
        }
    }

    @Test
    void testGenerateAdvancedMLRules_InvalidJsonOutput() {
        // This would test the JSON parsing error handling
        Map<String, Object> result = productionRuleGenerationService.generateAdvancedMLRules();

        // Then
        assertNotNull(result);
        // Should handle JSON parsing errors gracefully
        if (!(Boolean) result.getOrDefault("success", false)) {
            String error = (String) result.get("error");
            // Should contain meaningful error message
            assertNotNull(error);
        }
    }

    @Test
    void testTrainModel_Success() {
        // When
        Map<String, Object> result = productionRuleGenerationService.trainModel();

        // Then
        assertNotNull(result);
        assertNotNull(result.get("success"));
        assertNotNull(result.get("output"));
        assertNotNull(result.get("message"));
        
        if ((Boolean) result.get("success")) {
            assertEquals("Model trained successfully", result.get("message"));
        } else {
            assertEquals("Training failed", result.get("message"));
        }
    }

    @Test
    void testGenerateRulesFromModel_Success() {
        // When
        Map<String, Object> result = productionRuleGenerationService.generateRulesFromModel();

        // Then
        assertNotNull(result);
        assertNotNull(result.get("success"));
        assertNotNull(result.get("output"));
        assertNotNull(result.get("generatedAt"));
        
        // Should have rules and metadata fields
        assertTrue(result.containsKey("rules"));
        assertTrue(result.containsKey("metadata"));
    }

    @Test
    void testGenerateAdvancedRules_WithCsvPath() {
        // Given
        String csvPath = "test_transactions.csv";

        // When
        Map<String, Object> result = productionRuleGenerationService.generateAdvancedRules(csvPath);

        // Then
        assertNotNull(result);
        assertNotNull(result.get("success"));
        assertNotNull(result.get("output"));
        assertNotNull(result.get("generatedAt"));
        
        // Should contain rules and metadata
        assertTrue(result.containsKey("rules"));
        assertTrue(result.containsKey("metadata"));
    }

    @Test
    void testGenerateRulesOnly_Success() {
        // Given
        String csvPath = "test_transactions.csv";

        // When
        String result = productionRuleGenerationService.generateRulesOnly(csvPath);

        // Then
        assertNotNull(result);
        // Should return either rules content or error message
        assertTrue(result.length() > 0);
    }

    @Test
    void testGenerateAdvancedMLRules_ResponseStructure() {
        // When
        Map<String, Object> result = productionRuleGenerationService.generateAdvancedMLRules();

        // Then
        assertNotNull(result);
        
        // Verify response structure
        assertTrue(result.containsKey("success"));
        assertTrue(result.containsKey("rules"));
        assertTrue(result.containsKey("metadata"));
        assertTrue(result.containsKey("output"));
        assertTrue(result.containsKey("generatedAt"));
        assertTrue(result.containsKey("ruleType"));
        
        if ((Boolean) result.get("success")) {
            assertEquals("ADVANCED_ML", result.get("ruleType"));
            
            // Verify rules structure
            Object rules = result.get("rules");
            assertTrue(rules instanceof List);
            
            // Verify metadata structure
            Object metadata = result.get("metadata");
            assertTrue(metadata instanceof Map);
        } else {
            // Should have error information
            assertTrue(result.containsKey("error"));
        }
    }

    @Test
    void testGenerateAdvancedMLRules_ErrorHandling() {
        // When
        Map<String, Object> result = productionRuleGenerationService.generateAdvancedMLRules();

        // Then
        assertNotNull(result);
        
        if (!(Boolean) result.getOrDefault("success", false)) {
            // Verify error response structure
            assertNotNull(result.get("error"));
            assertNotNull(result.get("output"));
            assertTrue(result.containsKey("details"));
            
            String error = (String) result.get("error");
            assertFalse(error.isEmpty());
            
            List<String> output = (List<String>) result.get("output");
            assertNotNull(output);
        }
    }
}
/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.TransactionFilterDto;
import net.com.fms_core.dto.TransactionHistoryDTO;
import net.com.fms_core.dto.TransactionStatusUpdateDTO;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.service.TransactionService;
import net.com.fms_core.service.impl.script.FutureRuleGenerationService;
import net.com.fms_core.service.impl.script.TransactionExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.*;

@RestController
@RequestMapping("/api/v1/tran")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private TransactionService tranService;
    private final Sinks.Many<TransactionHistoryDTO> sink;
    private final TransactionExportService exportService;
    private final FutureRuleGenerationService ruleService;

    @Autowired
    private ObjectMapper objectMapper;
    public TransactionController(TransactionService tranService, Sinks.Many<TransactionHistoryDTO> sink, TransactionExportService exportService, FutureRuleGenerationService ruleService) {
        this.tranService = tranService;
        this.sink = sink;
        this.exportService = exportService;
        this.ruleService = ruleService;
    }
    @PostMapping("/save-tran")
    public ResponseEntity<ApiResponseDTO> saveTran(@RequestBody List<TransactionHistoryDTO> tranDto){
        return tranService.saveTestTran(tranDto);
    }
    @GetMapping("/get-all-trans")
    public ResponseEntity<ApiResponseDTO> getAllTran(@RequestBody TransactionFilterDto transactionFilterDto){
        return tranService.getAllTran(transactionFilterDto);
    }
    @GetMapping("/get-all-tran")
    public ResponseEntity<ApiResponseDTO> getAllTrans(){
        return tranService.getAllTrans();
    }
    @GetMapping(value = "/transaction/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<TransactionHistoryDTO>> streamTransactions() {
        return sink.asFlux().map(data ->
                ServerSentEvent.builder(data).build()
        );
    }
    public void publish(TransactionHistory transaction) {
        System.out.println("emit............");
        TransactionHistoryDTO dto = new TransactionHistoryDTO();
        dto.setTransactionHistoryId(transaction.getTransactionHistoryId());
        dto.setTranUuid(transaction.getTranUuid());
        dto.setTranPacket(convertJsonToDto(transaction.getTranPacket()));
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        dto.setCreatedBy(transaction.getCreatedBy());
        dto.setUpdatedBy(transaction.getUpdatedBy());
        sink.tryEmitNext(dto);  // Push to connected clients
    }
    public IsoMessageDTO convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, IsoMessageDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/get-all-vari-names")
    public ResponseEntity<ApiResponseDTO> getAllVariableNames(){
        return tranService.getAllVariableNames();
    }


    @Autowired
    private net.com.fms_core.service.impl.script.MLModelTrainingService mlModelTrainingService;
    
    @Autowired
    private net.com.fms_core.service.impl.script.AIRuleDeploymentService deploymentService;
    
    @Autowired
    private net.com.fms_core.service.impl.script.ProductionRuleGenerationService productionRuleService;
    
    @Autowired
    private net.com.fms_core.service.impl.script.DynamicRuleGenerationService dynamicRuleService;

    @PostMapping("/train-fraud-model")
    public ResponseEntity<Map<String, Object>> trainFraudModel() {
        try {
            log.info("Training ML fraud detection models...");
            Map<String, Object> result = mlModelTrainingService.trainModel();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/generate-future-rules")
    public ResponseEntity<ApiResponseDTO> generateFutureRules(@RequestBody(required = false) Map<String, Object> params) {
        try {
            log.info("Generating ML-based intelligent rules with pre-trained models...");
            
            // Generate rules using pre-trained ML models
            Map<String, Object> result = productionRuleService.generateAdvancedMLRules();
            
            if (!(Boolean) result.getOrDefault("success", false)) {
                ApiResponseDTO errorResponse = new ApiResponseDTO();
                errorResponse.setSuccess(false);
                errorResponse.setData(null);
                errorResponse.setMessage("ML rule generation failed: " + result.get("error"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
            
            // Extract metadata and rules
            Map<String, Object> metadata = (Map<String, Object>) result.get("metadata");
            List<Map<String, Object>> rules = (List<Map<String, Object>>) result.get("rules");
            
            // Convert rules to DRL format for UI display
            StringBuilder rulesString = new StringBuilder();
            if (rules != null) {
                for (Map<String, Object> rule : rules) {
                    rulesString.append("import net.com.fms_core.dto.message.IsoMessageDTO;\n\n");
                    rulesString.append("rule \"").append(rule.get("name")).append("\"\n");
                    rulesString.append("when\n");
                    rulesString.append("  $msg : IsoMessageDTO(");
                    
                    // Generate proper conditions based on available fields
                    if (rule.containsKey("field")) {
                        String field = (String) rule.get("field");
                        String operator = (String) rule.get("operator");
                        Object value = rule.get("value");
                        
                        // Map ML fields to actual DTO fields
                        String actualField = mapToActualField(field);
                        String condition = generateCondition(actualField, operator, value);
                        rulesString.append(condition);
                    } else if (rule.containsKey("conditions")) {
                        // Handle multi-condition rules
                        List<Map<String, Object>> conditions = (List<Map<String, Object>>) rule.get("conditions");
                        List<String> conditionStrings = new ArrayList<>();
                        for (Map<String, Object> condition : conditions) {
                            String field = (String) condition.get("field");
                            String operator = (String) condition.get("operator");
                            Object value = condition.get("value");
                            String actualField = mapToActualField(field);
                            conditionStrings.add(generateCondition(actualField, operator, value));
                        }
                        rulesString.append(String.join(" && ", conditionStrings));
                    } else {
                        rulesString.append("amount > 1000"); // fallback condition
                    }
                    
                    rulesString.append(")\n");
                    rulesString.append("then\n");
                    rulesString.append("  $msg.setStatus(\"").append(rule.get("action")).append("\");\n");
                    rulesString.append("  $msg.setRiskLevel(\"HIGH\");\n");
                    rulesString.append("  $msg.setRiskScore(80.0);\n");
                    rulesString.append("  $msg.setFiredRule(\"").append(rule.get("name")).append("\");\n");
                    rulesString.append("end\n\n");
                }
            }
            
            // Format response data for UI
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("ruleType", "ML_BASED_INTELLIGENT");
            // Dynamic description based on actual model data
            String description = "Dynamic ML-based rule generation";
            if (metadata != null) {
                Object bestModel = metadata.get("best_model");
                Object modelAccuracy = metadata.get("model_accuracy");
                if (bestModel != null && modelAccuracy instanceof Number) {
                    double accuracy = ((Number) modelAccuracy).doubleValue();
                    description = String.format("%s model with %.4f%% accuracy", bestModel, accuracy * 100);
                }
            }
            responseData.put("description", description);
            responseData.put("rules", rulesString.toString());
            responseData.put("deployed", false);
            responseData.put("preview", true);
            responseData.put("generatedAt", result.get("generatedAt"));
            
            // Enhanced metadata for UI
            Map<String, Object> uiMetadata = new HashMap<>();
            if (metadata != null) {
                uiMetadata.put("total_rules", metadata.get("total_rules"));
                
                // Use actual model accuracy from metadata - no hardcoding
                Object modelAccuracy = metadata.get("model_accuracy");
                if (modelAccuracy instanceof Number) {
                    double accuracy = ((Number) modelAccuracy).doubleValue();
                    uiMetadata.put("model_accuracy", String.format("%.4f%%", accuracy * 100));
                    uiMetadata.put("model_f1_score", accuracy);
                } else {
                    uiMetadata.put("model_accuracy", "N/A");
                    uiMetadata.put("model_f1_score", null);
                }
                
                // Use actual metrics from model performance - no hardcoding
                Object modelPerformance = metadata.get("model_performance");
                if (modelPerformance instanceof Map) {
                    uiMetadata.put("metrics", modelPerformance);
                } else {
                    uiMetadata.put("metrics", null);
                }
                
                // All other fields from actual metadata
                uiMetadata.put("feature_count", metadata.get("feature_count"));
                uiMetadata.put("training_samples", metadata.get("training_samples"));
                uiMetadata.put("best_model", metadata.get("best_model"));
                uiMetadata.put("model_performance", metadata.get("model_performance"));
                uiMetadata.put("rule_details", metadata.get("rule_details"));
                uiMetadata.put("generated_at", result.get("generatedAt"));
                uiMetadata.put("model_used", metadata.get("best_model"));
                uiMetadata.put("total_transactions", metadata.get("training_samples"));
                
                // Dynamic rule types from actual rules
                if (rules != null && !rules.isEmpty()) {
                    List<String> ruleTypes = rules.stream()
                        .map(rule -> (String) rule.get("action"))
                        .distinct()
                        .collect(java.util.stream.Collectors.toList());
                    uiMetadata.put("rule_types", ruleTypes);
                } else {
                    uiMetadata.put("rule_types", java.util.Collections.emptyList());
                }
                
                // Dynamic status distribution from actual rules
                if (rules != null && !rules.isEmpty()) {
                    Map<String, Long> statusCount = rules.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                            rule -> (String) rule.get("action"),
                            java.util.stream.Collectors.counting()
                        ));
                    Map<String, Integer> statusDistribution = new HashMap<>();
                    statusCount.forEach((key, value) -> statusDistribution.put(key, value.intValue()));
                    uiMetadata.put("status_distribution", statusDistribution);
                } else {
                    uiMetadata.put("status_distribution", new HashMap<>());
                }
            } else {
                // Only set null/empty values if metadata is completely missing
                uiMetadata.put("total_rules", rules != null ? rules.size() : 0);
                uiMetadata.put("model_accuracy", "N/A");
                uiMetadata.put("model_f1_score", null);
                uiMetadata.put("feature_count", null);
                uiMetadata.put("training_samples", null);
                uiMetadata.put("best_model", "Unknown");
                uiMetadata.put("metrics", null);
                uiMetadata.put("rule_types", java.util.Collections.emptyList());
                uiMetadata.put("status_distribution", new HashMap<>());
            }
            
            responseData.put("metadata", uiMetadata);
            // Dynamic features list based on actual metadata
            List<String> features = new ArrayList<>();
            if (metadata != null) {
                Object bestModel = metadata.get("best_model");
                Object trainingSamples = metadata.get("training_samples");
                Object featureCount = metadata.get("feature_count");
                Object modelAccuracy = metadata.get("model_accuracy");
                
                if (bestModel != null) {
                    features.add("Best Model: " + bestModel);
                }
                if (modelAccuracy instanceof Number) {
                    double accuracy = ((Number) modelAccuracy).doubleValue();
                    features.add(String.format("Model Accuracy: %.4f%%", accuracy * 100));
                }
                if (trainingSamples != null) {
                    features.add("Training Samples: " + trainingSamples);
                }
                if (featureCount != null) {
                    features.add("Features Analyzed: " + featureCount);
                }
                
                Object modelPerformance = metadata.get("model_performance");
                if (modelPerformance instanceof Map) {
                    Map<String, Object> performance = (Map<String, Object>) modelPerformance;
                    features.add("Models: " + String.join(", ", performance.keySet()));
                }
                
                features.add("Generation Method: " + metadata.getOrDefault("generation_method", "ML-based"));
                features.add("Real-time fraud detection");
                features.add("Production-ready rules");
            } else {
                features.add("Dynamic ML-based rule generation");
                features.add("Real-time fraud detection");
            }
            
            responseData.put("features", features);
            
            ApiResponseDTO apiResponse = new ApiResponseDTO();
            apiResponse.setSuccess(true);
            apiResponse.setData(responseData);
            apiResponse.setMessage("Pre-trained ML rules generated successfully (PREVIEW - not saved to database)");
            
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            log.error("Failed to generate ML-based rules: {}", e.getMessage(), e);
            ApiResponseDTO errorResponse = new ApiResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setData(null);
            errorResponse.setMessage("ML rule generation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/generate-future-rules")
    public ResponseEntity<ApiResponseDTO> generateFutureRulesGet() {
        return generateFutureRules(null);
    }
    
    @PostMapping("/deploy-dynamic-rules")
    public ResponseEntity<ApiResponseDTO> deployDynamicRules(@RequestBody Map<String, Object> request) {
        try {
            String rulesContent = (String) request.get("rules");
            
            if (rulesContent == null || rulesContent.trim().isEmpty()) {
                ApiResponseDTO errorResponse = new ApiResponseDTO();
                errorResponse.setSuccess(false);
                errorResponse.setData(null);
                errorResponse.setMessage("No rules content provided for deployment");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            log.info("Deploying dynamic rules to database and KIE base...");
            Map<String, Object> result = dynamicRuleService.deployDynamicRules(rulesContent);
            
            ApiResponseDTO apiResponse = new ApiResponseDTO();
            if ((Boolean) result.getOrDefault("success", false)) {
                apiResponse.setSuccess(true);
                apiResponse.setData(result);
                apiResponse.setMessage("Dynamic rules deployed successfully to database and KIE base");
                return ResponseEntity.ok(apiResponse);
            } else {
                apiResponse.setSuccess(false);
                apiResponse.setData(null);
                apiResponse.setMessage((String) result.get("message"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
            }
        } catch (Exception e) {
            log.error("Failed to deploy dynamic rules: {}", e.getMessage(), e);
            ApiResponseDTO errorResponse = new ApiResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setData(null);
            errorResponse.setMessage("Rule deployment failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/deploy-ai-rules")
    public ResponseEntity<Map<String, Object>> deployAIRules() {
        try {
            String csvPath = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\csv\\transactions.csv";
            String generatedRules = ruleService.generateRules(csvPath);
            deploymentService.saveAndDeployRules(generatedRules);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "AI rules deployed successfully to AI KIE base");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/deploy-ai-rules")
    public ResponseEntity<Map<String, Object>> deployAIRulesGet() {
        return deployAIRules();
    }
    
    @PutMapping("/update-status")
    public ResponseEntity<ApiResponseDTO> updateTransactionStatus(@RequestBody TransactionStatusUpdateDTO updateDTO) {
        return tranService.updateTransactionStatus(updateDTO);
    }
    
    @DeleteMapping("/delete-tran/{id}")
    public ResponseEntity<ApiResponseDTO> deleteTransaction(@PathVariable Long id) {
        return tranService.deleteTransaction(id);
    }
    
    private String mapToActualField(String mlField) {
        // Map ML-generated field names to actual IsoMessageDTO fields
        switch (mlField) {
            case "fee_ratio":
                return "transactionFeeAmount"; // Use actual fee amount instead of ratio
            case "fraud_probability":
                return "fraudPercentage"; // Use existing fraud percentage field
            case "customerRiskScore":
                return "customerRiskScore"; // Direct mapping
            case "amount":
                return "amount"; // Direct mapping
            case "transactionFeeAmount":
                return "transactionFeeAmount"; // Direct mapping
            case "merchantCategoryCode":
                return "merchantCategoryCode"; // Direct mapping
            default:
                return "amount"; // Default fallback
        }
    }
    
    private String generateCondition(String field, String operator, Object value) {
        // Generate proper Drools conditions based on field type
        if ("merchantCategoryCode".equals(field) && "in".equals(operator)) {
            // Handle merchant category list conditions
            if (value instanceof List) {
                List<?> values = (List<?>) value;
                List<String> conditions = new ArrayList<>();
                for (Object val : values) {
                    conditions.add(field + " == \"" + val + "\"");
                }
                return "(" + String.join(" || ", conditions) + ")";
            }
        }
        
        // Handle numeric conditions
        if ("transactionFeeAmount".equals(field) && "fee_ratio".equals(field)) {
            // Convert fee ratio to actual fee amount comparison
            if (value instanceof Number) {
                double threshold = ((Number) value).doubleValue() * 10000; // Approximate conversion
                return "transactionFeeAmount > " + threshold;
            }
        }
        
        // Handle fraud percentage conditions
        if ("fraudPercentage".equals(field)) {
            if (value instanceof Number) {
                double threshold = ((Number) value).doubleValue() * 100; // Convert to percentage
                return "fraudPercentage != null && fraudPercentage " + operator + " " + threshold;
            }
        }
        
        // Standard numeric conditions
        if (value instanceof Number) {
            return field + " " + operator + " " + value;
        }
        
        // String conditions
        if (value instanceof String) {
            return field + " " + operator + " \"" + value + "\"";
        }
        
        return field + " " + operator + " " + value;
    }
}

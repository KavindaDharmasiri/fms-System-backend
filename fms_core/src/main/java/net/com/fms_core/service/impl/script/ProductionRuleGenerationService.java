package net.com.fms_core.service.impl.script;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Service
public class ProductionRuleGenerationService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public Map<String, Object> trainModel() {
        List<String> output = new ArrayList<>();
        String pythonCommand = "python";
        String scriptPath = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\train_model.py";
        
        try {
            log.info("Training fraud detection model...");
            ProcessBuilder pb = new ProcessBuilder(pythonCommand, scriptPath);
            pb.directory(new java.io.File("C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator"));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }

            int exitCode = process.waitFor();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", exitCode == 0);
            response.put("output", output);
            response.put("message", exitCode == 0 ? "Model trained successfully" : "Training failed");
            return response;
        } catch (Exception e) {
            log.error("Failed to train model: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("output", output);
            return errorResponse;
        }
    }
    
    public Map<String, Object> generateRulesFromModel() {
        StringBuilder rulesContent = new StringBuilder();
        StringBuilder metadataContent = new StringBuilder();
        List<String> output = new ArrayList<>();
        
        String pythonCommand = "python";
        String scriptPath = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\generate_rules_fast.py";
        
        try {
            log.info("Generating rules from trained model...");
            ProcessBuilder pb = new ProcessBuilder(pythonCommand, scriptPath);
            pb.directory(new java.io.File("C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator"));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean captureRules = false;
            boolean captureMetadata = false;
            
            while ((line = reader.readLine()) != null) {
                output.add(line);
                
                if (line.equals("===RULES_START===")) {
                    captureRules = true;
                    continue;
                }
                if (line.equals("===RULES_END===")) {
                    captureRules = false;
                    continue;
                }
                if (captureRules) {
                    rulesContent.append(line).append("\n");
                }
                
                if (line.equals("===METADATA_START===")) {
                    captureMetadata = true;
                    continue;
                }
                if (line.equals("===METADATA_END===")) {
                    captureMetadata = false;
                    continue;
                }
                if (captureMetadata) {
                    metadataContent.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            
            Map<String, Object> metadata = new HashMap<>();
            try {
                if (metadataContent.length() > 0) {
                    metadata = objectMapper.readValue(metadataContent.toString(), Map.class);
                }
            } catch (Exception e) {
                log.warn("Could not parse metadata: {}", e.getMessage());
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("rules", rulesContent.toString());
            response.put("metadata", metadata);
            response.put("output", output);
            response.put("success", exitCode == 0);
            response.put("generatedAt", new Date());
            
            return response;
        } catch (Exception e) {
            log.error("Failed to generate rules: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("output", output);
            return errorResponse;
        }
    }
    
    public Map<String, Object> generateAdvancedRules(String csvPath) {
        StringBuilder rulesContent = new StringBuilder();
        StringBuilder metadataContent = new StringBuilder();
        List<String> output = new ArrayList<>();
        
        String pythonCommand = "python";
        String scriptPath = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\advanced_fraud_trainer.py";
        
        try {
            log.info("Starting ADVANCED fraud rule generation with real fraud dataset...");
            
            // No csvPath needed - uses built-in datasets
            ProcessBuilder pb = new ProcessBuilder(pythonCommand, scriptPath);
            pb.directory(new java.io.File("C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator"));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean captureRules = false;
            boolean captureMetadata = false;
            
            while ((line = reader.readLine()) != null) {
                output.add(line);
                
                // Capture rules
                if (line.equals("===RULES_START===")) {
                    captureRules = true;
                    continue;
                }
                if (line.equals("===RULES_END===")) {
                    captureRules = false;
                    continue;
                }
                if (captureRules) {
                    rulesContent.append(line).append("\n");
                }
                
                // Capture metadata
                if (line.equals("===METADATA_START===")) {
                    captureMetadata = true;
                    continue;
                }
                if (line.equals("===METADATA_END===")) {
                    captureMetadata = false;
                    continue;
                }
                if (captureMetadata) {
                    metadataContent.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            log.info("Python script completed with exit code: {}", exitCode);
            
            // Parse metadata
            Map<String, Object> metadata = new HashMap<>();
            try {
                if (metadataContent.length() > 0) {
                    metadata = objectMapper.readValue(metadataContent.toString(), Map.class);
                }
            } catch (Exception e) {
                log.warn("Could not parse metadata: {}", e.getMessage());
            }
            
            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("rules", rulesContent.toString());
            response.put("metadata", metadata);
            response.put("output", output);
            response.put("success", exitCode == 0);
            response.put("generatedAt", new Date());
            
            log.info("Rule generation completed. Rules count: {}", 
                    metadata.getOrDefault("total_rules", "unknown"));
            
            return response;

        } catch (Exception e) {
            log.error("Failed to generate production rules: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("output", output);
            
            return errorResponse;
        }
    }
    
    public String generateRulesOnly(String csvPath) {
        Map<String, Object> result = generateAdvancedRules(csvPath);
        return (String) result.getOrDefault("rules", "Error: No rules generated");
    }
}

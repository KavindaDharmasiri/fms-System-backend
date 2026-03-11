package net.com.fms_core.service.impl.script;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.entity.AIRule;
import net.com.fms_core.entity.AIRuleGroup;
import net.com.fms_core.repository.AIRuleGroupRepository;
import net.com.fms_core.repository.AIRuleRepository;
import net.com.fms_core.repository.TransactionRepository;
import net.com.fms_core.service.impl.AIKieService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicRuleGenerationService {

    private final TransactionRepository transactionRepository;
    private final AIRuleRepository aiRuleRepository;
    private final AIRuleGroupRepository aiRuleGroupRepository;
    private final AIKieService aiKieService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> generateDynamicRules() {
        try {
            log.info("Starting dynamic rule generation (preview mode) with real transaction data...");
            
            // Export current transaction data for analysis
            String csvPath = exportTransactionData();
            
            // Generate intelligent rules using Python ML (preview only)
            Map<String, Object> result = executeDynamicRuleGeneration(csvPath);
            
            if ((Boolean) result.getOrDefault("success", false)) {
                String rulesContent = (String) result.get("rules");
                if (rulesContent != null && !rulesContent.trim().isEmpty()) {
                    result.put("deployed", false); // Preview mode - not deployed
                    result.put("preview", true);
                    result.put("message", "Rules generated successfully (preview mode - not saved to database)");
                    log.info("Successfully generated dynamic rules in preview mode");
                } else {
                    log.warn("No rules generated, but process completed successfully");
                    result.put("deployed", false);
                    result.put("preview", true);
                    result.put("message", "Rule generation completed but no rules were created");
                }
            } else {
                log.error("Dynamic rule generation failed: {}", result.get("message"));
            }
            
            return result;
        } catch (Exception e) {
            log.error("Failed to generate dynamic rules: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Dynamic rule generation failed: " + e.getMessage());
            error.put("deployed", false);
            error.put("preview", true);
            return error;
        }
    }
    
    public Map<String, Object> deployDynamicRules(String rulesContent) {
        try {
            log.info("Starting deployment of dynamic rules to database and KIE base...");
            
            if (rulesContent == null || rulesContent.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "No rules content provided for deployment");
                return error;
            }
            
            // Decode HTML entities if present
            String cleanedRules = rulesContent
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'");
            
            log.debug("Cleaned rules content: {}", cleanedRules.substring(0, Math.min(500, cleanedRules.length())));
            
            // Validate rule syntax before deployment
            String validationError = validateRuleSyntax(cleanedRules);
            if (validationError != null) {
                log.error("Rule validation failed: {}", validationError);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Rule validation failed: " + validationError);
                return error;
            }
            
            // Count rules in content for validation
            String[] ruleSections = cleanedRules.split("(?=import\\s+net\\.com\\.fms_core\\.dto\\.message\\.IsoMessageDTO;)");
            int expectedRuleCount = (int) Arrays.stream(ruleSections)
                .filter(section -> section.trim().length() > 0 && section.contains("rule \""))
                .count();
            
            log.info("Expected to deploy {} rules based on content analysis", expectedRuleCount);
            
            // Deploy rules to database and KIE base
            deployGeneratedRules(cleanedRules);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("deployed", true);
            response.put("message", String.format("Successfully deployed %d dynamic rules to database and KIE base", expectedRuleCount));
            response.put("deployedAt", new Date());
            response.put("rulesDeployed", expectedRuleCount);
            
            log.info("Successfully completed deployment of {} rules", expectedRuleCount);
            return response;
        } catch (Exception e) {
            log.error("Failed to deploy dynamic rules: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Rule deployment failed: " + e.getMessage());
            error.put("deployed", false);
            return error;
        }
    }
    
    private String validateRuleSyntax(String rulesContent) {
        try {
            // Check for common syntax issues
            if (rulesContent.contains("transactionVelocity >") && !rulesContent.contains("transactionVelocity() >")) {
                return "Invalid method call: use transactionVelocity() instead of transactionVelocity";
            }
            
            if (rulesContent.contains("10.00.0") || rulesContent.contains(".00.0")) {
                return "Invalid number literal format detected";
            }
            
            // Basic validation - just check if we have valid rule structure
            if (!rulesContent.contains("import net.com.fms_core.dto.message.IsoMessageDTO")) {
                return "Missing import statement";
            }
            
            if (!rulesContent.contains("rule \"")) {
                return "No valid rule declarations found";
            }
            
            return null; // No validation errors
        } catch (Exception e) {
            return "Validation error: " + e.getMessage();
        }
    }

    private String exportTransactionData() {
        // Use existing CSV files for rule generation
        String[] availableCsvFiles = {
            "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\csv\\transactions.csv",
            "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\csv\\creditcard.csv",
            "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\csv\\train_transaction.csv"
        };
        
        // Find the first available CSV file
        for (String csvPath : availableCsvFiles) {
            java.io.File file = new java.io.File(csvPath);
            if (file.exists()) {
                log.info("Using CSV file for rule generation: {}", csvPath);
                return csvPath;
            }
        }
        
        // If no files found, return the default path (Python will handle the error gracefully)
        log.warn("No CSV files found, Python script will use fallback data");
        return availableCsvFiles[0];
    }

    private Map<String, Object> executeDynamicRuleGeneration(String csvPath) {
        List<String> output = new ArrayList<>();
        StringBuilder rulesContent = new StringBuilder();
        StringBuilder metadataContent = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();
        
        String pythonCommand = "python";
        String scriptPath = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\ml_rule_generator.py";
        String csvDirectory = "C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator\\csv";
        
        try {
            ProcessBuilder pb = new ProcessBuilder(pythonCommand, scriptPath, csvDirectory);
            pb.directory(new java.io.File("C:\\Users\\kavinda_d\\Documents\\e soft\\final project\\project\\fms backend\\fms_core\\src\\main\\java\\net\\com\\fms_core\\script\\rulegenerator"));
            Process process = pb.start();

            // Read stdout
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean captureRules = false;
            boolean captureMetadata = false;
            
            while ((line = reader.readLine()) != null) {
                output.add(line);
                log.debug("Python output: {}", line);
                
                if (line.equals("===DYNAMIC_RULES_START===")) {
                    captureRules = true;
                    continue;
                }
                if (line.equals("===DYNAMIC_RULES_END===")) {
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
            
            // Read stderr
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorOutput.append(errorLine).append("\n");
                log.error("Python error: {}", errorLine);
            }

            int exitCode = process.waitFor();
            log.info("ML rule generator completed with exit code: {}", exitCode);
            
            Map<String, Object> metadata = new HashMap<>();
            try {
                if (metadataContent.length() > 0) {
                    metadata = objectMapper.readValue(metadataContent.toString(), Map.class);
                    log.info("Parsed metadata with {} keys", metadata.size());
                    if (metadata.containsKey("rule_details")) {
                        log.info("Found rule_details with {} items", ((List<?>) metadata.get("rule_details")).size());
                    } else {
                        log.warn("No rule_details found in metadata");
                    }
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
            response.put("ruleType", "ML_BASED_INTELLIGENT");
            
            if (exitCode != 0) {
                response.put("error", "Python script failed with exit code: " + exitCode);
                response.put("errorOutput", errorOutput.toString());
            }
            
            return response;
        } catch (Exception e) {
            log.error("Failed to execute dynamic rule generation: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Java execution error: " + e.getMessage());
            errorResponse.put("output", output);
            errorResponse.put("errorOutput", errorOutput.toString());
            return errorResponse;
        }
    }

    private void deployGeneratedRules(String rulesContent) {
        try {
            // Create dynamic rule group
            AIRuleGroup ruleGroup = createDynamicRuleGroup();
            
            // Parse and save individual rules
            List<AIRule> savedRules = parseAndSaveRules(rulesContent, ruleGroup);
            
            // Deploy to KIE base
            if (!savedRules.isEmpty()) {
                aiKieService.deployAIRules(savedRules);
                
                // Mark as deployed
                savedRules.forEach(rule -> {
                    rule.setIsDeployed(true);
                    aiRuleRepository.save(rule);
                });
                
                ruleGroup.setIsDeployed(true);
                aiRuleGroupRepository.save(ruleGroup);
                
                log.info("Successfully deployed {} dynamic rules", savedRules.size());
            }
        } catch (Exception e) {
            log.error("Failed to deploy dynamic rules: {}", e.getMessage(), e);
        }
    }

    private AIRuleGroup createDynamicRuleGroup() {
        String groupName = "Dynamic_Intelligent_Rules_" + System.currentTimeMillis();
        String groupCode = "DYN_INT_" + System.currentTimeMillis();
        
        AIRuleGroup ruleGroup = new AIRuleGroup();
        ruleGroup.setGroupName(groupName);
        ruleGroup.setGroupCode(groupCode);
        ruleGroup.setDescription("Dynamic Intelligent Rules - Real-time fraud detection");
        ruleGroup.setPriority(1);
        ruleGroup.setStatus(true);
        ruleGroup.setIsDeployed(false);
        ruleGroup.setCreatedBy("DYNAMIC_AI_SYSTEM");
        ruleGroup.setUpdatedBy("DYNAMIC_AI_SYSTEM");
        
        return aiRuleGroupRepository.save(ruleGroup);
    }

    private List<AIRule> parseAndSaveRules(String rulesContent, AIRuleGroup ruleGroup) {
        List<AIRule> savedRules = new ArrayList<>();
        
        if (rulesContent == null || rulesContent.trim().isEmpty()) {
            log.warn("No rules content provided for parsing");
            return savedRules;
        }
        
        log.info("Starting to parse rules content of length: {}", rulesContent.length());
        
        // Split by import statements to get complete rule blocks
        String[] ruleBlocks = rulesContent.split("(?=import\\s+net\\.com\\.fms_core\\.dto\\.message\\.IsoMessageDTO;)");
        
        log.info("Found {} rule blocks after splitting by import statements", ruleBlocks.length);
        
        for (int i = 0; i < ruleBlocks.length; i++) {
            String ruleBlock = ruleBlocks[i].trim();
            
            if (ruleBlock.isEmpty()) {
                log.debug("Skipping empty rule block {}", i);
                continue;
            }
            
            // Ensure the rule block has both import and rule declaration
            if (!ruleBlock.contains("import net.com.fms_core.dto.message.IsoMessageDTO") || 
                !ruleBlock.contains("rule \"")) {
                log.warn("Skipping invalid rule block {} - missing import or rule declaration", i);
                log.debug("Invalid block content: {}", ruleBlock.substring(0, Math.min(200, ruleBlock.length())));
                continue;
            }
            
            String ruleName = extractRuleName(ruleBlock);
            if (ruleName == null || ruleName.trim().isEmpty()) {
                ruleName = "DynamicRule_" + System.currentTimeMillis() + "_" + i;
                log.warn("Could not extract rule name, using generated name: {}", ruleName);
            }
            
            String ruleCode = "DYN_" + ruleName.replaceAll("[^a-zA-Z0-9]", "_").toUpperCase();
            
            // Check if rule already exists
            AIRule existingRule = aiRuleRepository.findAll().stream()
                .filter(r -> r.getRuleCode().equals(ruleCode))
                .findFirst()
                .orElse(null);
            
            AIRule rule;
            if (existingRule != null) {
                log.info("Updating existing rule: {} ({})", ruleName, ruleCode);
                rule = existingRule;
                rule.setUpdatedBy("DYNAMIC_AI_SYSTEM");
            } else {
                log.info("Creating new rule: {} ({})", ruleName, ruleCode);
                rule = new AIRule();
                rule.setCreatedBy("DYNAMIC_AI_SYSTEM");
                rule.setUpdatedBy("DYNAMIC_AI_SYSTEM");
            }
            
            // Set/update rule properties with complete rule block
            rule.setRuleName(ruleName);
            rule.setRuleCode(ruleCode);
            rule.setDescription("Dynamic ML Rule - " + ruleName);
            rule.setDroolRule(ruleBlock); // Save the complete rule block including import
            rule.setPriority(extractPriority(ruleBlock));
            rule.setStatus(true);
            rule.setIsDeployed(false);
            rule.setAiRuleGroup(ruleGroup);
            
            try {
                AIRule savedRule = aiRuleRepository.save(rule);
                savedRules.add(savedRule);
                log.info("Successfully saved rule: {} (ID: {}, Code: {})", ruleName, savedRule.getAiRuleId(), ruleCode);
                log.debug("Saved rule content length: {}", ruleBlock.length());
            } catch (Exception e) {
                log.error("Failed to save rule {}: {}", ruleName, e.getMessage(), e);
            }
        }
        
        log.info("Successfully parsed and saved {} rules out of {} blocks", savedRules.size(), ruleBlocks.length);
        return savedRules;
    }

    private String extractRuleName(String ruleBlock) {
        try {
            // Look for rule "RuleName" pattern
            int ruleStart = ruleBlock.indexOf("rule \"");
            if (ruleStart == -1) {
                return null;
            }
            
            int nameStart = ruleStart + 6; // Length of "rule \""
            int nameEnd = ruleBlock.indexOf("\"", nameStart);
            
            if (nameEnd == -1) {
                return null;
            }
            
            String ruleName = ruleBlock.substring(nameStart, nameEnd).trim();
            log.debug("Extracted rule name: {}", ruleName);
            return ruleName;
        } catch (Exception e) {
            log.error("Error extracting rule name: {}", e.getMessage());
            return null;
        }
    }

    private Integer extractPriority(String ruleBlock) {
        try {
            // Look for salience in the rule block
            if (ruleBlock.contains("salience")) {
                String[] lines = ruleBlock.split("\n");
                for (String line : lines) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.startsWith("salience")) {
                        // Extract number from salience line
                        String numberStr = trimmedLine.replaceAll("[^0-9]", "");
                        if (!numberStr.isEmpty()) {
                            return Integer.parseInt(numberStr);
                        }
                    }
                }
            }
            
            // Default priority based on rule content analysis
            if (ruleBlock.contains("CRITICAL") || ruleBlock.contains("BLOCK")) {
                return 100;
            } else if (ruleBlock.contains("HIGH") || ruleBlock.contains("ALERT")) {
                return 75;
            } else if (ruleBlock.contains("MEDIUM") || ruleBlock.contains("REVIEW")) {
                return 50;
            } else {
                return 25;
            }
        } catch (Exception e) {
            log.warn("Error extracting priority, using default: {}", e.getMessage());
            return 50;
        }
    }
}
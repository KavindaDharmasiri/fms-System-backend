package net.com.fms_core.service.impl.script;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.entity.*;
import net.com.fms_core.repository.*;
import net.com.fms_core.service.impl.AIKieService;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIRuleDeploymentService {

    private final AIRuleRepository aiRuleRepository;
    private final AIRuleGroupRepository aiRuleGroupRepository;
    private final AIKieService aiKieService;

    public void saveAndDeployRules(String rulesContent) {
        // Create AI rule group
        AIRuleGroup aiRuleGroup = createAIRuleGroup();
        if (aiRuleGroup == null) return;
        
        List<AIRule> savedRules = new ArrayList<>();
        String[] individualRules = rulesContent.split("import net.com.fms_core.dto.message.IsoMessageDTO;");
        
        for (String ruleText : individualRules) {
            if (ruleText.trim().isEmpty()) continue;
            
            String fullRule = "import net.com.fms_core.dto.message.IsoMessageDTO;\n" + ruleText.trim();
            String ruleName = extractRuleName(fullRule);
            
            if (ruleName != null) {
                AIRule rule = saveAIRuleToDB(ruleName, fullRule, aiRuleGroup);
                if (rule != null) {
                    savedRules.add(rule);
                }
            }
        }
        
        // Deploy to AI KIE base
        deployToAIKieBase(savedRules);
    }

    private String extractRuleName(String ruleText) {
        Pattern pattern = Pattern.compile("rule\\s+\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(ruleText);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private AIRuleGroup createAIRuleGroup() {
        try {
            String groupName = "AI_Generated_Rules_" + System.currentTimeMillis();
            String groupCode = "AI_GEN_" + System.currentTimeMillis();
            
            // Check if group exists
            AIRuleGroup existingGroup = aiRuleGroupRepository.findAll().stream()
                    .filter(g -> g.getGroupName().equals(groupName))
                    .findFirst()
                    .orElse(null);
            
            if (existingGroup != null) {
                log.info("AI rule group already exists: {}", groupName);
                return existingGroup;
            }
            
            AIRuleGroup aiRuleGroup = new AIRuleGroup();
            aiRuleGroup.setGroupName(groupName);
            aiRuleGroup.setGroupCode(groupCode);
            aiRuleGroup.setDescription("AI Generated Rule Group");
            aiRuleGroup.setPriority(1);
            aiRuleGroup.setStatus(true);
            aiRuleGroup.setIsDeployed(false);
            aiRuleGroup.setCreatedBy("AI_SYSTEM");
            aiRuleGroup.setUpdatedBy("AI_SYSTEM");
            
            aiRuleGroup = aiRuleGroupRepository.save(aiRuleGroup);
            log.info("Created AI rule group: {}", groupName);
            return aiRuleGroup;
        } catch (Exception e) {
            log.error("Failed to create AI rule group: {}", e.getMessage());
            return null;
        }
    }

    private AIRule saveAIRuleToDB(String ruleName, String ruleContent, AIRuleGroup aiRuleGroup) {
        try {
            // Check if rule already exists
            AIRule existingRule = aiRuleRepository.findAll().stream()
                    .filter(r -> r.getRuleName().equals(ruleName))
                    .findFirst()
                    .orElse(null);

            if (existingRule != null) {
                log.info("AI rule already exists in database, skipping: {}", ruleName);
                return existingRule;
            }

            String ruleCode = "AI_" + ruleName.replaceAll("[^a-zA-Z0-9]", "_").toUpperCase();
            
            AIRule aiRule = new AIRule();
            aiRule.setRuleName(ruleName);
            aiRule.setRuleCode(ruleCode);
            aiRule.setDescription("AI Generated Rule");
            aiRule.setDroolRule(ruleContent);
            aiRule.setPriority(1);
            aiRule.setStatus(true);
            aiRule.setIsDeployed(false);
            aiRule.setAiRuleGroup(aiRuleGroup);
            aiRule.setCreatedBy("AI_SYSTEM");
            aiRule.setUpdatedBy("AI_SYSTEM");

            aiRule = aiRuleRepository.save(aiRule);
            log.info("Saved AI rule to database: {}", ruleName);
            return aiRule;
        } catch (Exception e) {
            log.error("Failed to save AI rule {}: {}", ruleName, e.getMessage());
            return null;
        }
    }
    


    private void deployToAIKieBase(List<AIRule> aiRules) {
        try {
            // Automatically deploy to AI KIE base immediately after saving
            aiKieService.deployAIRules(aiRules);
            
            // Mark rules and group as deployed
            aiRules.forEach(rule -> {
                rule.setIsDeployed(true);
                aiRuleRepository.save(rule);
            });
            
            if (!aiRules.isEmpty()) {
                AIRuleGroup group = aiRules.get(0).getAiRuleGroup();
                group.setIsDeployed(true);
                aiRuleGroupRepository.save(group);
            }
            
            log.info("Successfully auto-deployed {} AI rules to AI KIE base", aiRules.size());
        } catch (Exception e) {
            log.error("Failed to auto-deploy AI rules to KIE base: {}", e.getMessage(), e);
        }
    }
}

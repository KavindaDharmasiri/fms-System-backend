package net.com.fms_core.service.impl.script;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.entity.*;
import net.com.fms_core.repository.*;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIRuleDeploymentService {

    private final FmsRuleRepository fmsRuleRepository;
    private final PaymentNetworkRepository paymentNetworkRepository;
    private final RuleGroupRepository ruleGroupRepository;
    private final RuleGroupRuleRepository ruleGroupRuleRepository;
    private final ReactionTemplateRepository reactionTemplateRepository;

    public void saveAndDeployRules(String rulesContent) {
        // Create rule group
        RuleGroup ruleGroup = createRuleGroup();
        if (ruleGroup == null) return;
        
        List<FmsRule> savedRules = new ArrayList<>();
        String[] individualRules = rulesContent.split("import net.com.fms_core.dto.message.IsoMessageDTO;");
        
        for (String ruleText : individualRules) {
            if (ruleText.trim().isEmpty()) continue;
            
            String fullRule = "import net.com.fms_core.dto.message.IsoMessageDTO;\n" + ruleText.trim();
            String ruleName = extractRuleName(fullRule);
            
            if (ruleName != null) {
                FmsRule rule = saveToDB(ruleName, fullRule);
                if (rule != null) {
                    savedRules.add(rule);
                }
            }
        }
        
        // Add rules to group
        addRulesToGroup(ruleGroup, savedRules);
        
        deployToKieBase(rulesContent);
    }

    private String extractRuleName(String ruleText) {
        Pattern pattern = Pattern.compile("rule\\s+\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(ruleText);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private RuleGroup createRuleGroup() {
        try {
            String groupName = "AI_Generated_Rules_" + System.currentTimeMillis();
            
            // Check if group exists
            RuleGroup existingGroup = ruleGroupRepository.findAll().stream()
                    .filter(g -> g.getGroupName().equals(groupName))
                    .findFirst()
                    .orElse(null);
            
            if (existingGroup != null) {
                log.info("Rule group already exists: {}", groupName);
                return existingGroup;
            }
            
            PaymentNetwork defaultNetwork = paymentNetworkRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No payment network found"));
            
            ReactionTemplate defaultTemplate = reactionTemplateRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No reaction template found"));
            
            RuleGroup ruleGroup = new RuleGroup();
            ruleGroup.setRuleGroupUuid(UUID.randomUUID().toString());
            ruleGroup.setGroupName(groupName);
            ruleGroup.setVerdict("BLOCK");
            ruleGroup.setStatus("ACTIVE");
            ruleGroup.setPaymentNetworkId(defaultNetwork);
            ruleGroup.setReactionTemplateId(defaultTemplate);
            ruleGroup.setCreatedAt(new Date());
            ruleGroup.setUpdatedAt(new Date());
            ruleGroup.setCreatedBy("AI_SYSTEM");
            ruleGroup.setUpdatedBy("AI_SYSTEM");
            
            ruleGroup = ruleGroupRepository.save(ruleGroup);
            log.info("Created AI rule group: {}", groupName);
            return ruleGroup;
        } catch (Exception e) {
            log.error("Failed to create rule group: {}", e.getMessage());
            return null;
        }
    }

    private FmsRule saveToDB(String ruleName, String ruleContent) {
        try {
            // Check if rule already exists
            FmsRule existingRule = fmsRuleRepository.findAll().stream()
                    .filter(r -> r.getRuleName().equals(ruleName))
                    .findFirst()
                    .orElse(null);

            if (existingRule != null) {
                log.info("Rule already exists in database, skipping: {}", ruleName);
                return existingRule;
            }

            PaymentNetwork defaultNetwork = paymentNetworkRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No payment network found"));

            FmsRule rule = new FmsRule();
            rule.setRuleUuid(UUID.randomUUID().toString());
            rule.setRuleName(ruleName);
            rule.setDescription("AI Generated Rule");
            rule.setStatus("ACTIVE");
            rule.setFinalRiskScore(7.0);
            rule.setFinalRule(ruleContent);
            rule.setPaymentNetworkId(defaultNetwork);
            rule.setCreatedAt(new Date());
            rule.setUpdatedAt(new Date());
            rule.setCreatedBy("AI_SYSTEM");
            rule.setUpdatedBy("AI_SYSTEM");

            rule = fmsRuleRepository.save(rule);
            log.info("Saved AI rule to database: {}", ruleName);
            return rule;
        } catch (Exception e) {
            log.error("Failed to save rule {}: {}", ruleName, e.getMessage());
            return null;
        }
    }
    
    private void addRulesToGroup(RuleGroup ruleGroup, List<FmsRule> rules) {
        try {
            for (FmsRule rule : rules) {
                // Check if rule already in group
                boolean exists = ruleGroupRuleRepository.findAll().stream()
                        .anyMatch(rgr -> rgr.getRuleGroupId().getRuleGroupId().equals(ruleGroup.getRuleGroupId()) 
                                && rgr.getFmsRuleId().getFmsRuleId().equals(rule.getFmsRuleId()));
                
                if (exists) {
                    log.info("Rule already in group, skipping: {}", rule.getRuleName());
                    continue;
                }
                
                RuleGroupRule ruleGroupRule = new RuleGroupRule();
                ruleGroupRule.setRuleGroupId(ruleGroup);
                ruleGroupRule.setFmsRuleId(rule);
                ruleGroupRule.setStatus("ACTIVE");
                ruleGroupRule.setCreatedAt(new Date());
                ruleGroupRule.setUpdatedAt(new Date());
                ruleGroupRule.setCreatedBy("AI_SYSTEM");
                ruleGroupRule.setUpdatedBy("AI_SYSTEM");
                
                ruleGroupRuleRepository.save(ruleGroupRule);
            }
            log.info("Added {} rules to group: {}", rules.size(), ruleGroup.getGroupName());
        } catch (Exception e) {
            log.error("Failed to add rules to group: {}", e.getMessage());
        }
    }

    private void deployToKieBase(String rulesContent) {
        try {
            String rulesPath = "fms_core/src/main/resources/rules/ai-generated-rules.drl";
            java.io.File file = new java.io.File(rulesPath);
            
            // Check if file already exists
            if (file.exists()) {
                log.info("AI rules file already exists at {}, skipping file creation", rulesPath);
                return;
            }
            
            file.getParentFile().mkdirs();
            
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(rulesContent);
            }
            log.info("Successfully saved AI rules to {}", rulesPath);
            log.info("Restart application to load new rules into KieBase");
        } catch (Exception e) {
            log.error("Failed to save rules file: {}", e.getMessage(), e);
        }
    }
}

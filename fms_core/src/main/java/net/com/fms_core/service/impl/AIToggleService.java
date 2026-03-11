package net.com.fms_core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.entity.AIRule;
import net.com.fms_core.entity.AIToggleConfig;
import net.com.fms_core.repository.AIRuleRepository;
import net.com.fms_core.repository.AIToggleConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIToggleService {
    
    private final AIToggleConfigRepository aiToggleConfigRepository;
    private static final String CONFIG_KEY = "USE_AI_RULES";
    private final AIRuleRepository aiRuleRepository;
    private final AIKieService aiKieService;
    
    public boolean isAIRulesEnabled() {
        return aiToggleConfigRepository.findByConfigKey(CONFIG_KEY)
                .map(AIToggleConfig::getEnabled)
                .orElse(false);
    }
    
    public void setAIRulesEnabled(boolean enabled, String updatedBy) {
        AIToggleConfig config = aiToggleConfigRepository.findByConfigKey(CONFIG_KEY)
                .orElse(new AIToggleConfig());
        
        config.setConfigKey(CONFIG_KEY);
        config.setEnabled(enabled);
        config.setUpdatedBy(updatedBy);
        
        aiToggleConfigRepository.save(config);
        initializeAIRules();
        log.info("AI rules toggle updated: {} by {}", enabled, updatedBy);
    }

    private void initializeAIRules() {
        try {
            List<AIRule> deployedAIRules = aiRuleRepository.findDeployedRulesOrderByPriority();
            if (!deployedAIRules.isEmpty()) {
                aiKieService.deployAIRules(deployedAIRules);
                System.out.println("Initialized AI KIE base with " + deployedAIRules.size() + " deployed AI rules");
            } else {
                System.out.println("No deployed AI rules found - AI KIE base initialized with default rule");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize AI rules: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

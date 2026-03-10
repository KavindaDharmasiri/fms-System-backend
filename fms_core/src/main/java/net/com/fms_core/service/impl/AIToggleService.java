package net.com.fms_core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.entity.AIToggleConfig;
import net.com.fms_core.repository.AIToggleConfigRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIToggleService {
    
    private final AIToggleConfigRepository aiToggleConfigRepository;
    private static final String CONFIG_KEY = "USE_AI_RULES";
    
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
        log.info("AI rules toggle updated: {} by {}", enabled, updatedBy);
    }
}
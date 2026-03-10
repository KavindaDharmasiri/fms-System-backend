package net.com.fms_core.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.AIRule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIKieService {
    
    private KieContainer aiKieContainer;
    private final KieServices kieServices = KieServices.Factory.get();
    
    @PostConstruct
    public void initializeAIKieBase() {
        try {
            // Create separate KIE base for AI rules
            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
            
            // Add default AI rule template
            String defaultRule = """
                package net.com.fms_core.ai.rules;
                import net.com.fms_core.dto.message.IsoMessageDTO;
                
                rule "AI_DEFAULT_RULE"
                when
                    $transaction : IsoMessageDTO()
                then
                    // Default AI rule - no action
                end
                """;
            
            kieFileSystem.write("src/main/resources/ai-rules/default.drl", defaultRule);
            
            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
            kieBuilder.buildAll();
            
            if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
                log.error("AI KIE build errors: {}", kieBuilder.getResults().getMessages());
                throw new RuntimeException("Failed to build AI KIE base");
            }
            
            aiKieContainer = kieServices.newKieContainer(kieBuilder.getKieModule().getReleaseId());
            log.info("AI KIE base initialized successfully");
            
        } catch (Exception e) {
            log.error("Failed to initialize AI KIE base", e);
            throw new RuntimeException("AI KIE initialization failed", e);
        }
    }
    
    public void deployAIRules(List<AIRule> aiRules) {
        try {
            // Clear existing AI KIE container
            if (aiKieContainer != null) {
                aiKieContainer.dispose();
                aiKieContainer = null;
                log.info("Cleared existing AI KIE container");
            }
            
            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
            
            // Add each AI rule to the KIE file system
            for (int i = 0; i < aiRules.size(); i++) {
                AIRule rule = aiRules.get(i);
                String fileName = String.format("src/main/resources/ai-rules/rule_%d.drl", i);
                kieFileSystem.write(fileName, rule.getDroolRule());
            }
            
            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
            kieBuilder.buildAll();
            
            if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
                log.error("AI rules build errors: {}", kieBuilder.getResults().getMessages());
                throw new RuntimeException("Failed to build AI rules");
            }
            
            // Create new AI KIE container with fresh rules
            aiKieContainer = kieServices.newKieContainer(kieBuilder.getKieModule().getReleaseId());
            log.info("Deployed {} AI rules successfully", aiRules.size());
            
        } catch (Exception e) {
            log.error("Failed to deploy AI rules", e);
            throw new RuntimeException("AI rules deployment failed", e);
        }
    }
    
    public IsoMessageDTO evaluateWithAIRules(IsoMessageDTO transaction) {
        if (aiKieContainer == null) {
            log.warn("AI KIE container not initialized");
            return transaction;
        }
        
        try {
            List<String> aiFiredRules = new ArrayList<>();
            KieSession kieSession = aiKieContainer.newKieSession();
            
            kieSession.addEventListener(new DefaultAgendaEventListener() {
                @Override
                public void afterMatchFired(AfterMatchFiredEvent event) {
                    String ruleName = event.getMatch().getRule().getName();
                    aiFiredRules.add(ruleName);
                    System.out.println("AI rule fired: " + ruleName);
                }
            });
            
            kieSession.insert(transaction);
            int firedRules = kieSession.fireAllRules();
            System.out.println("Number of AI rules fired: " + firedRules);
            kieSession.dispose();
            
            // Add AI fired rules to transaction
            if (!aiFiredRules.isEmpty()) {
                if (transaction.getFiredRules() == null) {
                    transaction.setFiredRules(new ArrayList<>());
                }
                transaction.getFiredRules().addAll(aiFiredRules);
            }
            
            return transaction;
            
        } catch (Exception e) {
            log.error("Error evaluating transaction with AI rules", e);
            return transaction;
        }
    }
    
    public boolean isAIKieBaseReady() {
        return aiKieContainer != null;
    }
    
    public void clearAIRules() {
        if (aiKieContainer != null) {
            aiKieContainer.dispose();
            aiKieContainer = null;
            log.info("AI KIE container cleared");
        }
        // Reinitialize with default rule
        initializeAIKieBase();
    }
}

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.client.NotificationClient;
import net.com.fms_core.controller.TransactionController;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.TransactionEvaluationResult;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.RiskMetrix;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.entity.FmsRule;
import net.com.fms_core.entity.RuleGroupRule;
import net.com.fms_core.entity.TransactionFlaggedRules;
import net.com.fms_core.repository.FmsRuleRepository;
import net.com.fms_core.repository.RiskMetrixRepository;
import net.com.fms_core.repository.RuleGroupRuleRepository;
import net.com.fms_core.repository.TransactionFlaggedRulesRepository;
import net.com.fms_core.repository.TransactionRepository;
import net.com.fms_core.service.DynamicDroolsService;
import org.kie.api.KieBase;
import org.kie.api.builder.Results;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service("dynamicDroolsService")
@RequiredArgsConstructor
public class DynamicDroolsServiceImpl implements DynamicDroolsService {
    private KieBase kieBase;
    private KieBase kieBase2;
    private final TransactionRepository transactionHistoryRepository;
    private final TransactionController transactionController;
    private final RiskMetrixRepository riskMetrixRepository;
    private final NotificationClient notificationClient;
    private final FmsRuleRepository fmsRuleRepository;
    private final RuleGroupRuleRepository ruleGroupRuleRepository;
    private final TransactionFlaggedRulesRepository transactionFlaggedRulesRepository;
    @Override
    public KieBase loadRulesFromStringList(List<String> rules) {
        try {
            long startTime = System.currentTimeMillis();
            KieHelper kieHelper = new KieHelper();
            for (String rule : rules) {
                kieHelper.addContent(rule, ResourceType.DRL);
            }
            Results results = kieHelper.verify();
            if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
                throw new IllegalStateException("Rule compilation errors: " + results.getMessages());
            }
            this.kieBase = kieHelper.build();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Execution time: LOAD RULES TO KIA " + rules.size() + " - " + duration + " milliseconds");
            return this.kieBase;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public KieBase loadRulesFromStringList2(List<String> rules) {
        try {
            long startTime = System.currentTimeMillis();
            KieHelper kieHelper = new KieHelper();
            for (String rule : rules) {
                kieHelper.addContent(rule, ResourceType.DRL);
            }
            Results results = kieHelper.verify();
            if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
                throw new IllegalStateException("Rule compilation errors: " + results.getMessages());
            }
            this.kieBase2 = kieHelper.build();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Execution time: LOAD RULES TO KIA " + rules.size() + " - " + duration + " milliseconds");
            return this.kieBase2;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public IsoMessageDTO evaluateTransaction(IsoMessageDTO transaction) {
        transaction.setRiskLevel("LOW");
        List<String> firedRuleNames = new ArrayList<>();
        KieSession kieSession = kieBase.newKieSession();
        kieSession.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                String ruleName = event.getMatch().getRule().getName();
                firedRuleNames.add(ruleName);
                System.out.println("Rule fired: " + event.getMatch().getRule());
                System.out.println("Rule fired name: " + ruleName);
            }
        });
        kieSession.insert(transaction);
        int firedRules = kieSession.fireAllRules();
        System.out.println("Number of fired rules: " + firedRules);
        
        // Merge with existing fired rules from distance check
        if (transaction.getFiredRules() != null) {
            firedRuleNames.addAll(0, transaction.getFiredRules());
        }
        transaction.setFiredRules(firedRuleNames);
        System.out.println(transaction.getFiredRules());
        
        // Set block reason only if not already set by distance check
        if (!firedRuleNames.isEmpty() && transaction.getBlockReason() == null) {
            transaction.setBlockReason(firedRuleNames.contains("Impossible Distance") ? "Impossible Distance" : "Blocked by Rules");
        }
        
        kieSession.dispose();
        saveTranHistory(transaction);
        return transaction;
    }
    @Override
    public IsoMessageDTO evaluateTransaction2(IsoMessageDTO transaction) {
        System.out.println("getRulesCount(kieBase) = " + getRulesCount(kieBase2));
        transaction.setRuleFired(false);
        KieSession kieSession = kieBase2.newKieSession();
        kieSession.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                transaction.setRuleFired(true);
            }
        });
        kieSession.insert(transaction);
        int firedRules = kieSession.fireAllRules();
        System.out.println("Number of fired rules: " + firedRules);
        kieSession.dispose();
        return transaction;
    }
    @Override
    public TransactionEvaluationResult evaluateTransactionWithRules(IsoMessageDTO transaction) {
        transaction.setRuleFired(false);
        System.out.println("getRulesCount(kieBase) = " + getRulesCount(kieBase2));
        List<String> firedRuleNames = new ArrayList<>();
        KieSession kieSession = kieBase2.newKieSession();
        kieSession.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                String ruleName = event.getMatch().getRule().getName();
                firedRuleNames.add(ruleName);
            }
        });
        kieSession.insert(transaction);
        int firedRules = kieSession.fireAllRules();
        System.out.println("Number of fired rules: " + firedRules);
        kieSession.dispose();
        return new TransactionEvaluationResult(transaction, firedRuleNames);
    }
    private void saveTranHistory(IsoMessageDTO evaluatedTxn) {
        try {
            System.out.println(evaluatedTxn.getRiskScore()+" : risk score");
            System.out.println(evaluatedTxn.getFraudPercentage()+" : fraud percentage");

            if (evaluatedTxn.getFraudPercentage() == null) {
                evaluatedTxn.setFraudPercentage(0.0);
            }

            // Keep risk level from rules if already set to HIGH
            String finalRiskLevel = evaluatedTxn.getRiskLevel();
            
            // Only override if fraud percentage is significant
            if (evaluatedTxn.getFraudPercentage() > 40) {
                finalRiskLevel = "HIGH";
            } else if (evaluatedTxn.getFraudPercentage() > 30) {
                finalRiskLevel = "MID";
            } else if (finalRiskLevel == null || finalRiskLevel.equals("LOW")) {
                // Use risk matrix only if no rule fired
                RiskMetrix byRiskValue = riskMetrixRepository.findByRiskValue(evaluatedTxn.getFraudPercentage());
                finalRiskLevel = byRiskValue != null ? byRiskValue.getFlag() : "LOW";
            }
            
            evaluatedTxn.setRiskLevel(finalRiskLevel);
            sendRiskNotification(evaluatedTxn, finalRiskLevel);

            System.out.println("Saving transaction history...");
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setCreatedBy("admin");
            transactionHistory.setStatus(finalRiskLevel);
            transactionHistory.setFraudPercentage(evaluatedTxn.getFraudPercentage());
            
            // Set default action_status based on risk level if no rules fired
            String defaultActionStatus = "ALLOWED";
            if ("HIGH".equals(finalRiskLevel)) {
                defaultActionStatus = "BLOCKED";
            } else if ("MID".equals(finalRiskLevel)) {
                defaultActionStatus = "REVIEW";
            }
            transactionHistory.setActionStatus(defaultActionStatus);

            System.out.println(evaluatedTxn.getBlockReason());
            transactionHistory.setTranPacket(getDtoAsJson(evaluatedTxn));
            transactionHistory.setTranUuid(UUID.randomUUID().toString());
            transactionHistory.setUpdatedBy("admin");
            transactionHistory.setBlockReason(evaluatedTxn.getBlockReason());
            TransactionHistory save = transactionHistoryRepository.save(transactionHistory);
            
            // Save fired rules
            if (evaluatedTxn.getFiredRules() != null && !evaluatedTxn.getFiredRules().isEmpty()) {
                List<String> triggeredActions = new ArrayList<>();
                String finalActionStatus = save.getActionStatus();
                
                for (String ruleName : evaluatedTxn.getFiredRules()) {
                    FmsRule fmsRule = fmsRuleRepository.findByRuleName(ruleName);
                    if (fmsRule != null) {
                        RuleGroupRule ruleGroupRule = ruleGroupRuleRepository.findByFmsRuleId(fmsRule);
                        
                        if (ruleGroupRule != null && ruleGroupRule.getRuleGroupId() != null 
                            && ruleGroupRule.getRuleGroupId().getReactionTemplateId() != null) {
                            
                            var reactionTemplate = ruleGroupRule.getRuleGroupId().getReactionTemplateId();
                            String reactionStatus = reactionTemplate.getStatus();
                            
                            if (!triggeredActions.contains(reactionTemplate.getTemplateName())) {
                                triggeredActions.add(reactionTemplate.getTemplateName());
                            }
                            
                            // Determine action priority: BLOCKED > REVIEW > ALLOWED
                            boolean shouldUpdate = false;
                            if ("BLOCKED".equalsIgnoreCase(reactionStatus) || "BLOCK".equalsIgnoreCase(reactionStatus)) {
                                finalActionStatus = "BLOCKED";
                                shouldUpdate = true;
                            } else if (("REVIEW".equalsIgnoreCase(reactionStatus) || "SEND_MESSAGE".equalsIgnoreCase(reactionStatus)) 
                                       && !"BLOCKED".equals(finalActionStatus)) {
                                finalActionStatus = "REVIEW";
                                shouldUpdate = true;
                            } else if (save.getReactionTemplateName() == null) {
                                // If no template set yet, use this one
                                shouldUpdate = true;
                            }
                            
                            // Copy reaction template data
                            if (shouldUpdate) {
                                save.setReactionTemplateName(reactionTemplate.getTemplateName());
                                save.setSmsEnabled(reactionTemplate.getSmsEnabled());
                                save.setEmailEnabled(reactionTemplate.getEmailEnabled());
                                save.setFrmEnabled(reactionTemplate.getFrmEnabled());
                            }
                        }
                        
                        TransactionFlaggedRules flaggedRule = new TransactionFlaggedRules();
                        flaggedRule.setTransactionHistoryId(save);
                        flaggedRule.setFmsRuleId(fmsRule);
                        flaggedRule.setRuleGroupId(ruleGroupRule != null ? ruleGroupRule.getRuleGroupId() : null);
                        flaggedRule.setStatus("FLAGGED");
                        flaggedRule.setRiskScore(fmsRule.getFinalRiskScore());
                        flaggedRule.setFlag(finalRiskLevel);
                        flaggedRule.setCreatedBy("admin");
                        flaggedRule.setUpdatedBy("admin");
                        flaggedRule.setCreatedAt(new java.util.Date());
                        flaggedRule.setUpdatedAt(new java.util.Date());
                        transactionFlaggedRulesRepository.save(flaggedRule);
                    }
                }
                
                save.setActionStatus(finalActionStatus);
                transactionHistoryRepository.save(save);
                evaluatedTxn.setTriggeredActions(triggeredActions);
            }
            
            transactionController.publish(save);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRiskNotification(IsoMessageDTO transaction, String riskScore) {
        try {
            String transactionId = String.valueOf(transaction.getStan());
            String amount = String.valueOf(transaction.getAmount());
            String cardNumber = transaction.getPan();
            System.out.println(riskScore);
            System.out.println(riskScore.equals("MID"));
            if (riskScore.equals("HIGH")) {
                // High risk - immediate notification
                notificationClient.sendHighRiskAlert(transactionId, String.valueOf(riskScore), amount, cardNumber);
                log.warn("HIGH RISK transaction detected: ID={}, Score={}", transactionId, riskScore);
            } else if (riskScore.equals("MID")) {
                // Medium risk - notification for review
                notificationClient.sendMediumRiskAlert(transactionId, String.valueOf(riskScore), amount, cardNumber);
                log.info("MEDIUM RISK transaction detected: ID={}, Score={}", transactionId, riskScore);
            }
        } catch (Exception e) {
            log.error("Failed to send risk notification: {}", e.getMessage());
        }
    }

    public String getDtoAsJson(IsoMessageDTO evaluatedTxn) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(evaluatedTxn);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert DTO to JSON", e);
        }
    }
    public static int getRulesCount(KieBase kieBase) {
        int count = 0;
        for (org.kie.api.definition.KiePackage kiePackage : kieBase.getKiePackages()) {
            for (Rule rule : kiePackage.getRules()) {
                count++;
            }
        }
        return count;
    }
}

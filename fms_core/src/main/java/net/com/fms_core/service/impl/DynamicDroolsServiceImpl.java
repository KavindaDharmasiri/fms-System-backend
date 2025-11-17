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
import net.com.fms_core.repository.RiskMetrixRepository;
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
        System.out.println("getRulesCount(kieBase) = " + getRulesCount(kieBase));
        System.out.println(transaction.getAmount());
        transaction.setRiskLevel("LOW");
        System.out.println(transaction.getFiredRules());
        KieSession kieSession = kieBase.newKieSession();
        kieSession.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                System.out.println("Rule fired: " + event.getMatch().getRule());
                System.out.println("Rule fired name: " + event.getMatch().getRule().getName());
            }
        });
        kieSession.insert(transaction);
        int firedRules = kieSession.fireAllRules();
        System.out.println("Number of fired rules: " + firedRules);
        System.out.println(transaction.getFiredRules());
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
            System.out.println(evaluatedTxn.getRiskScore());
            RiskMetrix byRiskValue = riskMetrixRepository.findByRiskValue(evaluatedTxn.getRiskScore());
            evaluatedTxn.setRiskLevel(byRiskValue.getFlag());
            if(!byRiskValue.getFlag().equals("LOW")){
                sendRiskNotification(evaluatedTxn, byRiskValue.getFlag());
            }
            System.out.println("Saving transaction history...");
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setCreatedBy("admin");
            transactionHistory.setStatus(evaluatedTxn.getRiskLevel());
            transactionHistory.setTranPacket(getDtoAsJson(evaluatedTxn));
            transactionHistory.setTranUuid(UUID.randomUUID().toString());
            transactionHistory.setUpdatedBy("admin");
            TransactionHistory save = transactionHistoryRepository.save(transactionHistory);
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

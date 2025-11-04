/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.controller.RiskController;
import net.com.fms_core.dto.ApiPageReqDTO;
import net.com.fms_core.dto.FmsRuleConditionDTO;
import net.com.fms_core.dto.FmsRuleDTO;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.*;
import net.com.fms_core.enums.OperatorSymbol;
import net.com.fms_core.repository.*;
import net.com.fms_core.service.DynamicDroolsService;
import net.com.fms_core.service.FmsRuleService;
import net.com.fms_core.service.MyAsyncService;
import net.com.fms_core.util.ApiCommonMethod;
import net.com.fms_core.util.mapping.FmsRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service("FmsRuleService")
public class FmsRuleServiceIMPL implements FmsRuleService {
    private final FmsRuleRepository FmsRuleRepository;
    private final FmsRuleConditionRepository FmsRuleConditionRepository;
    private final FmsRuleMapper FmsRuleMapper;
    private final FmsElementRepository FmsElementRepository;
    private final RuleGroupRuleRepository ruleGroupRuleRepository;
    private final TransactionRepository transactionRepository;
    private final RuleGroupRepository ruleGroupRepository;
    private final MyAsyncService myAsyncService;
    private final DynamicDroolsService droolsService;
    private final RiskMetrixRepository riskMetrixRepository;
    private final RiskController riskController;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PaymentNetworkRepository paymentNetworkRepository;
    @Override
    public FmsRuleDTO saveRule(FmsRuleDTO FmsRuleDTO) {
        try {
            FmsRule FmsRule = FmsRuleMapper.mapFmsRuleDTOToFmsRuleForSave(FmsRuleDTO);
            Collection<FmsRuleCondition> FmsRuleConditions = FmsRule.getFmsRuleConditionCollection();
            FmsRule.setFmsRuleConditionCollection(null);
            String ruleFinal = "";
            Optional<PaymentNetwork> paymentNet = paymentNetworkRepository.findById(FmsRuleDTO.getPaymentNetworkId());
            for (FmsRuleConditionDTO rule : FmsRuleDTO.getFmsRuleConditionCollection()) {
                Optional<FmsElement> byId = FmsElementRepository.findById(rule.getFmsElementId());
                String symbol = OperatorSymbol.getSymbolByCode(rule.getOperator());
                if (symbol.equals("==") || symbol.equals("!=")) {
                    ruleFinal += byId.get().getVariableName() +" "+ symbol + " \"" + rule.getValue() + "\", ";
                } else {
                    ruleFinal += byId.get().getVariableName() +" "+ symbol + " " + rule.getValue() + ", ";
                }
            }
            if (ruleFinal.endsWith(", ")) {
                ruleFinal = ruleFinal.substring(0, ruleFinal.length() - 2);
            }
            RiskMetrix byRiskValue = riskMetrixRepository.findByRiskValue(FmsRuleDTO.getFinalRiskScore());
            FmsRule.setFinalRule("import net.com.fms_core.dto.message.IsoMessageDTO;\n" + "rule \"" + FmsRuleDTO.getRuleName() + "\"\n" + "when\n" + "$t : IsoMessageDTO(" + ruleFinal  +")\n" + "then\n" + "    $t.setRiskLevel(\""+byRiskValue.getFlag()+"\");\n" + "    $t.setFlaggedForReview(true);\n" +"    $t.setRuleFired(true);\n"+
                    "    $t.setRuleName(\"" + FmsRuleDTO.getRuleName() + "\");\n" +
                    "    $t.setFiredRule(\"" + "--" + "--" + "\");\n" +
                    "    $t.setRiskScore("  + FmsRuleDTO.getFinalRiskScore() + ");\n"+
                    "    $t.setPaymentNetwork(\"" + paymentNet.get().getNetworkName() + "\");\n" +
                    "end");
            FmsRule savedFmsRule = FmsRuleRepository.save(FmsRule);
            FmsRuleConditions.stream().forEach(FmsRuleCondition -> {
                FmsRuleCondition.setFmsRuleId(savedFmsRule);
            });
            FmsRuleConditionRepository.saveAll(FmsRuleConditions);
            return FmsRuleMapper.mapBasicFmsRuleToFmsRuleDTOForGet(savedFmsRule);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    public FmsRuleDTO updateRule(FmsRuleDTO FmsRuleDTO) {
        try {
            FmsRuleConditionRepository.removeAllByfmsRuleId_fmsRuleId(FmsRuleDTO.getFmsRuleId());
            FmsRule existingFmsRule = FmsRuleRepository.findById(FmsRuleDTO.getFmsRuleId()).orElseThrow();
            FmsRuleMapper.mapFmsRuleDTOToFmsRuleForUpdate(FmsRuleDTO, existingFmsRule);
            existingFmsRule.setFmsRuleConditionCollection(new ArrayList<>());
            String ruleFinal = "";
            Optional<PaymentNetwork> paymentNet = paymentNetworkRepository.findById(FmsRuleDTO.getPaymentNetworkId());
            for (FmsRuleConditionDTO rule : FmsRuleDTO.getFmsRuleConditionCollection()) {
                Optional<FmsElement> byId = FmsElementRepository.findById(rule.getFmsElementId());
                String symbol = OperatorSymbol.getSymbolByCode(rule.getOperator());
                if (symbol.equals("==") || symbol.equals("!=")) {
                    ruleFinal += byId.get().getVariableName() +" "+ symbol + " \"" + rule.getValue() + "\", ";
                } else {
                    ruleFinal += byId.get().getVariableName() +" "+ symbol + " " + rule.getValue() + ", ";
                }
                FmsRuleCondition FmsRuleCondition = new FmsRuleCondition();
                FmsRuleCondition.setFmsRuleId(existingFmsRule);
                FmsRuleCondition.setFmsElementId(byId.get());
                FmsRuleCondition.setOperator(rule.getOperator());
                FmsRuleCondition.setValue(rule.getValue());
                FmsRuleCondition.setCreatedAt(new Date());
                FmsRuleCondition.setUpdatedAt(new Date());
                FmsRuleConditionRepository.save(FmsRuleCondition);
            }
            if (ruleFinal.endsWith(", ")) {
                ruleFinal = ruleFinal.substring(0, ruleFinal.length() - 2);
            }
            RiskMetrix byRiskValue = riskMetrixRepository.findByRiskValue(FmsRuleDTO.getFinalRiskScore());
            existingFmsRule.setFinalRule("import net.com.fms_core.dto.message.IsoMessageDTO;\n" + "rule \"" + FmsRuleDTO.getRuleName() + "\"\n" + "when\n" + "$t : IsoMessageDTO(" + ruleFinal  +")\n" + "then\n" + "    $t.setRiskLevel(\""+byRiskValue.getFlag()+"\");\n" + "    $t.setFlaggedForReview(true);\n" +"    $t.setRuleFired(true);\n" +"    $t.setRuleName("+FmsRuleDTO.getRuleName()+");\n"+ "end");
            existingFmsRule.setFinalRule("import net.com.fms_core.dto.message.IsoMessageDTO;\n" + "rule \"" + FmsRuleDTO.getRuleName() + "\"\n" + "when\n" + "$t : IsoMessageDTO(" + ruleFinal  +")\n" + "then\n" + "    $t.setRiskLevel(\""+byRiskValue.getFlag()+"\");\n" + "    $t.setFlaggedForReview(true);\n" +"    $t.setRuleFired(true);\n"+
                    "    $t.setRuleName(\"" + FmsRuleDTO.getRuleName() + "\");\n" +
                    "    $t.setFiredRule(\"" + "--" + "--" + "\");\n" +
                    "    $t.setRiskScore(" + FmsRuleDTO.getFinalRiskScore() + ");\n"+
                    "    $t.setPaymentNetwork(\"" + paymentNet.get().getNetworkName() + "\");\n" +
                    "end");
            FmsRule savedFmsRule = FmsRuleRepository.save(existingFmsRule);
            return FmsRuleMapper.mapBasicFmsRuleToFmsRuleDTOForGet(savedFmsRule);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    public FmsRuleDTO getRule(Integer fmsRuleId) {
        try {
            FmsRule existingFmsRule = FmsRuleRepository.findById(fmsRuleId).orElseThrow();
            return FmsRuleMapper.mapFmsRuleToFmsRuleDTOForGetWithConditions(existingFmsRule);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    public Page<FmsRuleDTO> filterFmsRules(FmsRuleDTO FmsRuleDTO, ApiPageReqDTO pageable) {
        try {
            Page<FmsRule> FmsRulesPage = FmsRuleRepository.filterFmsRules(FmsRuleDTO.getFmsRuleId(), FmsRuleDTO.getRuleUuid(), FmsRuleDTO.getRuleName(), FmsRuleDTO.getDescription(), FmsRuleDTO.getFromDate(), FmsRuleDTO.getToDate(), FmsRuleDTO.getStatus(), FmsRuleDTO.getFinalRiskScore(), FmsRuleDTO.getFinalRule(), FmsRuleDTO.getPaymentNetworkId(), ApiCommonMethod.pageableFrom(pageable, "created_at"));
            return FmsRuleMapper.mapBasicBranchPageToBranchDTOPageForGet(FmsRulesPage);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    public List<String> getFinalRules() {
        try {
            return ruleGroupRepository.findAllByStatus("ACTIVE").stream()
                    .flatMap(ruleGroup -> ruleGroup.getRuleGroupRuleCollection().stream()
                            .filter(ruleGroupRule ->
                                    ruleGroupRule.getFmsRuleId() != null &&
                                            ruleGroupRule.getFmsRuleId().getFmsRuleId() != null &&
                                            ruleGroupRule.getFmsRuleId().getFmsRuleId() != 0 &&
                                            "ACTIVE".equals(ruleGroupRule.getFmsRuleId().getStatus())
                            )
                            .map(ruleGroupRule -> ruleGroupRule.getFmsRuleId().getFinalRule())
                    )
                    .filter(rule -> rule != null && !rule.trim().isEmpty())
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list instead of null
        }
    }
    @Override
    public SseEmitter testRule(FmsRuleDTO FmsRuleDTO) {
        String s = setRules(FmsRuleDTO);
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> {
            try {
                if (s.equals("Rule loaded successfully.")) {
                    List<IsoMessageDTO> transactionList = transactionRepository.findAll().stream()
                            .filter(t -> t.getTranPacket() != null)
                            .map(t -> {
                                IsoMessageDTO dto = convertJsonToDto(t.getTranPacket());
                                dto.setId(t.getTransactionHistoryId()); // assuming `getName()` exists on your entity
                                dto.setUUID(t.getTranUuid()); // assuming `getName()` exists on your entity
                                dto.setTimestamp(t.getCreatedAt()); // assuming `getTimestamp()` exists on your entity
                                return dto;
                            })
                            .collect(Collectors.toList());
                    long startTime = System.currentTimeMillis();
                    for (IsoMessageDTO txn : transactionList) {
                        IsoMessageDTO evaluatedTxn = droolsService.evaluateTransaction2(txn);
                        emitter.send(SseEmitter.event()
                                .name("loopEvent")
                                .data(evaluatedTxn));
                        Thread.sleep(300); // 3-second gap
                    }
                    long endTime = System.currentTimeMillis();
                    System.out.println("All Tasks completed. Execution time: " + (endTime - startTime) + " milliseconds");
                    emitter.complete();
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start(); // must run this in a separate thread to avoid blocking
        return emitter;
    }
    private String setRules(FmsRuleDTO FmsRuleDTO) {
        try {
            FmsRule FmsRule = FmsRuleMapper.mapFmsRuleDTOToFmsRuleForSave(FmsRuleDTO);
            Collection<FmsRuleCondition> FmsRuleConditions = FmsRule.getFmsRuleConditionCollection();
            FmsRule.setFmsRuleConditionCollection(null);
            String ruleFinal = "";
            for (FmsRuleConditionDTO rule : FmsRuleDTO.getFmsRuleConditionCollection()) {
                Optional<FmsElement> byId = FmsElementRepository.findById(rule.getFmsElementId());
                String symbol = OperatorSymbol.getSymbolByCode(rule.getOperator());
                if (symbol.equals("==") || symbol.equals("!=")) {
                    ruleFinal += byId.get().getVariableName() +" "+ symbol + " \"" + rule.getValue() + "\", ";
                } else {
                    ruleFinal += byId.get().getVariableName() +" "+ symbol + " " + rule.getValue() + ", ";
                }
            }
            if (ruleFinal.endsWith(", ")) {
                ruleFinal = ruleFinal.substring(0, ruleFinal.length() - 2);
            }
            FmsRule.setFinalRule("import net.com.fms_core.dto.message.IsoMessageDTO;\n" + "rule \"" + FmsRuleDTO.getRuleName() + "\"\n" + "when\n" + "$t : IsoMessageDTO(" + ruleFinal  +")\n" + "then\n" + "    $t.setRiskLevel(\"HIGH\");\n" + "    $t.setFlaggedForReview(true);\n" +"    $t.setRuleFired(true);\n" +"    $t.setRuleName(\"" + FmsRuleDTO.getRuleName() + "\");\n"+ "end");
            System.out.println("------------------------------------------------");
            System.out.println(ruleFinal);
            RiskMetrix byRiskValue = riskMetrixRepository.findByRiskValue(FmsRuleDTO.getFinalRiskScore());
            String newRule = "import net.com.fms_core.dto.message.IsoMessageDTO;\n" + "rule \"" + FmsRuleDTO.getRuleName() + "\"\n" + "when\n" + "$t : IsoMessageDTO(" + ruleFinal + ")\n" + "then\n" + "    $t.setRiskLevel(\""+byRiskValue.getFlag()+"\");\n" + "    $t.setFlaggedForReview(true);\n" + "$t.setRuleFired(true);"+"    $t.setRuleName(\"" + FmsRuleDTO.getRuleName() + "\");\n"+ "end";
            System.out.println(newRule);
            long startTime = System.currentTimeMillis();
            List<String> rulesList = new ArrayList<>();
            rulesList.add(newRule);
            droolsService.loadRulesFromStringList2(rulesList);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Rule loaded. Execution time: SET RULES " + duration + " milliseconds");
            return "Rule loaded successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public IsoMessageDTO convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, IsoMessageDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return empty list instead of null
        }
    }
}

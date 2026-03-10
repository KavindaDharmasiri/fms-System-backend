/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.controller.RiskController;
import net.com.fms_core.dto.ApiPageReqDTO;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.RuleGroupDTO;
import net.com.fms_core.dto.RuleGroupRuleDTO;
import net.com.fms_core.dto.TransactionEvaluationResult;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.FmsRule;
import net.com.fms_core.entity.RuleGroup;
import net.com.fms_core.entity.RuleGroupRole;
import net.com.fms_core.entity.RuleGroupRule;
import net.com.fms_core.repository.*;
import net.com.fms_core.service.DynamicDroolsService;
import net.com.fms_core.service.MyAsyncService;
import net.com.fms_core.service.RuleGroupService;
import net.com.fms_core.util.ApiCommonMethod;
import net.com.fms_core.util.mapping.RuleGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service("ruleGroupService")
public class RuleGroupServiceIMPL implements RuleGroupService {
    private final RuleGroupRepository ruleGroupRepository;
    private final RuleGroupRoleRepository ruleGroupRoleRepository;
    private final RuleGroupRuleRepository ruleGroupRuleRepository;
    private final FmsRuleRepository ruleRepository;
    private final RuleGroupMapper ruleGroupMapper;
    private final DynamicDroolsService droolsService;
    private final FmsElementRepository FmsElementRepository;
    private final MyAsyncService myAsyncService;
    private final RiskController riskController;
    private final TransactionRepository transactionRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public RuleGroupDTO saveRuleGroup(RuleGroupDTO ruleGroupDTO) {
        try {
            String pattern = "setFiredRule\\(\".*?\"\\)";
            Pattern regex = Pattern.compile(pattern);
            RuleGroup ruleGroup = ruleGroupMapper.mapRuleGroupDTOToRuleGroupForSave(ruleGroupDTO);
            Collection<RuleGroupRole> ruleGroupRole = ruleGroup.getRuleGroupRoleCollection();
            Collection<RuleGroupRule> ruleGroupRule = ruleGroup.getRuleGroupRuleCollection();
            ruleGroup.setRuleGroupRoleCollection(null);
            ruleGroup.setRuleGroupRuleCollection(null);
            RuleGroup savedRuleGroup = ruleGroupRepository.save(ruleGroup);
            ruleGroupRole.stream().forEach(ruleGroupRole1 -> {
                ruleGroupRole1.setRuleGroupId(savedRuleGroup);
            });
            ruleGroupRule.stream().forEach(ruleGroupRule1 -> {
                ruleGroupRule1.setRuleGroupId(savedRuleGroup);
            });
            ruleGroupDTO.getRuleGroupRuleCollection().stream().forEach(ruleGroupRule1 -> {
                FmsRule FmsRule = ruleRepository.findById(ruleGroupRule1.getFmsRuleId()).orElseThrow();
                String finalRule = FmsRule.getFinalRule();
                Matcher matcher = regex.matcher(finalRule);
                if (matcher.find()) {
                    String replacement = "setFiredRule(\"" + FmsRule.getRuleName() + "\", \"" + ruleGroupDTO.getGroupName() + "\")";
                    finalRule = matcher.replaceFirst(replacement);
                }
                String updatedFinalRule = finalRule;
                FmsRule.setFinalRule(updatedFinalRule);
                FmsRule save = ruleRepository.save(FmsRule);
            });
            ruleGroupRoleRepository.saveAll(ruleGroupRole);
            ruleGroupRuleRepository.saveAll(ruleGroupRule);
            return ruleGroupMapper.mapBasicRuleGroupToRuleGroupDTOForGet(savedRuleGroup);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    public RuleGroupDTO updateRuleGroup(RuleGroupDTO ruleGroupDTO) {
        try {
            String pattern = "setFiredRule\\(\".*?\"\\)";
            Pattern regex = Pattern.compile(pattern);
            RuleGroup existingRuleGroup = ruleGroupRepository.findById(ruleGroupDTO.getRuleGroupId()).orElseThrow();
            RuleGroup ruleGroup = ruleGroupMapper.mapRuleGroupDTOToRuleGroupForUpdate(ruleGroupDTO, existingRuleGroup);
            ruleGroupDTO.getRuleGroupRuleCollection().stream().forEach(ruleGroupRule1 -> {
                FmsRule FmsRule = ruleRepository.findById(ruleGroupRule1.getFmsRuleId()).orElseThrow();
                String finalRule = FmsRule.getFinalRule();
                Matcher matcher = regex.matcher(finalRule);
                if (matcher.find()) {
                    String replacement = "setFiredRule(\"" + FmsRule.getRuleName() + "\", \"" + ruleGroupDTO.getGroupName() + "\")";
                    finalRule = matcher.replaceFirst(replacement);
                }
                String updatedFinalRule = finalRule;
                FmsRule.setFinalRule(updatedFinalRule);
                ruleRepository.save(FmsRule);
            });
            RuleGroup savedRuleGroup = ruleGroupRepository.save(ruleGroup);
            return ruleGroupMapper.mapBasicRuleGroupToRuleGroupDTOForGet(savedRuleGroup);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    public RuleGroupDTO getRuleGroup(Integer ruleGroupId) {
        try {
            RuleGroup ruleGroup = ruleGroupRepository.findById(ruleGroupId).orElseThrow();
            return ruleGroupMapper.mapBasicRuleGroupToRuleGroupDTOForGetIndividual(ruleGroup);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    public Page<RuleGroupDTO> filterRuleGroups(RuleGroupDTO ruleGroupDTO, ApiPageReqDTO pageable) {
        try {
            if(ruleGroupDTO.getGroupName() != null){
                ruleGroupDTO.setGroupName(ruleGroupDTO.getGroupName().toLowerCase());
            }
            Page<RuleGroup> ruleGroupPage = ruleGroupRepository.filterRuleGroups(ruleGroupDTO.getRuleGroupId(),
                    ruleGroupDTO.getRuleGroupUuid(),
                    ruleGroupDTO.getGroupName(),
                    ruleGroupDTO.getVerdict(),
                    ruleGroupDTO.getFromDate(),
                    ruleGroupDTO.getToDate(),
                    ruleGroupDTO.getStatus(),
                    ruleGroupDTO.getPaymentNetworkId(),
                    ruleGroupDTO.getReactionTemplateId(),
                    ruleGroupDTO.getRuleGroupRoleId(),
                    ruleGroupDTO.getRuleGroupRuleId(), ApiCommonMethod.pageableFrom(pageable, "createdAt"));
            return ruleGroupMapper.mapBasicBranchPageToBranchDTOPageForGet(ruleGroupPage);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
    @Override
    public SseEmitter testRuleGroup(RuleGroupDTO ruleGroupDTO) {
        List<String> rulesList = new ArrayList<>();
        for (RuleGroupRuleDTO ruleGroupRuleDTO : ruleGroupDTO.getRuleGroupRuleCollection()) {
            Optional<FmsRule> byId = ruleRepository.findById(ruleGroupRuleDTO.getFmsRuleId());
            System.out.println(byId.get().getFinalRule());
            rulesList.add(byId.get().getFinalRule());
        }
        String s = setRules(rulesList);
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> {
            try {
                if (s.equals("Rule loaded successfully.")) {
                    List<IsoMessageDTO> transactionList = transactionRepository.findAll().stream().filter(t -> t.getTranPacket() != null).map(t -> convertJsonToDto(t.getTranPacket())).collect(Collectors.toList());
                    long startTime = System.currentTimeMillis();
                    for (IsoMessageDTO txn : transactionList) {
                        TransactionEvaluationResult evaluatedTxn = droolsService.evaluateTransactionWithRules(txn);
                            emitter.send(SseEmitter.event().name("loopEvent").data(evaluatedTxn));
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
    private String setRules(List<String> FmsRule) {
        try {
            droolsService.loadRulesFromStringList2(FmsRule);
            long startTime = System.currentTimeMillis();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Rule loaded. Execution time: SET RULES " + duration + " milliseconds");
            return "Rule loaded successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public IsoMessageDTO convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, IsoMessageDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String deleteRuleGroup(Integer ruleGroupId) {
        try {
            Optional<RuleGroup> ruleGroup = ruleGroupRepository.findById(ruleGroupId);
            if (ruleGroup.isPresent()) {
                ruleGroupRepository.deleteById(ruleGroupId);
                log.info("Rule Group deleted successfully: {}", ruleGroupId);
                return "Rule Group deleted successfully";
            } else {
                throw new RuntimeException("Rule Group not found");
            }
        } catch (Exception ex) {
            log.error("Error deleting rule group: {}", ex.getMessage());
            throw ex;
        }
    }
}

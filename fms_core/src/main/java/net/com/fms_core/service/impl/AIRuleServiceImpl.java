package net.com.fms_core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.*;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.AIRule;
import net.com.fms_core.entity.AIRuleGroup;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.repository.AIRuleGroupRepository;
import net.com.fms_core.repository.AIRuleRepository;
import net.com.fms_core.repository.TransactionRepository;
import net.com.fms_core.service.AIRuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AIRuleServiceImpl implements AIRuleService {
    
    private final AIRuleRepository aiRuleRepository;
    private final AIRuleGroupRepository aiRuleGroupRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final AIKieService aiKieService;
    private final ObjectMapper objectMapper;
    
    @Override
    public ResponseEntity<ApiResponseDTO> saveAIRule(AIRuleDTO aiRuleDTO) {
        try {
            AIRule aiRule = new AIRule();
            if (aiRuleDTO.getAiRuleId() != null && aiRuleDTO.getAiRuleId() != 0) {
                aiRule = aiRuleRepository.findById(aiRuleDTO.getAiRuleId())
                    .orElseThrow(() -> new RuntimeException("AI Rule not found"));
            } else {
                if (aiRuleRepository.existsByRuleCode(aiRuleDTO.getRuleCode())) {
                    return ResponseEntity.status(409).body(ApiResponseDTO.error(
                        ErrorDetailDTO.builder()
                            .code(ErrorCode.INVALID_INPUT)
                            .message("Rule code already exists")
                            .field("ruleCode")
                            .build()));
                }
            }
            
            modelMapper.map(aiRuleDTO, aiRule);
            if (aiRuleDTO.getAiRuleGroupId() != null) {
                AIRuleGroup group = aiRuleGroupRepository.findById(aiRuleDTO.getAiRuleGroupId())
                    .orElseThrow(() -> new RuntimeException("AI Rule Group not found"));
                aiRule.setAiRuleGroup(group);
            }
            
            AIRule saved = aiRuleRepository.save(aiRule);
            return ResponseEntity.ok(ApiResponseDTO.success(saved.getAiRuleId()));
            
        } catch (Exception e) {
            log.error("Error saving AI rule", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to save AI rule")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> saveAIRuleGroup(AIRuleGroupDTO aiRuleGroupDTO) {
        try {
            AIRuleGroup aiRuleGroup = new AIRuleGroup();
            if (aiRuleGroupDTO.getAiRuleGroupId() != null && aiRuleGroupDTO.getAiRuleGroupId() != 0) {
                aiRuleGroup = aiRuleGroupRepository.findById(aiRuleGroupDTO.getAiRuleGroupId())
                    .orElseThrow(() -> new RuntimeException("AI Rule Group not found"));
            } else {
                if (aiRuleGroupRepository.existsByGroupCode(aiRuleGroupDTO.getGroupCode())) {
                    return ResponseEntity.status(409).body(ApiResponseDTO.error(
                        ErrorDetailDTO.builder()
                            .code(ErrorCode.INVALID_INPUT)
                            .message("Group code already exists")
                            .field("groupCode")
                            .build()));
                }
            }
            
            modelMapper.map(aiRuleGroupDTO, aiRuleGroup);
            AIRuleGroup saved = aiRuleGroupRepository.save(aiRuleGroup);
            return ResponseEntity.ok(ApiResponseDTO.success(saved.getAiRuleGroupId()));
            
        } catch (Exception e) {
            log.error("Error saving AI rule group", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to save AI rule group")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> deployAIRulesToProduction(Integer aiRuleGroupId) {
        try {
            AIRuleGroup ruleGroup = aiRuleGroupRepository.findById(aiRuleGroupId)
                .orElseThrow(() -> new RuntimeException("AI Rule Group not found"));
            
            List<AIRule> rules = aiRuleRepository.findByAiRuleGroupAiRuleGroupIdAndStatusTrue(aiRuleGroupId);
            
            if (rules.isEmpty()) {
                return ResponseEntity.status(400).body(ApiResponseDTO.error(
                    ErrorDetailDTO.builder()
                        .code(ErrorCode.INVALID_INPUT)
                        .message("No active rules found in group")
                        .build()));
            }
            
            // Deploy to AI KIE base
            aiKieService.deployAIRules(rules);
            
            // Mark as deployed
            ruleGroup.setIsDeployed(true);
            aiRuleGroupRepository.save(ruleGroup);
            
            rules.forEach(rule -> {
                rule.setIsDeployed(true);
                aiRuleRepository.save(rule);
            });
            
            return ResponseEntity.ok(ApiResponseDTO.success("AI rules deployed successfully"));
            
        } catch (Exception e) {
            log.error("Error deploying AI rules", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to deploy AI rules")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getAllAIRuleGroups() {
        try {
            List<AIRuleGroup> groups = aiRuleGroupRepository.findByStatusTrue();
            List<AIRuleGroupDTO> groupDTOs = groups.stream()
                .map(group -> {
                    AIRuleGroupDTO dto = modelMapper.map(group, AIRuleGroupDTO.class);
                    dto.setRuleCount(group.getAiRules() != null ? group.getAiRules().size() : 0);
                    return dto;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponseDTO.success(groupDTOs));
            
        } catch (Exception e) {
            log.error("Error fetching AI rule groups", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to fetch AI rule groups")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getAIRulesByGroupId(Integer aiRuleGroupId) {
        try {
            List<AIRule> rules = aiRuleRepository.findByAiRuleGroupAiRuleGroupIdAndStatusTrue(aiRuleGroupId);
            List<AIRuleDTO> ruleDTOs = rules.stream()
                .map(rule -> modelMapper.map(rule, AIRuleDTO.class))
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponseDTO.success(ruleDTOs));
            
        } catch (Exception e) {
            log.error("Error fetching AI rules by group", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to fetch AI rules")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> getDeployedAIRules() {
        try {
            List<AIRule> deployedRules = aiRuleRepository.findDeployedRulesOrderByPriority();
            List<AIRuleDTO> ruleDTOs = deployedRules.stream()
                .map(rule -> modelMapper.map(rule, AIRuleDTO.class))
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponseDTO.success(ruleDTOs));
            
        } catch (Exception e) {
            log.error("Error fetching deployed AI rules", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to fetch deployed AI rules")
                    .build()));
        }
    }
    
    @Override
    public ResponseEntity<ApiResponseDTO> testAIRuleGroup(AIRuleTestRequestDTO testRequest) {
        try {
            AIRuleGroup ruleGroup = aiRuleGroupRepository.findById(testRequest.getAiRuleGroupId())
                .orElseThrow(() -> new RuntimeException("AI Rule Group not found"));
            
            Date startDate = Date.from(testRequest.getStartDate().atZone(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(testRequest.getEndDate().atZone(ZoneId.systemDefault()).toInstant());
            
            // Parallel execution using CompletableFuture
            CompletableFuture<List<TransactionHistory>> transactionsFuture = CompletableFuture
                .supplyAsync(() -> transactionRepository.findByCreatedAtBetween(startDate, endDate));
            
            CompletableFuture<List<AIRule>> rulesFuture = CompletableFuture
                .supplyAsync(() -> aiRuleRepository.findByAiRuleGroupAiRuleGroupIdAndStatusTrue(testRequest.getAiRuleGroupId()));
            
            // Wait for both to complete
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(transactionsFuture, rulesFuture);
            allFutures.join();
            
            List<TransactionHistory> transactions = transactionsFuture.get();
            List<AIRule> aiRules = rulesFuture.get();
            
            if (transactions.isEmpty()) {
                return ResponseEntity.ok(ApiResponseDTO.success(new ArrayList<>()));
            }
            
            // Process transactions in parallel
            List<CompletableFuture<AIRuleTestResultDTO>> futures = transactions.stream()
                .map(transaction -> CompletableFuture.supplyAsync(() -> processTransaction(transaction)))
                .collect(Collectors.toList());
            
            // Collect all results
            List<AIRuleTestResultDTO> results = futures.stream()
                .map(CompletableFuture::join)
                .filter(result -> result != null)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponseDTO.success(results));
            
        } catch (Exception e) {
            log.error("Error testing AI rule group", e);
            return ResponseEntity.status(500).body(ApiResponseDTO.error(
                ErrorDetailDTO.builder()
                    .code(ErrorCode.INTERNAL_SERVER_ERROR)
                    .message("Failed to test AI rule group")
                    .build()));
        }
    }
    
    private AIRuleTestResultDTO processTransaction(TransactionHistory transaction) {
        try {
            IsoMessageDTO isoMessage = objectMapper.readValue(transaction.getTranPacket(), IsoMessageDTO.class);
            IsoMessageDTO testResult = aiKieService.evaluateWithAIRules(isoMessage);
            
            AIRuleTestResultDTO result = new AIRuleTestResultDTO();
            result.setTransactionId(Long.valueOf(transaction.getTransactionHistoryId()));
            result.setTransactionUuid(transaction.getTranUuid());
            result.setAmount(isoMessage.getAmount());
            result.setPan(maskPan(isoMessage.getPan()));
            result.setOriginalRiskLevel(transaction.getStatus());
            result.setNewRiskLevel(testResult.getRiskLevel());
            result.setFiredAIRules(getAIRulesFromFiredRules(testResult.getFiredRules()));
            result.setStatus(testResult.getFiredRules() != null && !testResult.getFiredRules().isEmpty() ? "FLAGGED" : "PASSED");
            result.setTransactionDate(transaction.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            
            AIRuleTestResultDTO.AIRuleTestDetailDTO details = new AIRuleTestResultDTO.AIRuleTestDetailDTO();
            details.setTransactionPacket(transaction.getTranPacket());
            details.setAllFiredRules(testResult.getFiredRules());
            details.setBlockReason(testResult.getBlockReason());
            details.setRiskScore(testResult.getRiskScore());
            details.setFraudPercentage(testResult.getFraudPercentage());
            details.setActionStatus(transaction.getActionStatus());
            result.setDetails(details);
            
            return result;
        } catch (Exception e) {
            log.error("Error processing transaction {}: {}", transaction.getTransactionHistoryId(), e.getMessage());
            return null;
        }
    }
    
    private List<String> getAIRulesFromFiredRules(List<String> firedRules) {
        if (firedRules == null) return new ArrayList<>();
        return firedRules.stream()
            .filter(rule -> rule.startsWith("AI_"))
            .collect(Collectors.toList());
    }
    
    private String maskPan(String pan) {
        if (pan == null || pan.length() < 4) return "****";
        return "**** **** **** " + pan.substring(pan.length() - 4);
    }
}

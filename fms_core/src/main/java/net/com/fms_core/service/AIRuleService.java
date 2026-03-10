package net.com.fms_core.service;

import net.com.fms_core.dto.AIRuleDTO;
import net.com.fms_core.dto.AIRuleGroupDTO;
import net.com.fms_core.dto.AIRuleTestRequestDTO;
import net.com.fms_core.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;

public interface AIRuleService {
    
    ResponseEntity<ApiResponseDTO> saveAIRule(AIRuleDTO aiRuleDTO);
    
    ResponseEntity<ApiResponseDTO> saveAIRuleGroup(AIRuleGroupDTO aiRuleGroupDTO);
    
    ResponseEntity<ApiResponseDTO> deployAIRulesToProduction(Integer aiRuleGroupId);
    
    ResponseEntity<ApiResponseDTO> getAllAIRuleGroups();
    
    ResponseEntity<ApiResponseDTO> getAIRulesByGroupId(Integer aiRuleGroupId);
    
    ResponseEntity<ApiResponseDTO> getDeployedAIRules();
    
    ResponseEntity<ApiResponseDTO> testAIRuleGroup(AIRuleTestRequestDTO testRequest);
}

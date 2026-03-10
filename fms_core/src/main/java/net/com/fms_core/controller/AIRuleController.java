package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.AIRuleDTO;
import net.com.fms_core.dto.AIRuleGroupDTO;
import net.com.fms_core.dto.AIRuleTestRequestDTO;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.service.AIRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai-rules")
@RequiredArgsConstructor
public class AIRuleController {
    
    private final AIRuleService aiRuleService;
    
    @PostMapping("/rule")
    public ResponseEntity<ApiResponseDTO> saveAIRule(@RequestBody AIRuleDTO aiRuleDTO) {
        return aiRuleService.saveAIRule(aiRuleDTO);
    }
    
    @PostMapping("/group")
    public ResponseEntity<ApiResponseDTO> saveAIRuleGroup(@RequestBody AIRuleGroupDTO aiRuleGroupDTO) {
        return aiRuleService.saveAIRuleGroup(aiRuleGroupDTO);
    }
    
    @PostMapping("/deploy/{aiRuleGroupId}")
    public ResponseEntity<ApiResponseDTO> deployAIRulesToProduction(@PathVariable Integer aiRuleGroupId) {
        return aiRuleService.deployAIRulesToProduction(aiRuleGroupId);
    }
    
    @GetMapping("/groups")
    public ResponseEntity<ApiResponseDTO> getAllAIRuleGroups() {
        return aiRuleService.getAllAIRuleGroups();
    }
    
    @GetMapping("/group/{aiRuleGroupId}/rules")
    public ResponseEntity<ApiResponseDTO> getAIRulesByGroupId(@PathVariable Integer aiRuleGroupId) {
        return aiRuleService.getAIRulesByGroupId(aiRuleGroupId);
    }
    
    @PostMapping("/test")
    public ResponseEntity<ApiResponseDTO> testAIRuleGroup(@RequestBody AIRuleTestRequestDTO testRequest) {
        return aiRuleService.testAIRuleGroup(testRequest);
    }
    
    @GetMapping("/deployed")
    public ResponseEntity<ApiResponseDTO> getDeployedAIRules() {
        return aiRuleService.getDeployedAIRules();
    }
}

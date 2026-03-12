package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.AIRuleDTO;
import net.com.fms_core.dto.AIRuleGroupDTO;
import net.com.fms_core.dto.AIRuleTestRequestDTO;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.service.AIRuleService;
import net.com.fms_core.service.impl.AIRuleReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/ai-rules")
@RequiredArgsConstructor
public class AIRuleController {
    
    private final AIRuleService aiRuleService;
    private final AIRuleReportService reportService;
    
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
    
    @PostMapping("/test/download-report")
    public ResponseEntity<byte[]> downloadTestReport(@RequestBody AIRuleTestReportRequest reportRequest) {
        try {
            // Generate the report
            byte[] reportData = reportService.generateDetailedExcelReport(
                reportRequest.getTestResults(),
                reportRequest.getRuleGroupName(),
                reportRequest.getStartDate(),
                reportRequest.getEndDate()
            );
            
            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("AI_Rule_Test_Report_%s_%s.xlsx", 
                reportRequest.getRuleGroupName().replaceAll("[^a-zA-Z0-9]", "_"), timestamp);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(reportData);
                
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/deployed")
    public ResponseEntity<ApiResponseDTO> getDeployedAIRules() {
        return aiRuleService.getDeployedAIRules();
    }
    
    // DTO for report request
    public static class AIRuleTestReportRequest {
        private java.util.List<net.com.fms_core.dto.AIRuleTestResultDTO> testResults;
        private String ruleGroupName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        
        // Getters and setters
        public java.util.List<net.com.fms_core.dto.AIRuleTestResultDTO> getTestResults() { return testResults; }
        public void setTestResults(java.util.List<net.com.fms_core.dto.AIRuleTestResultDTO> testResults) { this.testResults = testResults; }
        public String getRuleGroupName() { return ruleGroupName; }
        public void setRuleGroupName(String ruleGroupName) { this.ruleGroupName = ruleGroupName; }
        public LocalDateTime getStartDate() { return startDate; }
        public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
        public LocalDateTime getEndDate() { return endDate; }
        public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    }
}

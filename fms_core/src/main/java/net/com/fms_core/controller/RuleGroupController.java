/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiPageReqDTO;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.FmsRuleDTO;
import net.com.fms_core.dto.RuleGroupDTO;
import net.com.fms_core.service.RuleGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rule-group")
public class RuleGroupController {
    private final RuleGroupService ruleGroupService;
    @PostMapping("/add-rule-group")
    public ResponseEntity<ApiResponseDTO> saveRuleGroup(@RequestBody RuleGroupDTO ruleGroupDTO) {
        return ResponseEntity.ok(ApiResponseDTO.success(ruleGroupService.saveRuleGroup(ruleGroupDTO)));
    }
    @PutMapping("/update-rule-group")
    public ResponseEntity<ApiResponseDTO> updateRuleGroup(@RequestBody RuleGroupDTO ruleGroupDTO) {
        return ResponseEntity.ok(ApiResponseDTO.success(ruleGroupService.updateRuleGroup(ruleGroupDTO)));
    }
    @GetMapping("/get-rule-group/{id}")
    public ResponseEntity<ApiResponseDTO> getRuleGroup(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponseDTO.success(ruleGroupService.getRuleGroup(id)));
    }
    @GetMapping("/filter-rule-groups")
    public ResponseEntity<ApiResponseDTO> filterRuleGroup(RuleGroupDTO ruleGroupDTO, ApiPageReqDTO pageable) {
        return ResponseEntity.ok(ApiResponseDTO.success(ruleGroupService.filterRuleGroups(ruleGroupDTO, pageable)));
    }
    @PostMapping("/test-rule-group")
    public SseEmitter testRuleGroup(@RequestBody RuleGroupDTO ruleGroupDTO) {
        return ruleGroupService.testRuleGroup(ruleGroupDTO);
    }
    
    @DeleteMapping("/delete-rule-group/{id}")
    public ResponseEntity<ApiResponseDTO> deleteRuleGroup(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponseDTO.success(ruleGroupService.deleteRuleGroup(id)));
    }
}

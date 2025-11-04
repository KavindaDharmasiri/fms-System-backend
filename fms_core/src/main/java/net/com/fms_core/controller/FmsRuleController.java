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
import net.com.fms_core.service.FmsRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rule")
public class FmsRuleController {
    private final FmsRuleService FmsRuleService;
    @PostMapping("/add-rule")
    public ResponseEntity<ApiResponseDTO> saveRule(@RequestBody FmsRuleDTO FmsRuleDTO) {
        return ResponseEntity.ok(ApiResponseDTO.success(FmsRuleService.saveRule(FmsRuleDTO)));
    }
    @PutMapping("/update-rule")
    public ResponseEntity<ApiResponseDTO> updateRule(@RequestBody FmsRuleDTO FmsRuleDTO) {
        return ResponseEntity.ok(ApiResponseDTO.success(FmsRuleService.updateRule(FmsRuleDTO)));
    }
    @GetMapping("/get-rule/{id}")
    public ResponseEntity<ApiResponseDTO> getRule(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponseDTO.success(FmsRuleService.getRule(id)));
    }
    @GetMapping("/filter-rules")
    public ResponseEntity<ApiResponseDTO> filterRule(FmsRuleDTO FmsRuleDTO, ApiPageReqDTO pageable) {
        return ResponseEntity.ok(ApiResponseDTO.success(FmsRuleService.filterFmsRules(FmsRuleDTO, pageable)));
    }
    @GetMapping("/get-final-rules")
    public ResponseEntity<ApiResponseDTO> getFinalRules() {
        return ResponseEntity.ok(ApiResponseDTO.success(FmsRuleService.getFinalRules()));
    }
    @PostMapping("/test-rule")
    public SseEmitter testRule(@RequestBody FmsRuleDTO FmsRuleDTO) {
        return FmsRuleService.testRule(FmsRuleDTO);
    }
}

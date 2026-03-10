/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.RiskMetrixDTO;
import net.com.fms_core.service.RiskMetrixService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/risk-matrix")
@RequiredArgsConstructor
public class RiskMetrixController {
    
    private final RiskMetrixService riskMetrixService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponseDTO> getAllRiskMetrix() {
        return riskMetrixService.getAllRiskMetrix();
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponseDTO> getRiskMetrixById(@PathVariable Integer id) {
        return riskMetrixService.getRiskMetrixById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDTO> saveRiskMetrix(@RequestBody RiskMetrixDTO riskMetrixDTO) {
        return riskMetrixService.saveRiskMetrix(riskMetrixDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO> updateRiskMetrix(@RequestBody RiskMetrixDTO riskMetrixDTO) {
        return riskMetrixService.updateRiskMetrix(riskMetrixDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDTO> deleteRiskMetrix(@PathVariable Integer id) {
        return riskMetrixService.deleteRiskMetrix(id);
    }
}
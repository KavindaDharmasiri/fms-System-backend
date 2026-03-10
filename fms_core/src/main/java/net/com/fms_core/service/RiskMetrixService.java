/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;

import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.RiskMetrixDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RiskMetrixService {
    ResponseEntity<ApiResponseDTO> getAllRiskMetrix();
    ResponseEntity<ApiResponseDTO> getRiskMetrixById(Integer id);
    ResponseEntity<ApiResponseDTO> saveRiskMetrix(RiskMetrixDTO riskMetrixDTO);
    ResponseEntity<ApiResponseDTO> updateRiskMetrix(RiskMetrixDTO riskMetrixDTO);
    ResponseEntity<ApiResponseDTO> deleteRiskMetrix(Integer id);
}
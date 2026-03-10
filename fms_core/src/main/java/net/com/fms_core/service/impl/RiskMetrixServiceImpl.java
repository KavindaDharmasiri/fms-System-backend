/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.ErrorDetailDTO;
import net.com.fms_core.dto.RiskMetrixDTO;
import net.com.fms_core.entity.RiskMetrix;
import net.com.fms_core.enums.ErrorCode;
import net.com.fms_core.repository.RiskMetrixRepository;
import net.com.fms_core.service.RiskMetrixService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RiskMetrixServiceImpl implements RiskMetrixService {
    
    private final RiskMetrixRepository riskMetrixRepository;

    @Override
    public ResponseEntity<ApiResponseDTO> getAllRiskMetrix() {
        try {
            List<RiskMetrix> riskMetrixList = riskMetrixRepository.findAll();
            List<RiskMetrixDTO> dtoList = riskMetrixList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success(dtoList));
        } catch (Exception e) {
            log.error("Error getting all risk matrix", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error(ErrorDetailDTO.builder()
                            .code(ErrorCode.INTERNAL_SERVER_ERROR)
                            .message("Failed to retrieve risk matrix")
                            .field("")
                            .build()));
        }
    }

    @Override
    public ResponseEntity<ApiResponseDTO> getRiskMetrixById(Integer id) {
        try {
            RiskMetrix riskMetrix = riskMetrixRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Risk Matrix not found"));
            return ResponseEntity.ok(ApiResponseDTO.success(convertToDTO(riskMetrix)));
        } catch (Exception e) {
            log.error("Error getting risk matrix by id: " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(ErrorDetailDTO.builder()
                            .code(ErrorCode.ENTITY_NOT_FOUND)
                            .message("Risk Matrix not found")
                            .field("")
                            .build()));
        }
    }

    @Override
    public ResponseEntity<ApiResponseDTO> saveRiskMetrix(RiskMetrixDTO riskMetrixDTO) {
        try {
            RiskMetrix riskMetrix = convertToEntity(riskMetrixDTO);
            RiskMetrix saved = riskMetrixRepository.save(riskMetrix);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success(convertToDTO(saved)));
        } catch (Exception e) {
            log.error("Error saving risk matrix", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error(ErrorDetailDTO.builder()
                            .code(ErrorCode.INTERNAL_SERVER_ERROR)
                            .message("Failed to save risk matrix")
                            .field("")
                            .build()));
        }
    }

    @Override
    public ResponseEntity<ApiResponseDTO> updateRiskMetrix(RiskMetrixDTO riskMetrixDTO) {
        try {
            if (riskMetrixDTO.getRiskMetrixId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponseDTO.error(ErrorDetailDTO.builder()
                                .code(ErrorCode.INVALID_INPUT)
                                .message("Risk Matrix ID is required for update")
                                .field("")
                                .build()));
            }
            
            RiskMetrix existingRiskMetrix = riskMetrixRepository.findById(riskMetrixDTO.getRiskMetrixId())
                    .orElseThrow(() -> new RuntimeException("Risk Matrix not found"));
            
            existingRiskMetrix.setFlag(riskMetrixDTO.getFlag());
            existingRiskMetrix.setMinValue(riskMetrixDTO.getMinValue());
            existingRiskMetrix.setMaxValue(riskMetrixDTO.getMaxValue());
            
            RiskMetrix updated = riskMetrixRepository.save(existingRiskMetrix);
            return ResponseEntity.ok(ApiResponseDTO.success(convertToDTO(updated)));
        } catch (Exception e) {
            log.error("Error updating risk matrix", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error(ErrorDetailDTO.builder()
                            .code(ErrorCode.INTERNAL_SERVER_ERROR)
                            .message("Failed to update risk matrix")
                            .field("")
                            .build()));
        }
    }

    @Override
    public ResponseEntity<ApiResponseDTO> deleteRiskMetrix(Integer id) {
        try {
            if (!riskMetrixRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseDTO.error(ErrorDetailDTO.builder()
                                .code(ErrorCode.ENTITY_NOT_FOUND)
                                .message("Risk Matrix not found")
                                .field("")
                                .build()));
            }
            
            riskMetrixRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Risk Matrix deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting risk matrix", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error(ErrorDetailDTO.builder()
                            .code(ErrorCode.INTERNAL_SERVER_ERROR)
                            .message("Failed to delete risk matrix")
                            .field("")
                            .build()));
        }
    }

    private RiskMetrixDTO convertToDTO(RiskMetrix riskMetrix) {
        return RiskMetrixDTO.builder()
                .riskMetrixId(riskMetrix.getRiskMetrixId())
                .flag(riskMetrix.getFlag())
                .minValue(riskMetrix.getMinValue())
                .maxValue(riskMetrix.getMaxValue())
                .build();
    }

    private RiskMetrix convertToEntity(RiskMetrixDTO dto) {
        RiskMetrix riskMetrix = new RiskMetrix();
        riskMetrix.setRiskMetrixId(dto.getRiskMetrixId());
        riskMetrix.setFlag(dto.getFlag());
        riskMetrix.setMinValue(dto.getMinValue());
        riskMetrix.setMaxValue(dto.getMaxValue());
        return riskMetrix;
    }
}
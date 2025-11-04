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
import net.com.fms_core.dto.ValidateTransactionDTO;
import net.com.fms_core.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/validate-transaction")
public class ValidationController {
    private final ValidationService validationService;
    @GetMapping("/filter")
    public ResponseEntity<ApiResponseDTO> filterTransactions(ValidateTransactionDTO dto, ApiPageReqDTO pageable) {
        return ResponseEntity.ok(ApiResponseDTO.success(validationService.filterTransactions(dto, pageable)));
    }
}

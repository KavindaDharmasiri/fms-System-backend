/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.service.CommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common")
public class CommonController {
    private final CommonService commonService;
    @GetMapping("/get-payment-networks")
    public ResponseEntity<ApiResponseDTO> getPaymentNetworks() {
        return commonService.getPaymentNetworks();
    }
}

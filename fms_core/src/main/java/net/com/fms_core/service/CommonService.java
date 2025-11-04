/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;

public interface CommonService {
    ResponseEntity<ApiResponseDTO> getPaymentNetworks();
}

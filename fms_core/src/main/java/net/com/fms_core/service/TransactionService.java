/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.ApiResponseDTO;
import net.com.fms_core.dto.TransactionFilterDto;
import net.com.fms_core.dto.TransactionHistoryDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;
public interface TransactionService {
    ResponseEntity<ApiResponseDTO> saveTestTran(List<TransactionHistoryDTO> tranDto);
    ResponseEntity<ApiResponseDTO> getAllTran(TransactionFilterDto transactionFilterDto);
    ResponseEntity<ApiResponseDTO> getAllTrans();
    ResponseEntity<ApiResponseDTO> getAllVariableNames();
}

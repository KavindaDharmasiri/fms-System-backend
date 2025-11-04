/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.ApiPageReqDTO;
import net.com.fms_core.dto.ValidateTransactionDTO;
import net.com.fms_core.dto.ValidationResultDTO;
import net.com.fms_core.dto.message.IsoMessageDTO;
import org.springframework.data.domain.Page;

public interface ValidationService {
    Page<ValidateTransactionDTO> filterTransactions(ValidateTransactionDTO validateTransactionDTO, ApiPageReqDTO pageable);
    ValidationResultDTO ValidateTransaction(IsoMessageDTO isoMessageDTO);
}

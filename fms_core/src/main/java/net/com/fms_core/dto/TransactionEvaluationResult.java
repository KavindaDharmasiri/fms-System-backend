/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.message.IsoMessageDTO;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionEvaluationResult {
    private IsoMessageDTO transaction;
    private List<String> firedRuleNames;
}

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.TransactionEvaluationResult;
import net.com.fms_core.dto.message.IsoMessageDTO;
import org.kie.api.KieBase;
import java.util.List;

public interface DynamicDroolsService {
    KieBase loadRulesFromStringList(List<String> rulesList);
    KieBase loadRulesFromStringList2(List<String> rulesList);
    IsoMessageDTO evaluateTransaction(IsoMessageDTO txn);
    IsoMessageDTO evaluateTransaction2(IsoMessageDTO txn);
    TransactionEvaluationResult evaluateTransactionWithRules(IsoMessageDTO txn);
}

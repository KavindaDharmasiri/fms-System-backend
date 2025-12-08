package net.com.fms_core.service.impl.script;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.FmsRule;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.repository.FmsRuleRepository;
import net.com.fms_core.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */

@Service
@RequiredArgsConstructor
public class TransactionExportService {

    private final TransactionRepository transactionRepository;
    private final FmsRuleRepository fmsRuleRepository;
    private final ObjectMapper objectMapper;

    public void exportTransactionsToCSV(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            List<TransactionHistory> transactions = transactionRepository.findAll();
            List<FmsRule> rules = fmsRuleRepository.findAll();
            Map<String, String> ruleMap = new HashMap<>();
            for (FmsRule rule : rules) {
                ruleMap.put(rule.getRuleName(), rule.getFinalRule());
            }

            writer.append("amount,transactionFeeAmount,settlementFeeAmount,transactionProcessingFee,settlementProcessingFee,fired_rule_name,rule_content\n");

            for (TransactionHistory txn : transactions) {
                IsoMessageDTO dto = objectMapper.readValue(txn.getTranPacket(), IsoMessageDTO.class);
                String ruleName = dto.getRuleName() != null ? dto.getRuleName() : "NO_RULE";
                String ruleContent = ruleMap.getOrDefault(ruleName, "").replace("\"", "\\\"").replace("\n", " ");

                String line = String.format("%f,%f,%f,%f,%f,%s,\"%s\"\n",
                        dto.getAmount() != 0 ? dto.getAmount() : 0.0,
                        dto.getTransactionFeeAmount() != 0 ? dto.getTransactionFeeAmount() : 0.0,
                        dto.getSettlementFeeAmount() != 0 ? dto.getSettlementFeeAmount() : 0.0,
                        dto.getTransactionProcessingFee() != 0 ? dto.getTransactionProcessingFee() : 0.0,
                        dto.getSettlementProcessingFee() != 0 ? dto.getSettlementProcessingFee() : 0.0,
                        ruleName,
                        ruleContent
                );

                writer.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

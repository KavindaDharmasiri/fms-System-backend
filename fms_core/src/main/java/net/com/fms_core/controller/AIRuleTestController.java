package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.service.DynamicDroolsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai-rule-test")
@RequiredArgsConstructor
public class AIRuleTestController {

    private final DynamicDroolsService droolsService;
    private final RiskController riskController;

    @PostMapping("/test-transaction")
    public String testTransaction(@RequestBody TestTransactionRequest request) {
        IsoMessageDTO transaction = new IsoMessageDTO();
        transaction.setAmount(request.getAmount());
        transaction.setTransactionFeeAmount(request.getTransactionFeeAmount());
        transaction.setSettlementFeeAmount(request.getSettlementFeeAmount());
        transaction.setTransactionProcessingFee(request.getTransactionProcessingFee());
        transaction.setSettlementProcessingFee(request.getSettlementProcessingFee());
        transaction.setPan("4123709999000029");
        transaction.setStan(123.0);
        transaction.setMerchantCategoryCode("5999");
        
        if (request.getTerminalId() != null) {
            transaction.setTerminalId(request.getTerminalId());
        }
        if (request.getCardAcceptorNameLocation() != null) {
            transaction.setCardAcceptorNameLocation(request.getCardAcceptorNameLocation());
        }
        
        return riskController.executeTransactions(transaction);
    }

    @GetMapping("/test-scenarios")
    public String testScenarios() {
        StringBuilder results = new StringBuilder();
        
        // Test 1: Micro transaction
        results.append("Test 1 - Micro Transaction (amount=50):\n");
        results.append(testAmount(50.0)).append("\n\n");
        
        // Test 2: Low risk
        results.append("Test 2 - Low Risk (amount=300):\n");
        results.append(testAmount(300.0)).append("\n\n");
        
        // Test 3: Standard transaction
        results.append("Test 3 - Standard (amount=700):\n");
        results.append(testAmount(700.0)).append("\n\n");
        
        // Test 4: Medium amount
        results.append("Test 4 - Medium Amount (amount=1500):\n");
        results.append(testAmount(1500.0)).append("\n\n");
        
        // Test 5: High amount
        results.append("Test 5 - High Amount (amount=6000):\n");
        results.append(testAmount(6000.0)).append("\n\n");
        
        return results.toString();
    }

    private String testAmount(double amount) {
        IsoMessageDTO transaction = new IsoMessageDTO();
        transaction.setAmount(amount);
        transaction.setTransactionFeeAmount(amount * 0.02);
        transaction.setSettlementFeeAmount(amount * 0.01);
        transaction.setTransactionProcessingFee(2.0);
        transaction.setSettlementProcessingFee(1.0);
        transaction.setPan("4123709999000029");
        transaction.setStan(123.0);
        
        IsoMessageDTO result = droolsService.evaluateTransaction(transaction);
        return String.format("Amount: %.2f, Risk: %s, Rule: %s, Fired Rules: %s", 
            amount, result.getRiskLevel(), result.getRuleName(), result.getFiredRules());
    }

    public static class TestTransactionRequest {
        private double amount;
        private double transactionFeeAmount;
        private double settlementFeeAmount;
        private double transactionProcessingFee;
        private double settlementProcessingFee;
        private String terminalId;
        private String cardAcceptorNameLocation;

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public double getTransactionFeeAmount() { return transactionFeeAmount; }
        public void setTransactionFeeAmount(double transactionFeeAmount) { this.transactionFeeAmount = transactionFeeAmount; }
        public double getSettlementFeeAmount() { return settlementFeeAmount; }
        public void setSettlementFeeAmount(double settlementFeeAmount) { this.settlementFeeAmount = settlementFeeAmount; }
        public double getTransactionProcessingFee() { return transactionProcessingFee; }
        public void setTransactionProcessingFee(double transactionProcessingFee) { this.transactionProcessingFee = transactionProcessingFee; }
        public double getSettlementProcessingFee() { return settlementProcessingFee; }
        public void setSettlementProcessingFee(double settlementProcessingFee) { this.settlementProcessingFee = settlementProcessingFee; }
        public String getTerminalId() { return terminalId; }
        public void setTerminalId(String terminalId) { this.terminalId = terminalId; }
        public String getCardAcceptorNameLocation() { return cardAcceptorNameLocation; }
        public void setCardAcceptorNameLocation(String cardAcceptorNameLocation) { this.cardAcceptorNameLocation = cardAcceptorNameLocation; }
    }
}

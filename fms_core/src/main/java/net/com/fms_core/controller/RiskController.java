/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ImpossibleDistanceResult;
import net.com.fms_core.dto.RiskManagement.RuleA;
import net.com.fms_core.dto.RiskManagement.RuleGroup;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.ValidationResultDTO;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.dto.message.VisaField126DTO;
import net.com.fms_core.dto.message.VisaField44DTO;
import net.com.fms_core.service.DynamicDroolsService;
import net.com.fms_core.service.ImpossibleDistanceService;
import net.com.fms_core.service.MyAsyncService;
import net.com.fms_core.service.ValidationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static java.lang.Integer.parseInt;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/risk")
public class RiskController {
    private final DynamicDroolsService droolsService;
    private final MyAsyncService myAsyncService;
    private final ValidationService validationService;
    private final ImpossibleDistanceService impossibleDistanceService;
    @GetMapping("/set-rule")
    public String setRules() {
        long startTime = System.currentTimeMillis();
        List<String> rulesList = new ArrayList<>();
        RuleGroup[] ruleGroups = new RuleGroup[50];
        for (int i = 0; i < 50; i++) {
            ruleGroups[i] = new RuleGroup("group" + i, new RuleA());
        }
        int i = 0;
        for (RuleGroup group : ruleGroups) {
            i++;
            List<String> rules = group.getRule().getRules();
            if (rules != null) {
                for (String rule : rules) {
                    i++;
                    String newRule = rule.replace("- 0", "- " + String.format("%03d", i));
                    String newRule1 = newRule.replace("RISK due", "- " + String.format("%03d", i));
                    rulesList.add(newRule1);
                }
            }
        }
        rulesList = rulesList.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("rulesList.size = " + rulesList.size());
        log.info("rulesList.size = " + rulesList.size());
        droolsService.loadRulesFromStringList(rulesList);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("All Tasks completed. Execution time: SET RULES " + duration + " milliseconds");
        return "";
    }
    @GetMapping("/execute-transactions")
    public String executeTransactions() {
        Transaction txn;
        Transaction[] transactionList =  new Transaction[1000];
        for (int i = 0; i < 1000; i++) {
            txn = new Transaction();
            txn.setTranId(i);
            txn.setAmount(3677); // Example: Large amount
            txn.setCustomerRiskScore(8); // Example: High-risk customer
            txn.setLocation("Unknown"); // Example: Unusual location
            txn.setFrequency(15); // Example: High transaction frequency
            txn.setTimeDifference(30); // Example: Time difference in minutes
            txn.setVendorStatus("Unusual"); // Example: Unusual vendor
            txn.setAccountAge(5); // Example: New account
            txn.setSpendingPattern("Inconsistent"); // Example: Inconsistent spending
            txn.setFraudulent(false); // Example: Not fraudulent
            txn.setTimeOfDay("Late Night"); // Example: Unusual transaction time
            txn.setTransactionType("Withdrawal"); // Example: Withdrawal
            txn.setCountry("XYZ"); // Example: Suspicious country
            txn.setCurrency("USD"); // Example: Currency
            txn.setPaymentMethod("Credit Card"); // Example: Payment method
            txn.setDeviceStatus("New"); // Example: New device
            txn.setAccountType("Anonymous"); // Example: Anonymous account
            txn.setMerchantStatus("High-Risk"); // Example: High-risk merchant
            txn.setRecipientType("Family"); // Example: Recipient type
            transactionList[i] = txn;
        }
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("All Tasks completed. Execution time: " + duration + " milliseconds");
        return "";
    }
    public String executeTransactions(IsoMessageDTO isoMessageDTO) {
        System.out.println("1");
        List<IsoMessageDTO> transactionList = new ArrayList<>();
        isoMessageDTO.setTranId((int) isoMessageDTO.getStan());
        isoMessageDTO.setCustomerRiskScore(10);
        
        // 1. First validate transaction format
//        ValidationResultDTO validationResultDTO = validationService.ValidateTransaction(isoMessageDTO);
//        if (!validationResultDTO.isValid()) {
//            log.warn("Transaction validation failed: {}", validationResultDTO.getErrorMessages());
//            return "VALIDATION_FAILED";
//        }
        System.out.println("2");
        // 2. Check impossible distance BEFORE rule execution
        ImpossibleDistanceResult distanceResult = impossibleDistanceService.checkImpossibleDistance(isoMessageDTO);
        
        // Extract fraud percentage from distance analysis
        if (distanceResult != null && distanceResult.getAlertMessage() != null) {
            String alertMessage = distanceResult.getAlertMessage();
            
            // Parse fraud probability from alert message
            if (alertMessage.contains("Model probability:")) {
                try {
                    int startIdx = alertMessage.indexOf("Model probability:") + 18;
                    int endIdx = alertMessage.indexOf(")", startIdx);
                    if (endIdx > startIdx) {
                        String probStr = alertMessage.substring(startIdx, endIdx).trim();
                        Double fraudPercentage = Double.parseDouble(probStr) * 100;
                        isoMessageDTO.setFraudPercentage(fraudPercentage);
                        log.info("Fraud percentage set: {}%", fraudPercentage);
                    }
                } catch (Exception e) {
                    log.debug("Could not parse fraud probability: {}", e.getMessage());
                }
            }
            
            // Set block reason if high risk
            if (distanceResult.isImpossible() || "HIGH".equals(distanceResult.getRiskLevel())) {
                isoMessageDTO.setBlockReason("IMPOSSIBLE_DISTANCE_DETECTED");
            }
        }
        
        if (distanceResult.isImpossible()) {
            log.error("IMPOSSIBLE DISTANCE DETECTED: {}", distanceResult.getAlertMessage());
            log.error("Distance: {}km, Time: {}min, Required Speed: {}km/h", 
                     distanceResult.getDistanceKm(), 
                     distanceResult.getTimeDifferenceMinutes(), 
                     distanceResult.getRequiredSpeedKmh());
            
            // Set detailed block reason
            String detailedReason = String.format("Impossible Distance: %.2f km traveled in %d minutes (requires %.2f km/h speed). Previous location: %s, Current location: %s",
                distanceResult.getDistanceKm(),
                distanceResult.getTimeDifferenceMinutes(),
                distanceResult.getRequiredSpeedKmh(),
                distanceResult.getPreviousLocation(),
                distanceResult.getCurrentLocation());
            
            // Set high risk and block transaction
            isoMessageDTO.setRiskLevel("HIGH");
            isoMessageDTO.setRiskScore(100);
            isoMessageDTO.setBlockReason(detailedReason);
            
            // Add to fired rules list
            if (isoMessageDTO.getFiredRules() == null) {
                isoMessageDTO.setFiredRules(new ArrayList<>());
            }
            isoMessageDTO.getFiredRules().add("Impossible Distance");
            
            // Save blocked transaction
            droolsService.evaluateTransaction(isoMessageDTO);
            return "BLOCKED_IMPOSSIBLE_DISTANCE";
        }
        log.info("Distance: {}km, Time: {}min, Required Speed: {}km/h",
                 distanceResult.getDistanceKm(),
                 distanceResult.getTimeDifferenceMinutes(),
                 distanceResult.getRequiredSpeedKmh());

        // 3. Apply additional risk scoring based on distance analysis
        if ("HIGH".equals(distanceResult.getRiskLevel())) {
            isoMessageDTO.setCustomerRiskScore(isoMessageDTO.getCustomerRiskScore() + 20);
            log.warn("High distance risk detected: {}km in {}min", 
                    distanceResult.getDistanceKm(), distanceResult.getTimeDifferenceMinutes());
        } else if ("MID".equals(distanceResult.getRiskLevel())) {
            isoMessageDTO.setCustomerRiskScore(isoMessageDTO.getCustomerRiskScore() + 10);
        }
        
        // 4. Proceed with normal rule execution
        transactionList.add(isoMessageDTO);
        long startTime = System.currentTimeMillis();
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        for (IsoMessageDTO txn1 : transactionList) {
            tasks.add(myAsyncService.processTask(txn1));
        }
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("All Tasks completed. Execution time: " + duration + " milliseconds");
        
        return "PROCESSED";
    }
    private boolean isAmountUnusual(String transactionAmount) {
        long amount = Long.parseLong(transactionAmount);
        double actualAmount = amount / 100.0; // Convert to dollars
        return actualAmount > 10000 || actualAmount < 1; // Example limits
    }
    private boolean isMerchantHighRisk(String mcc) {
        if (mcc == null || mcc.length() != 4) return false;
        List<String> highRiskMCCs = Arrays.asList(
                "5999", // Miscellaneous Retail
                "6012", // Financial Institutions
                "7995", // Betting/Casinos
                "5122", // Drugs/Pharmaceutical
                "5967"  // Direct Marketing
        );
        return highRiskMCCs.contains(mcc);
    }
    private boolean isFraudulentTransaction(String responseCode) {
        if (responseCode == null || responseCode.length() != 2) {
            return false; // Invalid format
        }
        List<String> fraudCodes = Arrays.asList(
                "04", // Pick up card (may indicate stolen card) [[2]]
                "05", // Do not honor (generic decline, may include fraud) [[2]]
                "51", // Insufficient funds (not fraud, included for example)
                "59", // Suspected fraud (common industry standard)
                "63"  // Security violation
        );
        return fraudCodes.contains(responseCode);
    }
    private int calculateRiskScore(IsoMessageDTO isoMessageDTO) {
        int totalWeight = 0;
        int mismatchedWeight = 0;
        if (isoMessageDTO.getAmount() != 0) {
            totalWeight += 30; // Weight for transaction amount
            if (isAmountUnusual(isoMessageDTO.getAmount()+"")) {
                mismatchedWeight += 30; // High-risk amount
            }
        }
        if (isoMessageDTO.getMerchantCategoryCode() != null) {
            totalWeight += 25; // Weight for MCC
            if (isMerchantHighRisk(isoMessageDTO.getMerchantCategoryCode())) {
                mismatchedWeight += 25; // High-risk merchant
            }
        }
        if (isoMessageDTO.getResponseCode() != null) {
            totalWeight += 20; // Weight for response code
            if (isFraudulentTransaction(isoMessageDTO.getResponseCode())) {
                mismatchedWeight += 20; // Fraudulent response code
            }
        }
        if (isoMessageDTO.getPosEntryMode() != null) {
            totalWeight += 15; // Weight for POS entry mode
            if (isUnusualPOSEntryMode(isoMessageDTO.getPosEntryMode())) {
                mismatchedWeight += 15; // Unusual POS entry mode
            }
        }
        if (isoMessageDTO.getAdditionalResponseData() != null) {
            totalWeight += 10; // Weight for additional response data
            if (isCVVOrAVSFailure(isoMessageDTO.getAdditionalResponseData())) {
                mismatchedWeight += 10; // CVV/AVS failure
            }
        }
        return (int) ((double) mismatchedWeight / totalWeight * 100);
    }
    private boolean isUnusualPOSEntryMode(String posEntryMode) {
        return !Arrays.asList("02", "05").contains(posEntryMode); // Example valid POS modes
    }
    private boolean isCVVOrAVSFailure(VisaField44DTO additionalResponseData) {
        if (additionalResponseData == null) return false;
        return "N".equals(additionalResponseData.getCvvResultCode()) ||
                "N".equals(additionalResponseData.getAvsResultCode());
    }
    private double parseAmount(String amountStr) {
        try {
            return Double.parseDouble(amountStr) / 100; // Assuming cents format
        } catch (NumberFormatException e) {
            return 0.0; // Handle invalid amounts
        }
    }
}

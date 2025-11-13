package net.com.fms_core.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public class LoggingUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);
    
    public static void setRequestContext(String userId, String requestId) {
        MDC.put("userId", userId);
        MDC.put("requestId", requestId != null ? requestId : UUID.randomUUID().toString());
        MDC.put("service", "fms-core");
    }
    
    public static void setTransactionContext(String transactionId, String amount) {
        MDC.put("transactionId", transactionId);
        MDC.put("amount", amount);
    }
    
    public static void logFraudDetection(String transactionId, String riskScore, String decision) {
        MDC.put("transactionId", transactionId);
        MDC.put("riskScore", riskScore);
        MDC.put("decision", decision);
        logger.info("Fraud detection completed for transaction: {}", transactionId);
    }
    
    public static void logAuditEvent(String action, String entityType, String entityId) {
        MDC.put("auditAction", action);
        MDC.put("entityType", entityType);
        MDC.put("entityId", entityId);
        logger.info("Audit event: {} on {} with ID: {}", action, entityType, entityId);
    }
    
    public static void clearContext() {
        MDC.clear();
    }
}
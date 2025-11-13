package net.com.fms_auth.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public class AuthLoggingUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthLoggingUtil.class);
    
    public static void setAuthContext(String username, String requestId) {
        MDC.put("username", username);
        MDC.put("requestId", requestId != null ? requestId : UUID.randomUUID().toString());
        MDC.put("service", "fms-auth");
    }
    
    public static void logAuthEvent(String event, String username, String result) {
        MDC.put("authEvent", event);
        MDC.put("username", username);
        MDC.put("result", result);
        logger.info("Authentication event: {} for user: {} - Result: {}", event, username, result);
    }
    
    public static void logSecurityEvent(String event, String details) {
        MDC.put("securityEvent", event);
        MDC.put("details", details);
        logger.warn("Security event: {} - {}", event, details);
    }
    
    public static void clearContext() {
        MDC.clear();
    }
}
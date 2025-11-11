package net.com.fms_core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.entity.AuditLog;
import net.com.fms_core.repository.AuditLogRepository;
import net.com.fms_core.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {
    
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest httpServletRequest;
    
    @Override
    @Async
    public void logAction(String userId, String action, String entityType, String entityId, 
                         Object oldValues, Object newValues, String status, String errorMessage) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(userId);
            auditLog.setAction(action);
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);
            auditLog.setOldValues(oldValues != null ? objectMapper.writeValueAsString(oldValues) : null);
            auditLog.setNewValues(newValues != null ? objectMapper.writeValueAsString(newValues) : null);
            auditLog.setIpAddress(getClientIpAddressSafe());
            auditLog.setUserAgent(getUserAgentSafe());
            auditLog.setSessionId(getSessionIdSafe());
            auditLog.setTimestamp(LocalDateTime.now());
            auditLog.setStatus(status);
            auditLog.setErrorMessage(errorMessage);
            auditLog.setRequestId(UUID.randomUUID().toString());
            
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to save audit log", e);
        }
    }
    
    @Override
    public void logLogin(String userId, String ipAddress, String userAgent, String status) {
        logAction(userId, "LOGIN", "USER", userId, null, null, status, null);
    }
    
    @Override
    public void logLogout(String userId, String ipAddress, String userAgent) {
        logAction(userId, "LOGOUT", "USER", userId, null, null, "SUCCESS", null);
    }
    
    @Override
    public void logRuleExecution(String userId, String ruleId, String transactionId, String result) {
        logAction(userId, "RULE_EXECUTION", "RULE", ruleId, null, 
                 "Transaction: " + transactionId + ", Result: " + result, "SUCCESS", null);
    }
    
    @Override
    public void logConfigurationChange(String userId, String configType, String configId, 
                                     Object oldConfig, Object newConfig) {
        logAction(userId, "CONFIG_CHANGE", configType, configId, oldConfig, newConfig, "SUCCESS", null);
    }
    
    @Override
    public Page<AuditLog> getAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }
    
    @Override
    public Page<AuditLog> getAuditLogsByUser(String userId, Pageable pageable) {
        return auditLogRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
    }
    
    @Override
    public Page<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate, pageable);
    }
    
    private String getClientIpAddressSafe() {
        try {
            String xForwardedFor = httpServletRequest.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }
            return httpServletRequest.getRemoteAddr();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
    
    private String getUserAgentSafe() {
        try {
            return httpServletRequest.getHeader("User-Agent");
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
    
    private String getSessionIdSafe() {
        try {
            return httpServletRequest.getSession().getId();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
    
    @Override
    @Async
    public void logActionWithRequestInfo(String userId, String action, String entityType, String entityId, 
                                        Object oldValues, Object newValues, String status, String errorMessage,
                                        String ipAddress, String userAgent, String sessionId) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(userId);
            auditLog.setAction(action);
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);
            auditLog.setOldValues(oldValues != null ? objectMapper.writeValueAsString(oldValues) : null);
            auditLog.setNewValues(newValues != null ? objectMapper.writeValueAsString(newValues) : null);
            auditLog.setIpAddress(ipAddress);
            auditLog.setUserAgent(userAgent);
            auditLog.setSessionId(sessionId);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLog.setStatus(status);
            auditLog.setErrorMessage(errorMessage);
            auditLog.setRequestId(UUID.randomUUID().toString());
            
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to save audit log with request info", e);
        }
    }
}
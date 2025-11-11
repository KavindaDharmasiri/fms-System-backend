package net.com.fms_core.service;

import net.com.fms_core.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface AuditService {
    
    void logAction(String userId, String action, String entityType, String entityId, 
                  Object oldValues, Object newValues, String status, String errorMessage);
    
    void logActionWithRequestInfo(String userId, String action, String entityType, String entityId, 
                                 Object oldValues, Object newValues, String status, String errorMessage,
                                 String ipAddress, String userAgent, String sessionId);
    
    void logLogin(String userId, String ipAddress, String userAgent, String status);
    
    void logLogout(String userId, String ipAddress, String userAgent);
    
    void logRuleExecution(String userId, String ruleId, String transactionId, String result);
    
    void logConfigurationChange(String userId, String configType, String configId, 
                               Object oldConfig, Object newConfig);
    
    Page<AuditLog> getAuditLogs(Pageable pageable);
    
    Page<AuditLog> getAuditLogsByUser(String userId, Pageable pageable);
    
    Page<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
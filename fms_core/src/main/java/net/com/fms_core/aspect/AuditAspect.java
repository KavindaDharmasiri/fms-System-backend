package net.com.fms_core.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.service.AuditService;
import net.com.fms_core.util.AuthorizedUserContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "audit.enabled", havingValue = "true", matchIfMissing = true)
public class AuditAspect {
    
    private final AuditService auditService;
    
    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Auditable auditable, Object result) {
        try {
            String userId = AuthorizedUserContext.getUser();
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            
            auditService.logAction(
                userId != null ? userId : "SYSTEM",
                auditable.action().isEmpty() ? methodName : auditable.action(),
                auditable.entityType().isEmpty() ? className : auditable.entityType(),
                extractEntityId(joinPoint.getArgs()),
                null,
                result,
                "SUCCESS",
                null
            );
        } catch (Exception e) {
            log.error("Error in audit aspect", e);
        }
    }
    
    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Auditable auditable, Exception exception) {
        try {
            String userId = AuthorizedUserContext.getUser();
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            
            auditService.logAction(
                userId != null ? userId : "SYSTEM",
                auditable.action().isEmpty() ? methodName : auditable.action(),
                auditable.entityType().isEmpty() ? className : auditable.entityType(),
                extractEntityId(joinPoint.getArgs()),
                null,
                null,
                "FAILED",
                exception.getMessage()
            );
        } catch (Exception e) {
            log.error("Error in audit aspect", e);
        }
    }
    
    private String extractEntityId(Object[] args) {
        if (args != null && args.length > 0) {
            Object firstArg = args[0];
            if (firstArg instanceof Number) {
                return firstArg.toString();
            }
            if (firstArg instanceof String) {
                return (String) firstArg;
            }
        }
        return null;
    }
}
package net.com.fms_core.controller;

import lombok.RequiredArgsConstructor;
import net.com.fms_core.entity.AuditLog;
import net.com.fms_core.service.AuditService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@CrossOrigin("*")
@ConditionalOnBean(AuditService.class)
public class AuditController {
    
    private final AuditService auditService;
    
    @GetMapping("/logs")
    // @PreAuthorize("hasAuthority('AUDIT_VIEW')")
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<AuditLog> auditLogs = auditService.getAuditLogs(PageRequest.of(page, size));
        return ResponseEntity.ok(auditLogs);
    }
    
    @GetMapping("/logs/user/{userId}")
    // @PreAuthorize("hasAuthority('AUDIT_VIEW')")
    public ResponseEntity<Page<AuditLog>> getAuditLogsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<AuditLog> auditLogs = auditService.getAuditLogsByUser(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(auditLogs);
    }
    
    @GetMapping("/logs/date-range")
    // @PreAuthorize("hasAuthority('AUDIT_VIEW')")
    public ResponseEntity<Page<AuditLog>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<AuditLog> auditLogs = auditService.getAuditLogsByDateRange(startDate, endDate, PageRequest.of(page, size));
        return ResponseEntity.ok(auditLogs);
    }
}
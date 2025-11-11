package net.com.fms_core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name = "action", nullable = false)
    private String action;
    
    @Column(name = "entity_type", nullable = false)
    private String entityType;
    
    @Column(name = "entity_id")
    private String entityId;
    
    @Column(name = "old_values", columnDefinition = "TEXT")
    private String oldValues;
    
    @Column(name = "new_values", columnDefinition = "TEXT")
    private String newValues;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "status")
    private String status; // SUCCESS, FAILED
    
    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "request_id")
    private String requestId;
}
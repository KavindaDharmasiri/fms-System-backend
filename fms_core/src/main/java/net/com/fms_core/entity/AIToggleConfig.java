package net.com.fms_core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "ai_toggle_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIToggleConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_key", unique = true, nullable = false)
    private String configKey = "USE_AI_RULES";
    
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @PreUpdate
    @PrePersist
    public void updateTimestamp() {
        this.updatedAt = new Date();
    }
}
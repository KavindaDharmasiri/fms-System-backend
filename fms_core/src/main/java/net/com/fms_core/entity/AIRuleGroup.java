package net.com.fms_core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "ai_rule_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRuleGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_rule_group_id")
    private Integer aiRuleGroupId;
    
    @Column(name = "group_name", nullable = false)
    private String groupName;
    
    @Column(name = "group_code", unique = true, nullable = false)
    private String groupCode;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "priority")
    private Integer priority = 1;
    
    @Column(name = "status", nullable = false)
    private Boolean status = true;
    
    @Column(name = "is_deployed", nullable = false)
    private Boolean isDeployed = false;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "aiRuleGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<AIRule> aiRules;
}

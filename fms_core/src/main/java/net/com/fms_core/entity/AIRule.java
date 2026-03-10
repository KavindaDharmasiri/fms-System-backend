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
@Table(name = "ai_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_rule_id")
    private Integer aiRuleId;
    
    @Column(name = "rule_name", nullable = false)
    private String ruleName;
    
    @Column(name = "rule_code", unique = true, nullable = false)
    private String ruleCode;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "drool_rule", columnDefinition = "TEXT", nullable = false)
    private String droolRule;
    
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_rule_group_id")
    private AIRuleGroup aiRuleGroup;
    
    @OneToMany(mappedBy = "aiRule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<AIRuleCondition> aiRuleConditions;
}

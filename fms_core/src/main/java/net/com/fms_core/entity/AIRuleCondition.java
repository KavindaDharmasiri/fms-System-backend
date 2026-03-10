package net.com.fms_core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;

@Entity
@Table(name = "ai_rule_condition")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRuleCondition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_rule_condition_id")
    private Integer aiRuleConditionId;
    
    @Column(name = "field_name", nullable = false)
    private String fieldName;
    
    @Column(name = "operator", nullable = false)
    private String operator;
    
    @Column(name = "value", nullable = false)
    private String value;
    
    @Column(name = "logical_operator")
    private String logicalOperator;
    
    @Column(name = "condition_order")
    private Integer conditionOrder;
    
    @Column(name = "status", nullable = false)
    private Boolean status = true;
    
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
    @JoinColumn(name = "ai_rule_id", nullable = false)
    private AIRule aiRule;
}

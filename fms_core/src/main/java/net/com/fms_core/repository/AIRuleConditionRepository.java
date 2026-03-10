package net.com.fms_core.repository;

import net.com.fms_core.entity.AIRuleCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIRuleConditionRepository extends JpaRepository<AIRuleCondition, Integer> {
    
    List<AIRuleCondition> findByAiRuleAiRuleIdAndStatusTrueOrderByConditionOrder(Integer aiRuleId);
    
    List<AIRuleCondition> findByAiRuleAiRuleId(Integer aiRuleId);
}
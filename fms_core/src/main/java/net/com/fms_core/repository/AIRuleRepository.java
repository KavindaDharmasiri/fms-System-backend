package net.com.fms_core.repository;

import net.com.fms_core.entity.AIRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIRuleRepository extends JpaRepository<AIRule, Integer> {
    
    List<AIRule> findByStatusTrue();
    
    long countByStatusTrue();
    
    List<AIRule> findByIsDeployedTrue();
    
    List<AIRule> findByAiRuleGroupAiRuleGroupIdAndStatusTrue(Integer aiRuleGroupId);
    
    boolean existsByRuleCode(String ruleCode);
    
    @Query("SELECT ar FROM AIRule ar WHERE ar.isDeployed = true ORDER BY ar.priority ASC")
    List<AIRule> findDeployedRulesOrderByPriority();
}
package net.com.fms_core.repository;

import net.com.fms_core.entity.AIRuleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIRuleGroupRepository extends JpaRepository<AIRuleGroup, Integer> {
    
    List<AIRuleGroup> findByStatusTrue();
    
    List<AIRuleGroup> findByIsDeployedTrue();
    
    boolean existsByGroupCode(String groupCode);
    
    @Query("SELECT arg FROM AIRuleGroup arg WHERE arg.isDeployed = true ORDER BY arg.priority ASC")
    List<AIRuleGroup> findDeployedGroupsOrderByPriority();
}
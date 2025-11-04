/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.FmsRule;
import net.com.fms_core.entity.FmsRuleCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FmsRuleConditionRepository extends JpaRepository<FmsRuleCondition,Integer> {
    void removeAllByfmsRuleId_fmsRuleId(Integer fmsRuleId);
}

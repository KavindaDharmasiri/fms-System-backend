/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.FmsRule;
import net.com.fms_core.entity.RuleGroupRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleGroupRuleRepository extends JpaRepository<RuleGroupRule, Integer> {
    RuleGroupRule findByFmsRuleId(FmsRule fmsRule);
}

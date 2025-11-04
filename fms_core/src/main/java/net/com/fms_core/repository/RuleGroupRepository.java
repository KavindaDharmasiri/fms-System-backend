/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.RuleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Date;

@Repository
public interface RuleGroupRepository extends JpaRepository<RuleGroup,Integer> {
    @Query("""
SELECT rg FROM RuleGroup rg
WHERE (:ruleGroupId IS NULL OR rg.ruleGroupId = :ruleGroupId)
  AND (:ruleGroupUuid IS NULL OR rg.ruleGroupUuid = :ruleGroupUuid)
  AND (:groupName IS NULL OR LOWER(rg.groupName) LIKE LOWER(CONCAT('%', :groupName, '%')))
  AND (:verdict IS NULL OR :verdict = '' OR rg.verdict = :verdict)
  AND (:fromDate IS NULL OR rg.fromDate >= :fromDate)
  AND (:toDate IS NULL OR rg.toDate <= :toDate)
  AND (:status IS NULL OR :status = '' OR rg.status = :status)
  AND (:paymentNetworkId IS NULL OR rg.paymentNetworkId.paymentNetworkId = :paymentNetworkId)
  AND (:reactionTemplateId IS NULL OR rg.reactionTemplateId.reactionTemplateId = :reactionTemplateId)
  AND (:ruleGroupRuleId IS NULL OR EXISTS (
        SELECT 1 FROM rg.ruleGroupRuleCollection rgr WHERE rgr.fmsRuleId.fmsRuleId = :ruleGroupRuleId
      ))
  AND (:ruleGroupRoleId IS NULL OR EXISTS (
        SELECT 1 FROM rg.ruleGroupRoleCollection rgrl WHERE rgrl.ruleGroupId.ruleGroupId = :ruleGroupRoleId
      ))
""")
    Page<RuleGroup> filterRuleGroups(
            @Param("ruleGroupId") Integer ruleGroupId,
            @Param("ruleGroupUuid") String ruleGroupUuid,
            @Param("groupName") String groupName,
            @Param("verdict") String verdict,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("status") String status,
            @Param("paymentNetworkId") Integer paymentNetworkId,
            @Param("reactionTemplateId") Integer reactionTemplateId,
            @Param("ruleGroupRoleId") Integer ruleGroupRoleId,
            @Param("ruleGroupRuleId") Integer ruleGroupRuleId,
            Pageable pageable
    );
    Collection<RuleGroup> findAllByStatus(String status);
}

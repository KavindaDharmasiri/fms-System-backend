/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.FmsRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;

@Repository
public interface FmsRuleRepository extends JpaRepository<FmsRule,Integer> {
    @Query(value = "SELECT * FROM fms_rule e WHERE " +
            "(:fmsRuleId IS NULL OR e.fms_rule_id = :fmsRuleId) AND " +
            "(:ruleUuid IS NULL OR e.rule_uuid = :ruleUuid) AND " +
            "(:ruleName IS NULL OR LOWER(e.rule_name) LIKE LOWER(CONCAT('%', :ruleName, '%'))) AND " +
            "(:description IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:fromDate IS NULL OR e.from_date >= :fromDate) AND " +
            "(:toDate IS NULL OR e.to_date <= :toDate) AND " +
            "(:status IS NULL OR :status = '' OR e.status = :status) AND " +
            "(:finalRiskScore IS NULL OR e.final_risk_score = :finalRiskScore) AND " +
            "(:finalRule IS NULL OR e.final_rule = :finalRule) AND " +
            "(:paymentNetworkId IS NULL OR e.payment_network_id = :paymentNetworkId)", nativeQuery = true)
    Page<FmsRule> filterFmsRules(
            @Param("fmsRuleId") Integer fmsRuleId,
            @Param("ruleUuid") String ruleUuid,
            @Param("ruleName") String ruleName,
            @Param("description") String description,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("status") String status,
            @Param("finalRiskScore") Double finalRiskScore,
            @Param("finalRule") String finalRule,
            @Param("paymentNetworkId") Integer paymentNetworkId,
            Pageable pageable
    );
}

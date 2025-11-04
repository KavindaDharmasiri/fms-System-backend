/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.RiskMetrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface RiskMetrixRepository extends JpaRepository<RiskMetrix,Integer> {
    @Query("SELECT r FROM RiskMetrix r WHERE :value BETWEEN r.minValue AND r.maxValue")
    RiskMetrix findByRiskValue(@Param("value") double value);
}

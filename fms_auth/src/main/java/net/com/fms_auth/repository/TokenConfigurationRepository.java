/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.repository;
import net.com.fms_auth.entity.TokenConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface TokenConfigurationRepository extends JpaRepository<TokenConfiguration,Integer> {
    @Query(value = "SELECT tc FROM TokenConfiguration tc WHERE tc.tokenType=:type AND tc.status=:status")
    Optional<TokenConfiguration> findByStatusAndType(@Param("type") String type,@Param("status") String status);
}

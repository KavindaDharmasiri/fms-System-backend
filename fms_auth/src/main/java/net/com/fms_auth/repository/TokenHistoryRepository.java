/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.repository;
import net.com.fms_auth.entity.TokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface TokenHistoryRepository extends JpaRepository<TokenHistory,Integer> {
    @Query(value = "SELECT t FROM TokenHistory t WHERE t.tokenUuid=:uuid")
    Optional<TokenHistory> findTokenHistoryByTokenUUIDEquals(@Param("uuid") String uuid);
}

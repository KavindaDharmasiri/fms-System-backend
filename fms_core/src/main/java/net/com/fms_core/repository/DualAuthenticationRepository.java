/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.DualAuthentication;
import net.com.fms_core.entity.FmsRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface DualAuthenticationRepository extends JpaRepository<DualAuthentication,Integer> {
    @Query(value = "SELECT da FROM DualAuthentication da WHERE " +
            "( :configuration IS NULL OR da.configuration = :configuration ) AND " +
            "( :task IS NULL OR da.task = :task ) AND " +
            "( :modifiedUser IS NULL OR da.lastModifiedUser = :modifiedUser ) AND " + // Fixed createdBy -> lastModifiedUser?
            "( :from IS NULL OR da.createdAt >= :from ) AND " +
            "( :to IS NULL OR da.createdAt <= :to ) AND " +
            "da.status = 'PENDING' " +
            "ORDER BY da.createdAt DESC")
    Page<DualAuthentication> filterDualAuthentication(
            @Param("from") Date from,
            @Param("to") Date to,
            @Param("configuration") String configuration,
            @Param("task") String task,
            @Param("modifiedUser") String modifiedUser,
            Pageable pageable);
    @Query(value = "SELECT da FROM DualAuthentication da WHERE da.groupID=:identifier")
    List<DualAuthentication> getByGroupID(@Param("identifier") String identifier);
}

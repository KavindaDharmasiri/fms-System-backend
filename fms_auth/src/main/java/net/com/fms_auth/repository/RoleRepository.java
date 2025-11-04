/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.repository;
import net.com.fms_auth.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    @Query(value = "SELECT r from Role r WHERE r.roleName=:roleName")
    Optional<Role> findSameRoleNameOrCode(@Param("roleName") String roleName);
    @Query(value = "SELECT r FROM Role r WHERE " +
            "(:roleName IS NULL OR :roleName = '' OR LOWER(r.roleName) LIKE LOWER(CONCAT('%', :roleName, '%'))) AND " +
            "(:roleCode IS NULL OR :roleCode = '' OR LOWER(r.roleCode) LIKE LOWER(CONCAT('%', :roleCode, '%'))) AND " +
            "(:status IS NULL OR :status = '' OR r.status = :status) AND " + "r.status != 'SUSPENDED'")
    Page<Role> filterRoles(String roleName, String roleCode, String status, Pageable pageable);
}

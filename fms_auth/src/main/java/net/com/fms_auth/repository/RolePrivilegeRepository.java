/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.repository;
import jakarta.transaction.Transactional;
import net.com.fms_auth.entity.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege,Integer> {
    @Query(value = "SELECT rpd FROM RolePrivilege rpd WHERE rpd.roleId.roleId=:roleId")
    List<RolePrivilege> findByRoleRoleId(@Param("roleId") Integer roleId);
    @Modifying
    @Transactional
    @Query(value = "Delete from RolePrivilege rpd WHERE rpd.roleId.roleId=:roleID")
    void deleteByRoleRoleId(@Param("roleID")int roleID);
}

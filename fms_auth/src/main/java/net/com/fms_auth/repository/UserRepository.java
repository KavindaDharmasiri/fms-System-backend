/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.repository;
import net.com.fms_auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query(value = "SELECT u FROM User u WHERE u.email=:username")
    Optional<User> findByUsernameEquals(@Param("username") String username);
    
    @Query(value = "SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userRoleCollection ur " +
            "LEFT JOIN FETCH ur.roleId r " +
            "WHERE u.email=:username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userRoleCollection ur " +
            "WHERE (:userID IS NULL OR u.userId = :userID) " +
            "AND (:username IS NULL OR u.userName LIKE %:username%) " +
            "AND (:fullName IS NULL OR u.fullName LIKE %:fullName%) " +
            "AND (:nic IS NULL OR u.nic = :nic) " +
            "AND (:employeeID IS NULL OR u.empId = :employeeID) " +
            "AND (:email IS NULL OR u.email = :email) " +
            "AND (:contactNumber IS NULL OR u.contact = :contactNumber) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "AND (:userRole IS NULL OR ur.roleId.roleId IN :userRole)" +
            "AND " + "u.status != 'SUSPENDED'")
    Page<User> findAllUsers(Integer userID, String username, String fullName, String nic, Integer[] userRole, String employeeID, String email, String contactNumber, String status, Pageable pageable);
    @Query(value = "SELECT COUNT(u) > 0 FROM User u WHERE u.userName = :username")
    boolean existsByUsername(@Param("username")String username);
    @Query(value = "SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email")String email);
    @Query(value = "SELECT COUNT(u) > 0 FROM User u WHERE u.contact = :contact")
    boolean existsByContact(@Param("contact")String contact);
    @Query(value = "SELECT COUNT(u) > 0 FROM User u WHERE u.nic = :nic")
    boolean existsByNic(@Param("nic")String nic);
    @Query(value = "SELECT COUNT(u) > 0 FROM User u WHERE u.empId = :employeeID")
    boolean existsByEmpId(@Param("employeeID") String employeeID);
}

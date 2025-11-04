/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.repository;
import net.com.fms_auth.entity.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest,Integer> {
    PasswordResetRequest findByUsernameAndRequestStatus(String userName, String requestStatus);
    PasswordResetRequest findByRequestKey(String requestKey);
}

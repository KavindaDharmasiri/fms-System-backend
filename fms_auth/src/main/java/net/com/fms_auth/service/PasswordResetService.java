/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service;
import net.com.fms_auth.dto.request.PasswordResetRequestDTO;
import org.springframework.http.ResponseEntity;
public interface PasswordResetService {
    ResponseEntity initRequest(String username);
    ResponseEntity verifyToken(String resetKey);
    ResponseEntity resetPassword(PasswordResetRequestDTO resetRequestDTO);
    ResponseEntity resetWhenLoggedIn(PasswordResetRequestDTO passwordResetRequestDTO);
}

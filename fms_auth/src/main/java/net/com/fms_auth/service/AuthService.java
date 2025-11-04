/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service;
import net.com.fms_auth.dto.request.LoginDTO;
import org.springframework.http.ResponseEntity;
public interface AuthService {
    ResponseEntity login(LoginDTO loginDTO);
    ResponseEntity getCurrentUser();
}

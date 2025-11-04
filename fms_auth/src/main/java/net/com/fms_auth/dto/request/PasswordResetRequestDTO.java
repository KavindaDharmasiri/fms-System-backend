/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto.request;
import lombok.Data;
@Data
public class PasswordResetRequestDTO {
    private String resetKey;
    private String oldPassword;
    private String password;
}

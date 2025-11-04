/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.com.fms_auth.enums.TokenType;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationSuccessDTO {
    private TokenType tokenType;
    private String token;
}

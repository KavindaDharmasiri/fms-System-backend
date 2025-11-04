/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service;
import com.fasterxml.jackson.core.JsonProcessingException;
public interface TokenService {
    String createToken(String username) throws JsonProcessingException;
    boolean validateToken(String authToken);
    String getUserIdFromToken(String token);
}

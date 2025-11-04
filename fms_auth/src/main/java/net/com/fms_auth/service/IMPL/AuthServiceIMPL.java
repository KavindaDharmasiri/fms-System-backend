/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service.IMPL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.dto.request.LoginDTO;
import net.com.fms_auth.dto.response.AuthenticationSuccessDTO;
import net.com.fms_auth.enums.TokenType;
import net.com.fms_auth.exceptions.UnauthorizedException;
import net.com.fms_auth.service.AuthService;
import net.com.fms_auth.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import net.com.fms_auth.service.UserService;
import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceIMPL implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    @Override
    public ResponseEntity login(LoginDTO loginDTO) {
        try {
            System.out.println("loginDTO = " + loginDTO.getUsername() + " / " + loginDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );
            System.out.println("authentication = " + authentication.isAuthenticated());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenService.createToken(loginDTO.getUsername());
            log.error("{} authenticated successfully", loginDTO.getUsername());
            return new ResponseEntity<>(new AuthenticationSuccessDTO(TokenType.ACCESS_TOKEN,token), HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Authentication failed for user {}: {}", loginDTO.getUsername(), e.getMessage());
            e.printStackTrace();
            throw new UnauthorizedException("Please enter a valid username and password");
        }
    }
    
    @Override
    public ResponseEntity getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", username);
            userInfo.put("authorities", authentication.getAuthorities());
            
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            log.error("Error getting current user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
}

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String deviceType = request.getHeader("X-Device-Type");
        if ("MOBILE".equalsIgnoreCase(deviceType)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", "401");
            errorResponse.put("message", "Invalid username or password");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid username or password");
        }
    }
}

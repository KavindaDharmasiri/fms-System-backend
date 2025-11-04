/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.interceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.UUID;

@Component
public class ApiVersionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String version = extractApiVersion(request);
        String requestId = UUID.randomUUID().toString();
        RequestContextHolder.currentRequestAttributes().setAttribute("apiVersion", version, 0);
        RequestContextHolder.currentRequestAttributes().setAttribute("requestId", requestId, 0);
        RequestContextHolder.currentRequestAttributes().setAttribute("path", request.getRequestURI(), 0);
        return true;
    }
    private String extractApiVersion(HttpServletRequest request) {
        String version = request.getHeader("X-API-Version");
        if (version == null) {
            version = request.getParameter("version");
        }
        if (version == null) {
            String path = request.getRequestURI();
            String[] parts = path.split("/");
            if (parts.length > 3 && parts[3].startsWith("v")) {
                version = parts[3];
            }
        }
        return version != null ? version : "v1"; // Default to v1
    }
}

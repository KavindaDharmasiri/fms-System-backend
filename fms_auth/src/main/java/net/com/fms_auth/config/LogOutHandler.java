/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.dto.common.StandardResponse;
import net.com.fms_auth.entity.TokenHistory;
import net.com.fms_auth.repository.TokenHistoryRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LogOutHandler implements LogoutHandler {
    private final TokenHistoryRepository tokenHistoryRepository;
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        if(StringUtils.hasText(request.getHeader("Authorization")) && request.getHeader("Authorization").startsWith("Bearer ")){
            return request.getHeader("Authorization").substring(7);
        }
        return null;
    }
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
        final String authHeader =request.getHeader("Authorization");
        final String jwt;
        if(authHeader != null && !authHeader.startsWith("Bearer ")){
            return;
        }
        jwt = getJwtFromRequest(request);
        TokenHistory token = tokenHistoryRepository.findTokenHistoryByTokenUUIDEquals(jwt).get();
        token.setStatus(false);
        token.setRevokeStatus(true);
        token.setExpiredAt(new Date());
        tokenHistoryRepository.save(token);
        SecurityContextHolder.clearContext();
        StandardResponse apiResponseDTO = new StandardResponse(200,true,"Logout successful.");
        response.setStatus(HttpServletResponse.SC_OK);
        writeJsonResponse(response, apiResponseDTO);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            StandardResponse failureResponse = new StandardResponse(false,"An error occurred during logout.");
            try {
                writeJsonResponse(response, failureResponse);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
    private void writeJsonResponse(HttpServletResponse response, Object dto) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(dto));
    }
}

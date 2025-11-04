/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.dto.common.StandardResponse;
import net.com.fms_auth.entity.TokenHistory;
import net.com.fms_auth.exceptions.UnauthorizedException;
import net.com.fms_auth.repository.TokenHistoryRepository;
import net.com.fms_auth.service.TokenService;
import net.com.fms_auth.util.AuthorizedUserContext;
import org.apache.http.HttpStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenProvider;
    private final TokenHistoryRepository tokenHistoryRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            if(
                    request.getServletPath().equals("/auth/login")
            ){
                filterChain.doFilter(request,response);
                return;
            }else {
                String jwtFromRequest = getJwtFromRequest(request);
                Optional<TokenHistory> tokenHistoryByTokenUUIDEquals = tokenHistoryRepository.findTokenHistoryByTokenUUIDEquals(jwtFromRequest);
                String jwt = tokenHistoryByTokenUUIDEquals.get().getToken();
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                    UserDetails userDetails = tokenHistoryByTokenUUIDEquals.get().getUserId();
                    AuthorizedUserContext.setUser(userDetails.getUsername());
                    UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("authorization");
        log.info("token"+bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            log.info("token = "+ bearerToken.substring(7));
            return bearerToken.substring(7);
        }
        if(StringUtils.hasText(request.getHeader("Authorization")) && request.getHeader("Authorization").startsWith("Bearer ")){
            return request.getHeader("Authorization").substring(7);
        }
        return null;
    }
    private void writeJsonResponse(HttpServletResponse response, Object dto) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(dto));
    }
}

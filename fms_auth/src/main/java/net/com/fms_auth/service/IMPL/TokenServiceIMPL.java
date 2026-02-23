/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service.IMPL;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.entity.TokenConfiguration;
import net.com.fms_auth.entity.TokenHistory;
import net.com.fms_auth.entity.User;
import net.com.fms_auth.enums.Status;
import net.com.fms_auth.enums.TokenType;
import net.com.fms_auth.exceptions.InternalServerErrorException;
import net.com.fms_auth.repository.TokenConfigurationRepository;
import net.com.fms_auth.repository.TokenHistoryRepository;
import net.com.fms_auth.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TokenServiceIMPL implements TokenService {
    private final TokenConfigurationRepository tokenConfigurationRepository;
    private final TokenHistoryRepository tokenHistoryRepository;
    private final HttpServletRequest httpServletRequest;
    private Date expiryDate = new Date();
    @Override
    public String createToken(String username){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("username",user.getUsername());
            claims.put("userID", user.getUserUuid());
            claims.put("privileges",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
            expiryDate =new Date(new Date().getTime() + getSecretKey().getExpirationTimeInSeconds());
            String token = Jwts.builder()
                    .setIssuer("ECDMS")
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, getSecretKey().getSecretKey())
                    .compact();
            String uuid = tokenSave(token, user);
            if(uuid != null){
                return uuid;
            }else {
                throw new InternalServerErrorException("Token saving unsuccessful");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new InternalServerErrorException("Require service unavailable.");
        }
    }
    private String tokenSave(String token, User user){
        try {
            String clientIp = httpServletRequest.getHeader("X-Forwarded-For");
            String userAgent = httpServletRequest.getHeader("User-Agent");
            String deviceType = httpServletRequest.getHeader("X-Device-Type");
            if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = httpServletRequest.getRemoteAddr();
            }
            ZoneId colomboZone = ZoneId.of("Asia/Colombo");
            Date expireAt = new Date();
            TokenHistory tokenHistory = new TokenHistory(
                    token,
                    false,
                    false,
                    deviceType != null ? deviceType:"WEB",
                    UUID.randomUUID().toString(),
                    expireAt,
                    true,
                    TokenType.ACCESS_TOKEN.toString(),
                    user
            );
            tokenHistoryRepository.save(tokenHistory);
            return tokenHistory.getTokenUuid();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey().getSecretKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    @Override
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSecretKey().getSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
    private TokenConfiguration getSecretKey(){
        Optional<TokenConfiguration> byStatusAndType = tokenConfigurationRepository.findByStatusAndType(TokenType.ACCESS_TOKEN.toString(), Status.ACTIVE.toString());
        if (!byStatusAndType.isPresent()){
            throw new InternalServerErrorException("Require service unavailable.");
        }else {
            return byStatusAndType.get();
        }
    }
}

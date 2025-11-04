/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.com.fms_core.entity.User;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenHistoryDTO {
    private Integer tokenId;
    private String deviceType;
    private boolean expireStatus;
    private Date expiredAt;
    private Date issuedAt;
    private boolean revokeStatus;
    private boolean status;
    private String token;
    private String tokenUuid;
    private String type;
    private Date updatedAt;
    private Integer userId;
    private UserDTO user;
}

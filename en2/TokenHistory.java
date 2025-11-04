/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name = "token_history")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false)
    private Integer tokenId;
    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    private String token;
    @CreationTimestamp
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "revoke_status", nullable = false)
    private boolean revokeStatus;
    @Column(name = "expire_status", nullable = false)
    private boolean expireStatus;
    @Column(name = "device_type", nullable = false)
    private String deviceType;
    @Column(name = "token_uuid", nullable = false)
    private String tokenUuid;
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;
    @Column(name = "status", nullable = false)
    private Boolean status;
    @Column(name = "type", nullable = false)
    private String type;
    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;
    public TokenHistory(String token, boolean revokeStatus, boolean expireStatus, String deviceType, String tokenUuid, LocalDateTime expiredAt, Boolean status, String type, User user) {
        this.token = token;
        this.revokeStatus = revokeStatus;
        this.expireStatus = expireStatus;
        this.deviceType = deviceType;
        this.tokenUuid = tokenUuid;
        this.expiredAt = expiredAt;
        this.status = status;
        this.type = type;
        this.user = user;
    }
}

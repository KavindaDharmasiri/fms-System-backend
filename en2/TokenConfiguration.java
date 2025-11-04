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
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name = "token_configuration")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenConfiguration implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_config_id", nullable = false)
    private Integer tokenConfigId;
    @Column(name = "version", nullable = false)
    private String version;
    @Column(name = "token_type", nullable = false)
    private String tokenType;
    @Column(name = "expiration_time_in_seconds", nullable = false)
    private Integer expirationTimeInSeconds;
    @Column(name = "secret_key", nullable = false)
    private String secretKey;
    @Column(name = "status", nullable = false)
    private String status; // e.g., "ACTIVE", "INACTIVE"
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;
}

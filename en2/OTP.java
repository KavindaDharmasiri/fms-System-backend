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
@Table(name = "otp")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OTP implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id", nullable = false)
    private Integer otpId;
    @Column(name = "otp", nullable = false)
    private Integer otp;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "expire_at", nullable = false)
    private LocalDateTime expireAt;
    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;
    @Column(name = "device_type", nullable = false)
    private String deviceType;
}

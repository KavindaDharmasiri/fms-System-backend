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
public class OtpDTO {
    private Integer otpId;
    private Date createdAt;
    private String createdBy;
    private String deviceType;
    private Date expireAt;
    private int otp;
    private String type;
    private Date updatedAt;
    private String updatedBy;
    private Integer userId;
    private UserDTO user;
}

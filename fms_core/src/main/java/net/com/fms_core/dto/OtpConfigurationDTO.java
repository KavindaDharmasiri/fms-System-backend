/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpConfigurationDTO {
    private Integer otpConfigId;
    private Date createdAt;
    private String createdBy;
    private int digits;
    private int expirationTimeInSeconds;
    private String secretKey;
    private String status;
    private Date updatedAt;
    private String updatedBy;
    private String version;
}

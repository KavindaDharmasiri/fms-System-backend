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
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private String userUuid;
    private String userName;
    private String fullName;
    private String nic;
    private String userCategory;
    private String empId;
    private String email;
    private String contactNo;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer loginAttempts;
    private String password;
    private String address;
    private String city;
    private String contact;
    private Collection<UserRoleDTO> userRoleCollection;
    private Collection<TokenHistoryDTO> tokenHistoryCollection;
    private Collection<OtpDTO> otpCollection;
}

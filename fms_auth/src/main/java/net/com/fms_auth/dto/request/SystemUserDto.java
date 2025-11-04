/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemUserDto {
    private Integer userId;
    private String userCategory;
    private String username;
    private String nic;
    private List<Integer> userRole;
    private String contact;
    private String email;
    private String empId;
    private String fullName;
    private String city;
    private String address;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String password;
}

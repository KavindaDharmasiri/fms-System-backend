/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable , UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(name = "user_uuid", nullable = false, unique = true)
    private String userUuid;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "login_attempts", nullable = false)
    private Integer loginAttempts;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user")
    private List<UserRoleDetails>  roleDetailsList;
    @OneToMany(mappedBy = "user")
    private List<OTP>  otpList;
    @OneToMany(mappedBy = "user")
    private List<TokenHistory> tokenHistoryList;
    @OneToOne(mappedBy = "user")
    private SysUser sysUser;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roleDetailsList.forEach(
                userRoleDetails -> {
                    userRoleDetails.getRole().getRolePrivilegeDetails().forEach(
                            rolePrivilegeDetails -> {
                                String permissionCode = rolePrivilegeDetails.getPrivilege().getPermissionCode();
                                authorities.add(() -> permissionCode );
                            }
                    );
                }
        );
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
       return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserUuid() {
        return userUuid;
    }
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getLoginAttempts() {
        return loginAttempts;
    }
    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }
    public List<UserRoleDetails> getRoleDetailsList() {
        return roleDetailsList;
    }
    public void setRoleDetailsList(List<UserRoleDetails> roleDetailsList) {
        this.roleDetailsList = roleDetailsList;
    }
    public List<OTP> getOtpList() {
        return otpList;
    }
    public void setOtpList(List<OTP> otpList) {
        this.otpList = otpList;
    }
    public List<TokenHistory> getTokenHistoryList() {
        return tokenHistoryList;
    }
    public void setTokenHistoryList(List<TokenHistory> tokenHistoryList) {
        this.tokenHistoryList = tokenHistoryList;
    }
}

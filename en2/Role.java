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
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, unique = true)
    private Integer roleId;
    @Column(name = "role_name", nullable = false)
    private String roleName;
    @Column(name = "role_code", nullable = false, unique = true)
    private String roleCode;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "role")
    private List<RolePrivilegeDetails> rolePrivilegeDetails;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "role")
    private List<UserRoleDetails> userRoleDetails;
}

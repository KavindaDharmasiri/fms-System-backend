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
import java.time.LocalDateTime;
@Entity
@Table(name = "role_privilege_details")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolePrivilegeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_privilege_id", nullable = false)
    private Integer rolePrivilegeId;
    @ManyToOne
    @JoinColumn(name = "privilege_id", nullable = false)
    private Privilege privilege;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "created_by",length = 45 , nullable = false)
    private String createdBy;
    @Column(name = "updated_by",length = 45 , nullable = false)
    private String updatedBy;
}

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
import java.util.List;
@Entity
@Table(name = "privilege")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Privilege implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privilege_id", nullable = false, unique = true)
    private Integer privilegeId;
    @Column(name = "permission_code", nullable = false, unique = true)
    private String permissionCode;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "page_code", nullable = false)
    private String pageCode;
    @Column(name = "section_code", nullable = false)
    private String sectionCode;
    @Column(name = "task", nullable = false)
    private String task;
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;
    @Column(name = "upated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "status", nullable = false)
    private String status;
    @OneToMany(mappedBy = "privilege")
    private List<RolePrivilegeDetails> rolePrivilegeDetails;
}

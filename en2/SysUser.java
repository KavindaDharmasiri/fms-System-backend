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
@Table(name = "sys_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userID;
    @Column(name = "user_category", length = 45 ,nullable = false)
    private String userCategory;
    @Column(name = "nic", length = 45 ,nullable = false)
    private String nic;
    @Column(name = "contact",length = 45 , nullable = false)
    private String contact;
    @Column(name = "email",length = 100 , nullable = false)
    private String email;
    @Column(name = "emp_id",length = 45 , nullable = false)
    private String employeeID;
    @Column(name = "full_name",length = 225 , nullable = false)
    private String fullName;
    @Column(name = "city",length = 45 , nullable = false)
    private String city;
    @Column(name = "address",length = 225 , nullable = false)
    private String address;
    @Column(name = "status",length = 45 , nullable = false)
    private String status;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "created_by",length = 45 , nullable = false)
    private String createdBy;
    @Column(name = "updated_by",length = 45 , nullable = false)
    private String updatedBy;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true)
    private User user;
}

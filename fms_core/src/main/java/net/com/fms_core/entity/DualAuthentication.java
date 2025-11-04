/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.entity; 
 import jakarta.persistence.*;
 import lombok.AllArgsConstructor;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import org.hibernate.annotations.CreationTimestamp;
 import org.hibernate.annotations.UpdateTimestamp;
 import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "dual_authentication")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DualAuthentication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dual_authentication_id")
    private Integer dualAuthenticationId;
    @Column(name = "last_modified_user")
    private String lastModifiedUser;
    @Column(name = "new_modified_user")
    private String newModifiedUser;
    @Column(name = "task")
    private String task;
    @Column(name = "modified_feild")
    private String modifiedFeild;
    @Lob
    @Column(name = "old_value")
    private String oldValue;
    @Lob
    @Column(name = "new_value")
    private String newValue;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "approved_by")
    private String approvedBy;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "group_id")
    private String groupID;
    @Column(name = "rejected_reason")
    private String rejectedReason;
    @Column(name = "configuration")
    private String configuration;
}

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.entity;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "dual_authentication")
@NamedQueries({
    @NamedQuery(name = "DualAuthentication.findAll", query = "SELECT d FROM DualAuthentication d")
    , @NamedQuery(name = "DualAuthentication.findByDualAuthenticationId", query = "SELECT d FROM DualAuthentication d WHERE d.dualAuthenticationId = :dualAuthenticationId")
    , @NamedQuery(name = "DualAuthentication.findByModifiedUser", query = "SELECT d FROM DualAuthentication d WHERE d.modifiedUser = :modifiedUser")
    , @NamedQuery(name = "DualAuthentication.findByTask", query = "SELECT d FROM DualAuthentication d WHERE d.task = :task")
    , @NamedQuery(name = "DualAuthentication.findByModifiedFeild", query = "SELECT d FROM DualAuthentication d WHERE d.modifiedFeild = :modifiedFeild")
    , @NamedQuery(name = "DualAuthentication.findByDate", query = "SELECT d FROM DualAuthentication d WHERE d.date = :date")
    , @NamedQuery(name = "DualAuthentication.findByApprovedBy", query = "SELECT d FROM DualAuthentication d WHERE d.approvedBy = :approvedBy")
    , @NamedQuery(name = "DualAuthentication.findByStatus", query = "SELECT d FROM DualAuthentication d WHERE d.status = :status")
    , @NamedQuery(name = "DualAuthentication.findByCreatedAt", query = "SELECT d FROM DualAuthentication d WHERE d.createdAt = :createdAt")
    , @NamedQuery(name = "DualAuthentication.findByUpdatedAt", query = "SELECT d FROM DualAuthentication d WHERE d.updatedAt = :updatedAt")
    , @NamedQuery(name = "DualAuthentication.findByCreatedBy", query = "SELECT d FROM DualAuthentication d WHERE d.createdBy = :createdBy")
    , @NamedQuery(name = "DualAuthentication.findByUpdatedBy", query = "SELECT d FROM DualAuthentication d WHERE d.updatedBy = :updatedBy")})
public class DualAuthentication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dual_authentication_id")
    private Integer dualAuthenticationId;
    @Column(name = "modified_user")
    private String modifiedUser;
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
    public DualAuthentication() {
    }
    public DualAuthentication(Integer dualAuthenticationId) {
        this.dualAuthenticationId = dualAuthenticationId;
    }
    public DualAuthentication(Integer dualAuthenticationId, Date date, String status, Date createdAt, Date updatedAt) {
        this.dualAuthenticationId = dualAuthenticationId;
        this.date = date;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getDualAuthenticationId() {
        return dualAuthenticationId;
    }
    public void setDualAuthenticationId(Integer dualAuthenticationId) {
        this.dualAuthenticationId = dualAuthenticationId;
    }
    public String getModifiedUser() {
        return modifiedUser;
    }
    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }
    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public String getModifiedFeild() {
        return modifiedFeild;
    }
    public void setModifiedFeild(String modifiedFeild) {
        this.modifiedFeild = modifiedFeild;
    }
    public String getOldValue() {
        return oldValue;
    }
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    public String getNewValue() {
        return newValue;
    }
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getApprovedBy() {
        return approvedBy;
    }
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
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
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dualAuthenticationId != null ? dualAuthenticationId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DualAuthentication)) {
            return false;
        }
        DualAuthentication other = (DualAuthentication) object;
        if ((this.dualAuthenticationId == null && other.dualAuthenticationId != null) || (this.dualAuthenticationId != null && !this.dualAuthenticationId.equals(other.dualAuthenticationId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.DualAuthentication[ dualAuthenticationId=" + dualAuthenticationId + " ]";
    }
}

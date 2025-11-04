/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.entity; 
 import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "role_privilege")
@NamedQueries({
    @NamedQuery(name = "RolePrivilege.findAll", query = "SELECT r FROM RolePrivilege r")
    , @NamedQuery(name = "RolePrivilege.findByRolePrivilegeId", query = "SELECT r FROM RolePrivilege r WHERE r.rolePrivilegeId = :rolePrivilegeId")
    , @NamedQuery(name = "RolePrivilege.findByStatus", query = "SELECT r FROM RolePrivilege r WHERE r.status = :status")
    , @NamedQuery(name = "RolePrivilege.findByCreated", query = "SELECT r FROM RolePrivilege r WHERE r.created = :created")
    , @NamedQuery(name = "RolePrivilege.findByUpdated", query = "SELECT r FROM RolePrivilege r WHERE r.updated = :updated")
    , @NamedQuery(name = "RolePrivilege.findByCreatedBy", query = "SELECT r FROM RolePrivilege r WHERE r.createdBy = :createdBy")
    , @NamedQuery(name = "RolePrivilege.findByUpdatedBy", query = "SELECT r FROM RolePrivilege r WHERE r.updatedBy = :updatedBy")
    , @NamedQuery(name = "RolePrivilege.findByCreatedAt", query = "SELECT r FROM RolePrivilege r WHERE r.createdAt = :createdAt")
    , @NamedQuery(name = "RolePrivilege.findByUpdatedAt", query = "SELECT r FROM RolePrivilege r WHERE r.updatedAt = :updatedAt")})
public class RolePrivilege implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "role_privilege_id")
    private Integer rolePrivilegeId;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "privilege_id", referencedColumnName = "privilege_id")
    @ManyToOne(optional = false)
    private Privilege privilegeId;
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne(optional = false)
    private Role roleId;
    public RolePrivilege() {
    }
    public RolePrivilege(Integer rolePrivilegeId) {
        this.rolePrivilegeId = rolePrivilegeId;
    }
    public RolePrivilege(Integer rolePrivilegeId, String status, Date created, Date updated, Date createdAt, Date updatedAt) {
        this.rolePrivilegeId = rolePrivilegeId;
        this.status = status;
        this.created = created;
        this.updated = updated;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getRolePrivilegeId() {
        return rolePrivilegeId;
    }
    public void setRolePrivilegeId(Integer rolePrivilegeId) {
        this.rolePrivilegeId = rolePrivilegeId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    public Date getUpdated() {
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
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
    public Privilege getPrivilegeId() {
        return privilegeId;
    }
    public void setPrivilegeId(Privilege privilegeId) {
        this.privilegeId = privilegeId;
    }
    public Role getRoleId() {
        return roleId;
    }
    public void setRoleId(Role roleId) {
        this.roleId = roleId;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolePrivilegeId != null ? rolePrivilegeId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RolePrivilege)) {
            return false;
        }
        RolePrivilege other = (RolePrivilege) object;
        if ((this.rolePrivilegeId == null && other.rolePrivilegeId != null) || (this.rolePrivilegeId != null && !this.rolePrivilegeId.equals(other.rolePrivilegeId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.RolePrivilege[ rolePrivilegeId=" + rolePrivilegeId + " ]";
    }
}

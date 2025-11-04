/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.entity;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "role")
@NamedQueries({
    @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
    , @NamedQuery(name = "Role.findByRoleId", query = "SELECT r FROM Role r WHERE r.roleId = :roleId")
    , @NamedQuery(name = "Role.findByRoleCode", query = "SELECT r FROM Role r WHERE r.roleCode = :roleCode")
    , @NamedQuery(name = "Role.findByRoleName", query = "SELECT r FROM Role r WHERE r.roleName = :roleName")
    , @NamedQuery(name = "Role.findByStatus", query = "SELECT r FROM Role r WHERE r.status = :status")
    , @NamedQuery(name = "Role.findByCreatedAt", query = "SELECT r FROM Role r WHERE r.createdAt = :createdAt")
    , @NamedQuery(name = "Role.findByUpdatedAt", query = "SELECT r FROM Role r WHERE r.updatedAt = :updatedAt")
    , @NamedQuery(name = "Role.findByCreatedBy", query = "SELECT r FROM Role r WHERE r.createdBy = :createdBy")
    , @NamedQuery(name = "Role.findByUpdatedBy", query = "SELECT r FROM Role r WHERE r.updatedBy = :updatedBy")})
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "role_id")
    private Integer roleId;
    @Basic(optional = false)
    @Column(name = "role_code")
    private String roleCode;
    @Basic(optional = false)
    @Column(name = "role_name")
    private String roleName;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId")
    private Collection<UserRole> userRoleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId")
    private Collection<RolePrivilege> rolePrivilegeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId")
    private Collection<RuleGroupRole> ruleGroupRoleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId")
    private Collection<ReactionTemplateRole> reactionTemplateRoleCollection;
    public Role() {
    }
    public Collection<UserRole> getUserRoleCollection() {
        return userRoleCollection;
    }
    public Collection<RolePrivilege> getRolePrivilegeCollection() {
        return rolePrivilegeCollection;
    }
    public Collection<RuleGroupRole> getRuleGroupRoleCollection() {
        return ruleGroupRoleCollection;
    }
    public Collection<ReactionTemplateRole> getReactionTemplateRoleCollection() {
        return reactionTemplateRoleCollection;
    }
    public Role(Integer roleId) {
        this.roleId = roleId;
    }
    public Role(Integer roleId, String roleCode, String roleName, String status, Date createdAt, Date updatedAt) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getRoleId() {
        return roleId;
    }
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    public String getRoleCode() {
        return roleCode;
    }
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
    public void setUserRoleCollection(Collection<UserRole> userRoleCollection) {
        this.userRoleCollection = userRoleCollection;
    }
    public void setRolePrivilegeCollection(Collection<RolePrivilege> rolePrivilegeCollection) {
        this.rolePrivilegeCollection = rolePrivilegeCollection;
    }
    public void setRuleGroupRoleCollection(Collection<RuleGroupRole> ruleGroupRoleCollection) {
        this.ruleGroupRoleCollection = ruleGroupRoleCollection;
    }
    public void setReactionTemplateRoleCollection(Collection<ReactionTemplateRole> reactionTemplateRoleCollection) {
        this.reactionTemplateRoleCollection = reactionTemplateRoleCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.Role[ roleId=" + roleId + " ]";
    }
}

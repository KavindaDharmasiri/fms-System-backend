/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.entity; 
 import jakarta.persistence.*;
 import org.hibernate.annotations.CreationTimestamp;
 import org.hibernate.annotations.UpdateTimestamp;
 import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reaction_template_role")
@NamedQueries({
    @NamedQuery(name = "ReactionTemplateRole.findAll", query = "SELECT r FROM ReactionTemplateRole r")
    , @NamedQuery(name = "ReactionTemplateRole.findByReactionTemplateRoleId", query = "SELECT r FROM ReactionTemplateRole r WHERE r.reactionTemplateRoleId = :reactionTemplateRoleId")
    , @NamedQuery(name = "ReactionTemplateRole.findByStatus", query = "SELECT r FROM ReactionTemplateRole r WHERE r.status = :status")
    , @NamedQuery(name = "ReactionTemplateRole.findByCreatedAt", query = "SELECT r FROM ReactionTemplateRole r WHERE r.createdAt = :createdAt")
    , @NamedQuery(name = "ReactionTemplateRole.findByUpdatedAt", query = "SELECT r FROM ReactionTemplateRole r WHERE r.updatedAt = :updatedAt")
    , @NamedQuery(name = "ReactionTemplateRole.findByCreatedBy", query = "SELECT r FROM ReactionTemplateRole r WHERE r.createdBy = :createdBy")
    , @NamedQuery(name = "ReactionTemplateRole.findByUpdatedBy", query = "SELECT r FROM ReactionTemplateRole r WHERE r.updatedBy = :updatedBy")})
public class ReactionTemplateRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "reaction_template_role_id")
    private Integer reactionTemplateRoleId;
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
    @JoinColumn(name = "reaction_template_id", referencedColumnName = "reaction_template_id")
    @ManyToOne(optional = false)
    private ReactionTemplate reactionTemplateId;
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne(optional = false)
    private Role roleId;
    public ReactionTemplateRole() {
    }
    public ReactionTemplateRole(Integer reactionTemplateRoleId) {
        this.reactionTemplateRoleId = reactionTemplateRoleId;
    }
    public ReactionTemplateRole(Integer reactionTemplateRoleId, String status, Date createdAt, Date updatedAt) {
        this.reactionTemplateRoleId = reactionTemplateRoleId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getReactionTemplateRoleId() {
        return reactionTemplateRoleId;
    }
    public void setReactionTemplateRoleId(Integer reactionTemplateRoleId) {
        this.reactionTemplateRoleId = reactionTemplateRoleId;
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
    public ReactionTemplate getReactionTemplateId() {
        return reactionTemplateId;
    }
    public void setReactionTemplateId(ReactionTemplate reactionTemplateId) {
        this.reactionTemplateId = reactionTemplateId;
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
        hash += (reactionTemplateRoleId != null ? reactionTemplateRoleId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReactionTemplateRole)) {
            return false;
        }
        ReactionTemplateRole other = (ReactionTemplateRole) object;
        if ((this.reactionTemplateRoleId == null && other.reactionTemplateRoleId != null) || (this.reactionTemplateRoleId != null && !this.reactionTemplateRoleId.equals(other.reactionTemplateRoleId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.ReactionTemplateRole[ reactionTemplateRoleId=" + reactionTemplateRoleId + " ]";
    }
}

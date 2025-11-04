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

@Entity
@Table(name = "privilege")
@NamedQueries({
    @NamedQuery(name = "Privilege.findAll", query = "SELECT p FROM Privilege p")
    , @NamedQuery(name = "Privilege.findByPrivilegeId", query = "SELECT p FROM Privilege p WHERE p.privilegeId = :privilegeId")
    , @NamedQuery(name = "Privilege.findByPrivilegeName", query = "SELECT p FROM Privilege p WHERE p.privilegeName = :privilegeName")
    , @NamedQuery(name = "Privilege.findByPage", query = "SELECT p FROM Privilege p WHERE p.page = :page")
    , @NamedQuery(name = "Privilege.findBySection", query = "SELECT p FROM Privilege p WHERE p.section = :section")
    , @NamedQuery(name = "Privilege.findByStatus", query = "SELECT p FROM Privilege p WHERE p.status = :status")
    , @NamedQuery(name = "Privilege.findByCreatedAt", query = "SELECT p FROM Privilege p WHERE p.createdAt = :createdAt")
    , @NamedQuery(name = "Privilege.findByUpdatedAt", query = "SELECT p FROM Privilege p WHERE p.updatedAt = :updatedAt")
    , @NamedQuery(name = "Privilege.findByCreatedBy", query = "SELECT p FROM Privilege p WHERE p.createdBy = :createdBy")
    , @NamedQuery(name = "Privilege.findByUpdatedBy", query = "SELECT p FROM Privilege p WHERE p.updatedBy = :updatedBy")})
public class Privilege implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "privilege_id")
    private Integer privilegeId;
    @Basic(optional = false)
    @Column(name = "privilege_name")
    private String privilegeName;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "page")
    private String page;
    @Column(name = "section")
    private String section;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "privilegeId")
    private Collection<RolePrivilege> rolePrivilegeCollection;
    public Privilege() {
    }
    public Privilege(Integer privilegeId) {
        this.privilegeId = privilegeId;
    }
    public Privilege(Integer privilegeId, String privilegeName, String status, Date createdAt, Date updatedAt) {
        this.privilegeId = privilegeId;
        this.privilegeName = privilegeName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getPrivilegeId() {
        return privilegeId;
    }
    public void setPrivilegeId(Integer privilegeId) {
        this.privilegeId = privilegeId;
    }
    public String getPrivilegeName() {
        return privilegeName;
    }
    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
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
    public void setRolePrivilegeCollection(Collection<RolePrivilege> rolePrivilegeCollection) {
        this.rolePrivilegeCollection = rolePrivilegeCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (privilegeId != null ? privilegeId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Privilege)) {
            return false;
        }
        Privilege other = (Privilege) object;
        if ((this.privilegeId == null && other.privilegeId != null) || (this.privilegeId != null && !this.privilegeId.equals(other.privilegeId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.Privilege[ privilegeId=" + privilegeId + " ]";
    }
}

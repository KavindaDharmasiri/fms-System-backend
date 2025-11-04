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
@Table(name = "field_dependencies")
@NamedQueries({
    @NamedQuery(name = "FieldDependencies.findAll", query = "SELECT f FROM FieldDependencies f")
    , @NamedQuery(name = "FieldDependencies.findByFieldDependenciesId", query = "SELECT f FROM FieldDependencies f WHERE f.fieldDependenciesId = :fieldDependenciesId")
    , @NamedQuery(name = "FieldDependencies.findByMainOperator", query = "SELECT f FROM FieldDependencies f WHERE f.mainOperator = :mainOperator")
    , @NamedQuery(name = "FieldDependencies.findByValue", query = "SELECT f FROM FieldDependencies f WHERE f.value = :value")
    , @NamedQuery(name = "FieldDependencies.findByDepOperator", query = "SELECT f FROM FieldDependencies f WHERE f.depOperator = :depOperator")
    , @NamedQuery(name = "FieldDependencies.findByDepValue", query = "SELECT f FROM FieldDependencies f WHERE f.depValue = :depValue")
    , @NamedQuery(name = "FieldDependencies.findByStatus", query = "SELECT f FROM FieldDependencies f WHERE f.status = :status")
    , @NamedQuery(name = "FieldDependencies.findByCreatedAt", query = "SELECT f FROM FieldDependencies f WHERE f.createdAt = :createdAt")
    , @NamedQuery(name = "FieldDependencies.findByUpdatedAt", query = "SELECT f FROM FieldDependencies f WHERE f.updatedAt = :updatedAt")
    , @NamedQuery(name = "FieldDependencies.findByCreatedBy", query = "SELECT f FROM FieldDependencies f WHERE f.createdBy = :createdBy")
    , @NamedQuery(name = "FieldDependencies.findByUpdatedBy", query = "SELECT f FROM FieldDependencies f WHERE f.updatedBy = :updatedBy")})
public class FieldDependencies implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "field_dependencies_id")
    private Integer fieldDependenciesId;
    @Column(name = "main_operator")
    private String mainOperator;
    @Column(name = "value")
    private String value;
    @Column(name = "dep_operator")
    private String depOperator;
    @Column(name = "dep_value")
    private String depValue;
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @JoinColumn(name = "dep_element_id", referencedColumnName = "fms_element_id")
    @ManyToOne
    private FmsElement depElementId;
    @JoinColumn(name = "fms_element_id", referencedColumnName = "fms_element_id")
    @ManyToOne(optional = false)
    private FmsElement fmsElementId;
    public FieldDependencies() {
    }
    public FieldDependencies(Integer fieldDependenciesId) {
        this.fieldDependenciesId = fieldDependenciesId;
    }
    public FieldDependencies(Integer fieldDependenciesId, Date createdAt, Date updatedAt) {
        this.fieldDependenciesId = fieldDependenciesId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getFieldDependenciesId() {
        return fieldDependenciesId;
    }
    public void setFieldDependenciesId(Integer fieldDependenciesId) {
        this.fieldDependenciesId = fieldDependenciesId;
    }
    public String getMainOperator() {
        return mainOperator;
    }
    public void setMainOperator(String mainOperator) {
        this.mainOperator = mainOperator;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getDepOperator() {
        return depOperator;
    }
    public void setDepOperator(String depOperator) {
        this.depOperator = depOperator;
    }
    public String getDepValue() {
        return depValue;
    }
    public void setDepValue(String depValue) {
        this.depValue = depValue;
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
    public FmsElement getDepElementId() {
        return depElementId;
    }
    public void setDepElementId(FmsElement depElementId) {
        this.depElementId = depElementId;
    }
    public FmsElement getFmsElementId() {
        return fmsElementId;
    }
    public void setFmsElementId(FmsElement fmsElementId) {
        this.fmsElementId = fmsElementId;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fieldDependenciesId != null ? fieldDependenciesId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FieldDependencies)) {
            return false;
        }
        FieldDependencies other = (FieldDependencies) object;
        if ((this.fieldDependenciesId == null && other.fieldDependenciesId != null) || (this.fieldDependenciesId != null && !this.fieldDependenciesId.equals(other.fieldDependenciesId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.FieldDependencies[ fieldDependenciesId=" + fieldDependenciesId + " ]";
    }
}

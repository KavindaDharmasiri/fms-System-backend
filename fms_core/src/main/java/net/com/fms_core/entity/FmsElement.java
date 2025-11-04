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
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "fms_element")
@NamedQueries({
    @NamedQuery(name = "FmsElement.findAll", query = "SELECT e FROM FmsElement e")
    , @NamedQuery(name = "FmsElement.findByfmsElementId", query = "SELECT e FROM FmsElement e WHERE e.fmsElementId = :fmsElementId")
    , @NamedQuery(name = "FmsElement.findByElementCode", query = "SELECT e FROM FmsElement e WHERE e.elementCode = :elementCode")
    , @NamedQuery(name = "FmsElement.findByElementName", query = "SELECT e FROM FmsElement e WHERE e.elementName = :elementName")
    , @NamedQuery(name = "FmsElement.findByRiskWeight", query = "SELECT e FROM FmsElement e WHERE e.riskWeight = :riskWeight")
    , @NamedQuery(name = "FmsElement.findByStatus", query = "SELECT e FROM FmsElement e WHERE e.status = :status")
    , @NamedQuery(name = "FmsElement.findByCreated", query = "SELECT e FROM FmsElement e WHERE e.created = :created")
    , @NamedQuery(name = "FmsElement.findByUpdated", query = "SELECT e FROM FmsElement e WHERE e.updated = :updated")
    , @NamedQuery(name = "FmsElement.findByCreatedBy", query = "SELECT e FROM FmsElement e WHERE e.createdBy = :createdBy")
    , @NamedQuery(name = "FmsElement.findByUpdatedBy", query = "SELECT e FROM FmsElement e WHERE e.updatedBy = :updatedBy")})
public class FmsElement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fms_element_id")
    private Integer fmsElementId;
    @Basic(optional = false)
    @Column(name = "element_code")
    private String elementCode;
    @Basic(optional = false)
    @Column(name = "element_name")
    private String elementName;
    @Basic(optional = false)
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Lob
    @Column(name = "variable_name")
    private String variableName;
    @Basic(optional = false)
    @Lob
    @Column(name = "validation")
    private String validation;
    @Basic(optional = false)
    @Lob
    @Column(name = "validation_message")
    private String validationMessage;
    @Basic(optional = false)
    @Column(name = "risk_weight")
    private double riskWeight;
    @Basic(optional = false)
    @Lob
    @Column(name = "operator")
    private String operator;
    @Basic(optional = false)
    @Lob
    @Column(name = "value")
    private String value;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "created")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "updated")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Basic(optional = false)
    @Column(name = "created_by")
    private String createdBy;
    @Basic(optional = false)
    @Column(name = "updated_by")
    private String updatedBy;
    @JoinColumn(name = "payment_network", referencedColumnName = "payment_network_id")
    @ManyToOne
    private PaymentNetwork paymentNetwork;
    public PaymentNetwork getPaymentNetwork() {
        return paymentNetwork;
    }
    public void setPaymentNetwork(PaymentNetwork paymentNetwork) {
        this.paymentNetwork = paymentNetwork;
    }
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fmsElementId")
    private Collection<FmsRuleCondition> fmsRuleConditionCollection;
    @OneToMany(mappedBy = "depElementId")
    private Collection<FieldDependencies> fieldDependenciesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fmsElementId")
    private Collection<FieldDependencies> fieldDependenciesCollection1;
    public FmsElement() {
    }
    public FmsElement(Integer fmsElementId) {
        this.fmsElementId = fmsElementId;
    }
    public FmsElement(Integer fmsElementId, String elementCode, String elementName, String description, String validation, String validationMessage, double riskWeight, String operator, String value, String status, Date created, Date updated, String createdBy, String updatedBy) {
        this.fmsElementId = fmsElementId;
        this.elementCode = elementCode;
        this.elementName = elementName;
        this.description = description;
        this.validation = validation;
        this.validationMessage = validationMessage;
        this.riskWeight = riskWeight;
        this.operator = operator;
        this.value = value;
        this.status = status;
        this.created = created;
        this.updated = updated;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
    public String getVariableName() {
        return variableName;
    }
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
    public Integer getFmsElementId() {
        return fmsElementId;
    }
    public void setFmsElementId(Integer fmsElementId) {
        this.fmsElementId = fmsElementId;
    }
    public String getElementCode() {
        return elementCode;
    }
    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }
    public String getElementName() {
        return elementName;
    }
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getValidation() {
        return validation;
    }
    public void setValidation(String validation) {
        this.validation = validation;
    }
    public String getValidationMessage() {
        return validationMessage;
    }
    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
    public double getRiskWeight() {
        return riskWeight;
    }
    public void setRiskWeight(double riskWeight) {
        this.riskWeight = riskWeight;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
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
    public Collection<FmsRuleCondition> getFmsRuleConditionCollection() {
        return fmsRuleConditionCollection;
    }
    public void setFmsRuleConditionCollection(Collection<FmsRuleCondition> fmsRuleConditionCollection) {
        this.fmsRuleConditionCollection = fmsRuleConditionCollection;
    }
    public Collection<FieldDependencies> getFieldDependenciesCollection() {
        return fieldDependenciesCollection;
    }
    public void setFieldDependenciesCollection(Collection<FieldDependencies> fieldDependenciesCollection) {
        this.fieldDependenciesCollection = fieldDependenciesCollection;
    }
    public Collection<FieldDependencies> getFieldDependenciesCollection1() {
        return fieldDependenciesCollection1;
    }
    public void setFieldDependenciesCollection1(Collection<FieldDependencies> fieldDependenciesCollection1) {
        this.fieldDependenciesCollection1 = fieldDependenciesCollection1;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fmsElementId != null ? fmsElementId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FmsElement)) {
            return false;
        }
        FmsElement other = (FmsElement) object;
        if ((this.fmsElementId == null && other.fmsElementId != null) || (this.fmsElementId != null && !this.fmsElementId.equals(other.fmsElementId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.FmsElement[ fmsElementId=" + fmsElementId + " ]";
    }
}

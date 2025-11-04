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
@Table(name = "payment_network")
@NamedQueries({
    @NamedQuery(name = "PaymentNetwork.findAll", query = "SELECT p FROM PaymentNetwork p")
    , @NamedQuery(name = "PaymentNetwork.findByPaymentNetworkId", query = "SELECT p FROM PaymentNetwork p WHERE p.paymentNetworkId = :paymentNetworkId")
    , @NamedQuery(name = "PaymentNetwork.findByNetworkName", query = "SELECT p FROM PaymentNetwork p WHERE p.networkName = :networkName")
    , @NamedQuery(name = "PaymentNetwork.findByBin", query = "SELECT p FROM PaymentNetwork p WHERE p.bin = :bin")
    , @NamedQuery(name = "PaymentNetwork.findByBinLength", query = "SELECT p FROM PaymentNetwork p WHERE p.binLength = :binLength")
    , @NamedQuery(name = "PaymentNetwork.findByStatus", query = "SELECT p FROM PaymentNetwork p WHERE p.status = :status")
    , @NamedQuery(name = "PaymentNetwork.findByCreatedBy", query = "SELECT p FROM PaymentNetwork p WHERE p.createdBy = :createdBy")
    , @NamedQuery(name = "PaymentNetwork.findByUpdatedBy", query = "SELECT p FROM PaymentNetwork p WHERE p.updatedBy = :updatedBy")
    , @NamedQuery(name = "PaymentNetwork.findByCreatedAt", query = "SELECT p FROM PaymentNetwork p WHERE p.createdAt = :createdAt")
    , @NamedQuery(name = "PaymentNetwork.findByUpdatedAt", query = "SELECT p FROM PaymentNetwork p WHERE p.updatedAt = :updatedAt")})
public class PaymentNetwork implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "payment_network_id")
    private Integer paymentNetworkId;
    @Column(name = "network_name")
    private String networkName;
    @Column(name = "bin")
    private Double bin;
    @Column(name = "bin_length")
    private Double binLength;
    @Column(name = "status")
    private String status;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentNetworkId")
    private Collection<FmsRule> FmsRuleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentNetworkId")
    private Collection<RuleGroup> ruleGroupCollection;
    public PaymentNetwork() {
    }
    public PaymentNetwork(Integer paymentNetworkId) {
        this.paymentNetworkId = paymentNetworkId;
    }
    public PaymentNetwork(Integer paymentNetworkId, Date createdAt, Date updatedAt) {
        this.paymentNetworkId = paymentNetworkId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Integer getPaymentNetworkId() {
        return paymentNetworkId;
    }
    public void setPaymentNetworkId(Integer paymentNetworkId) {
        this.paymentNetworkId = paymentNetworkId;
    }
    public String getNetworkName() {
        return networkName;
    }
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    public Double getBin() {
        return bin;
    }
    public void setBin(Double bin) {
        this.bin = bin;
    }
    public Double getBinLength() {
        return binLength;
    }
    public void setBinLength(Double binLength) {
        this.binLength = binLength;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    public void setFmsRuleCollection(Collection<FmsRule> FmsRuleCollection) {
        this.FmsRuleCollection = FmsRuleCollection;
    }
    public void setRuleGroupCollection(Collection<RuleGroup> ruleGroupCollection) {
        this.ruleGroupCollection = ruleGroupCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentNetworkId != null ? paymentNetworkId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PaymentNetwork)) {
            return false;
        }
        PaymentNetwork other = (PaymentNetwork) object;
        if ((this.paymentNetworkId == null && other.paymentNetworkId != null) || (this.paymentNetworkId != null && !this.paymentNetworkId.equals(other.paymentNetworkId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.PaymentNetwork[ paymentNetworkId=" + paymentNetworkId + " ]";
    }
}

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.entity;
import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "risk_metrix")
@NamedQueries({
    @NamedQuery(name = "RiskMetrix.findAll", query = "SELECT r FROM RiskMetrix r")
    , @NamedQuery(name = "RiskMetrix.findByRiskMetrixId", query = "SELECT r FROM RiskMetrix r WHERE r.riskMetrixId = :riskMetrixId")
    , @NamedQuery(name = "RiskMetrix.findByCondition", query = "SELECT r FROM RiskMetrix r WHERE r.condition = :condition")
    , @NamedQuery(name = "RiskMetrix.findByFlag", query = "SELECT r FROM RiskMetrix r WHERE r.flag = :flag")})
public class RiskMetrix implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "risk_metrix_id")
    private Integer riskMetrixId;
    @Basic(optional = false)
    @Column(name = "condition")
    private String condition;
    @Basic(optional = false)
    @Column(name = "flag")
    private String flag;
    public RiskMetrix() {
    }
    public RiskMetrix(Integer riskMetrixId) {
        this.riskMetrixId = riskMetrixId;
    }
    public RiskMetrix(Integer riskMetrixId, String condition, String flag) {
        this.riskMetrixId = riskMetrixId;
        this.condition = condition;
        this.flag = flag;
    }
    public Integer getRiskMetrixId() {
        return riskMetrixId;
    }
    public void setRiskMetrixId(Integer riskMetrixId) {
        this.riskMetrixId = riskMetrixId;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (riskMetrixId != null ? riskMetrixId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RiskMetrix)) {
            return false;
        }
        RiskMetrix other = (RiskMetrix) object;
        if ((this.riskMetrixId == null && other.riskMetrixId != null) || (this.riskMetrixId != null && !this.riskMetrixId.equals(other.riskMetrixId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.RiskMetrix[ riskMetrixId=" + riskMetrixId + " ]";
    }
}

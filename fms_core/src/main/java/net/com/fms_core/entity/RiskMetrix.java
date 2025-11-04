/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.entity; 
 import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "risk_metrix")
@NamedQueries({
    @NamedQuery(name = "RiskMetrix.findAll", query = "SELECT r FROM RiskMetrix r")
    , @NamedQuery(name = "RiskMetrix.findByRiskMetrixId", query = "SELECT r FROM RiskMetrix r WHERE r.riskMetrixId = :riskMetrixId")
    , @NamedQuery(name = "RiskMetrix.findByFlag", query = "SELECT r FROM RiskMetrix r WHERE r.flag = :flag")})
public class RiskMetrix implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "risk_metrix_id")
    private Integer riskMetrixId;
    @Basic(optional = false)
    @Column(name = "flag")
    private String flag;
    @Column(name = "min_value")
    private int minValue;
    @Column(name = "max_value")
    private int maxValue;
    public RiskMetrix() {
    }
    public RiskMetrix(Integer riskMetrixId) {
        this.riskMetrixId = riskMetrixId;
    }
    public RiskMetrix(Integer riskMetrixId, String flag) {
        this.riskMetrixId = riskMetrixId;
        this.flag = flag;
    }
    public int getMinValue() {
        return minValue;
    }
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }
    public int getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
    public Integer getRiskMetrixId() {
        return riskMetrixId;
    }
    public void setRiskMetrixId(Integer riskMetrixId) {
        this.riskMetrixId = riskMetrixId;
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

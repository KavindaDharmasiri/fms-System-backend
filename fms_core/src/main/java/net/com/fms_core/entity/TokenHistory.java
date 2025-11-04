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
@Table(name = "token_history")
@NamedQueries({
    @NamedQuery(name = "TokenHistory.findAll", query = "SELECT t FROM TokenHistory t")
    , @NamedQuery(name = "TokenHistory.findByTokenId", query = "SELECT t FROM TokenHistory t WHERE t.tokenId = :tokenId")
    , @NamedQuery(name = "TokenHistory.findByDeviceType", query = "SELECT t FROM TokenHistory t WHERE t.deviceType = :deviceType")
    , @NamedQuery(name = "TokenHistory.findByExpireStatus", query = "SELECT t FROM TokenHistory t WHERE t.expireStatus = :expireStatus")
    , @NamedQuery(name = "TokenHistory.findByExpiredAt", query = "SELECT t FROM TokenHistory t WHERE t.expiredAt = :expiredAt")
    , @NamedQuery(name = "TokenHistory.findByIssuedAt", query = "SELECT t FROM TokenHistory t WHERE t.issuedAt = :issuedAt")
    , @NamedQuery(name = "TokenHistory.findByRevokeStatus", query = "SELECT t FROM TokenHistory t WHERE t.revokeStatus = :revokeStatus")
    , @NamedQuery(name = "TokenHistory.findByStatus", query = "SELECT t FROM TokenHistory t WHERE t.status = :status")
    , @NamedQuery(name = "TokenHistory.findByTokenUuid", query = "SELECT t FROM TokenHistory t WHERE t.tokenUuid = :tokenUuid")
    , @NamedQuery(name = "TokenHistory.findByType", query = "SELECT t FROM TokenHistory t WHERE t.type = :type")
    , @NamedQuery(name = "TokenHistory.findByUpdatedAt", query = "SELECT t FROM TokenHistory t WHERE t.updatedAt = :updatedAt")})
public class TokenHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "token_id")
    private Integer tokenId;
    @Basic(optional = false)
    @Column(name = "device_type")
    private String deviceType;
    @Basic(optional = false)
    @Column(name = "expire_status")
    private boolean expireStatus;
    @Basic(optional = false)
    @Column(name = "expired_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredAt;
    @Basic(optional = false)
    @Column(name = "issued_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issuedAt;
    @Basic(optional = false)
    @Column(name = "revoke_status")
    private boolean revokeStatus;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @Basic(optional = false)
    @Lob
    @Column(name = "token")
    private String token;
    @Basic(optional = false)
    @Column(name = "token_uuid")
    private String tokenUuid;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User userId;
    public TokenHistory() {
    }
    public TokenHistory(Integer tokenId) {
        this.tokenId = tokenId;
    }
    public TokenHistory(Integer tokenId, String deviceType, boolean expireStatus, Date expiredAt, Date issuedAt, boolean revokeStatus, boolean status, String token, String tokenUuid, String type, Date updatedAt) {
        this.tokenId = tokenId;
        this.deviceType = deviceType;
        this.expireStatus = expireStatus;
        this.expiredAt = expiredAt;
        this.issuedAt = issuedAt;
        this.revokeStatus = revokeStatus;
        this.status = status;
        this.token = token;
        this.tokenUuid = tokenUuid;
        this.type = type;
        this.updatedAt = updatedAt;
    }
    public Integer getTokenId() {
        return tokenId;
    }
    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public boolean getExpireStatus() {
        return expireStatus;
    }
    public void setExpireStatus(boolean expireStatus) {
        this.expireStatus = expireStatus;
    }
    public Date getExpiredAt() {
        return expiredAt;
    }
    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }
    public Date getIssuedAt() {
        return issuedAt;
    }
    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }
    public boolean getRevokeStatus() {
        return revokeStatus;
    }
    public void setRevokeStatus(boolean revokeStatus) {
        this.revokeStatus = revokeStatus;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getTokenUuid() {
        return tokenUuid;
    }
    public void setTokenUuid(String tokenUuid) {
        this.tokenUuid = tokenUuid;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    public User getUserId() {
        return userId;
    }
    public void setUserId(User userId) {
        this.userId = userId;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tokenId != null ? tokenId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TokenHistory)) {
            return false;
        }
        TokenHistory other = (TokenHistory) object;
        if ((this.tokenId == null && other.tokenId != null) || (this.tokenId != null && !this.tokenId.equals(other.tokenId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.TokenHistory[ tokenId=" + tokenId + " ]";
    }
}

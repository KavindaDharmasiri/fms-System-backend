/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.entity; 
 import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId")
    , @NamedQuery(name = "User.findByUserUuid", query = "SELECT u FROM User u WHERE u.userUuid = :userUuid")
    , @NamedQuery(name = "User.findByUserName", query = "SELECT u FROM User u WHERE u.userName = :userName")
    , @NamedQuery(name = "User.findByNic", query = "SELECT u FROM User u WHERE u.nic = :nic")
    , @NamedQuery(name = "User.findByUserCategory", query = "SELECT u FROM User u WHERE u.userCategory = :userCategory")
    , @NamedQuery(name = "User.findByEmpId", query = "SELECT u FROM User u WHERE u.empId = :empId")
    , @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
    , @NamedQuery(name = "User.findByContactNo", query = "SELECT u FROM User u WHERE u.contactNo = :contactNo")
    , @NamedQuery(name = "User.findByStatus", query = "SELECT u FROM User u WHERE u.status = :status")
    , @NamedQuery(name = "User.findByCreatedAt", query = "SELECT u FROM User u WHERE u.createdAt = :createdAt")
    , @NamedQuery(name = "User.findByUpdatedAt", query = "SELECT u FROM User u WHERE u.updatedAt = :updatedAt")
    , @NamedQuery(name = "User.findByCreatedBy", query = "SELECT u FROM User u WHERE u.createdBy = :createdBy")
    , @NamedQuery(name = "User.findByUpdatedBy", query = "SELECT u FROM User u WHERE u.updatedBy = :updatedBy")
    , @NamedQuery(name = "User.findByLoginAttempts", query = "SELECT u FROM User u WHERE u.loginAttempts = :loginAttempts")
    , @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")
    , @NamedQuery(name = "User.findByAddress", query = "SELECT u FROM User u WHERE u.address = :address")
    , @NamedQuery(name = "User.findByCity", query = "SELECT u FROM User u WHERE u.city = :city")
    , @NamedQuery(name = "User.findByContact", query = "SELECT u FROM User u WHERE u.contact = :contact")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @Column(name = "user_uuid")
    private String userUuid;
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName;
    @Lob
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "nic")
    private String nic;
    @Column(name = "user_category")
    private String userCategory;
    @Column(name = "emp_id")
    private String empId;
    @Column(name = "email")
    private String email;
    @Column(name = "contact_no")
    private String contactNo;
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
    @Column(name = "login_attempts")
    private Integer loginAttempts;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "contact")
    private String contact;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<UserRole> userRoleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<TokenHistory> tokenHistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Otp> otpCollection;
    public User() {
    }
    public User(Integer userId) {
        this.userId = userId;
    }
    public User(Integer userId, String userUuid, String userName, String status, Date createdAt, Date updatedAt, String password) {
        this.userId = userId;
        this.userUuid = userUuid;
        this.userName = userName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.password = password;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserUuid() {
        return userUuid;
    }
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getNic() {
        return nic;
    }
    public void setNic(String nic) {
        this.nic = nic;
    }
    public String getUserCategory() {
        return userCategory;
    }
    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }
    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getContactNo() {
        return contactNo;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
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
    public Integer getLoginAttempts() {
        return loginAttempts;
    }
    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public Collection<UserRole> getUserRoleCollection() {
        return userRoleCollection;
    }
    public void setUserRoleCollection(Collection<UserRole> userRoleCollection) {
        this.userRoleCollection = userRoleCollection;
    }
    public Collection<TokenHistory> getTokenHistoryCollection() {
        return tokenHistoryCollection;
    }
    public void setTokenHistoryCollection(Collection<TokenHistory> tokenHistoryCollection) {
        this.tokenHistoryCollection = tokenHistoryCollection;
    }
    public Collection<Otp> getOtpCollection() {
        return otpCollection;
    }
    public void setOtpCollection(Collection<Otp> otpCollection) {
        this.otpCollection = otpCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "hibernateexample.User[ userId=" + userId + " ]";
    }
}

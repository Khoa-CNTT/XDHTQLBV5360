package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int id;

    @Column(name="fullname",length = 30)
    private String fullName;

    @Column(name="password",length = 255)
    private String passWord;

    @Column(name="phonenumber",length = 30)
    private String phoneNumber;

    @Column(name="email",length = 30)
    private String email;

    @Column(name="is_enable")
    private boolean isEnable;

    @Column(name="activation_code",length = 10)
    private String activationCode;

    @Column(name="otp_generated_time")
    private LocalDateTime otpGeneratedTime;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Rating> ratings;

    public User() {
    }

    public User(int id, String fullName, String passWord, String phoneNumber, String email, boolean isEnable, String activationCode, Collection<Role> roles) {
        this.id = id;
        this.fullName = fullName;
        this.passWord = passWord;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isEnable = isEnable;
        this.activationCode = activationCode;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        this.passWord = bCrypt.encode(passWord);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getOtpGeneratedTime() {
        return otpGeneratedTime;
    }

    public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
        this.otpGeneratedTime = otpGeneratedTime;
    }
}

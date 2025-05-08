package vn.nhom24.bus_ticket_reservation_system.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import vn.nhom24.bus_ticket_reservation_system.DTO.RegisterUser;
import vn.nhom24.bus_ticket_reservation_system.entity.User;

public interface UserSevice extends UserDetailsService {
    public User findByFullName(String fullName);
    public User findByPhoneNumber(String phoneNumber);

    public User findByEmail(String email);

    // đăng ký tài khoản
    public boolean save(RegisterUser registerUser);

    public void save(User user);

    // xác thực tài khoản
    public boolean verifyAccount(String phoneNumber, String otp);


}

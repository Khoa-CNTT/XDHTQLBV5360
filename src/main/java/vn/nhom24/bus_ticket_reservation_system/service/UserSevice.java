package vn.nhom24.bus_ticket_reservation_system.service;


import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import vn.nhom24.bus_ticket_reservation_system.DTO.AdminRegisterUser;
import vn.nhom24.bus_ticket_reservation_system.DTO.AdminUpdateUserDto;
import vn.nhom24.bus_ticket_reservation_system.DTO.RegisterUser;
import vn.nhom24.bus_ticket_reservation_system.entity.User;

public interface UserSevice extends UserDetailsService {
    public User findByFullName(String fullName);
    public User findByPhoneNumber(String phoneNumber);

    public User findByEmail(String email);

    boolean save(AdminRegisterUser registerUser);

    // đăng ký tài khoản
    public boolean save(RegisterUser registerUser);

    public void save(User user);

    public Page<User> getAll(int pageNo);

    // xác thực tài khoản
    public boolean verifyAccount(String phoneNumber, String otp);


    User findById(int id);

    void updateFromDto(AdminUpdateUserDto adminUpdateUserDto);
    void updateUserActiveStatus(int id,boolean active);
}

package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.AdminRegisterUser;
import vn.nhom24.bus_ticket_reservation_system.DTO.AdminUpdateUserDto;
import vn.nhom24.bus_ticket_reservation_system.DTO.RegisterUser;
import vn.nhom24.bus_ticket_reservation_system.entity.Role;
import vn.nhom24.bus_ticket_reservation_system.entity.User;
import vn.nhom24.bus_ticket_reservation_system.enums.EmailType;
import vn.nhom24.bus_ticket_reservation_system.exception.AccountNotVerifiedException;
import vn.nhom24.bus_ticket_reservation_system.repository.RoleRepository;
import vn.nhom24.bus_ticket_reservation_system.repository.UserRepository;
import vn.nhom24.bus_ticket_reservation_system.service.UserSevice;
import vn.nhom24.bus_ticket_reservation_system.util.EmailUtils;
import vn.nhom24.bus_ticket_reservation_system.util.RandomCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserSeviceIpml implements UserSevice {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmailSeviceIpml emailSeviceIpml;

    @Autowired
    public UserSeviceIpml(UserRepository userRepository, RoleRepository roleRepository,EmailSeviceIpml emailSeviceIpml) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailSeviceIpml = emailSeviceIpml;
    }

    public UserSeviceIpml() {
    }

    // cài đặt securyti theo số điẹn thoại password
    @Override
    public User findByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, AccountNotVerifiedException {
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new UsernameNotFoundException("invalid Username or password");
        }
        if(!user.isEnable()){
            throw new AccountNotVerifiedException("account is not verified");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassWord(), rolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
    // ----------------------END

    @Override
    public boolean save(AdminRegisterUser registerUser) {
        User user = new User();
        user .setFullName(registerUser.getFullName());
        user.setEmail(registerUser.getEmail());
        user.setPassWord(registerUser.getPassWord());
        user.setPhoneNumber(registerUser.getPhoneNumber());
        user.setEnable(registerUser.isActive());

        Role role = roleRepository.findByName(registerUser.getRole());
        user.setRoles(List.of(role) );
        if(userRepository.save(user) != null){
            return true;
        }

        return false;

    }

    @Override
    public boolean save(RegisterUser registerUser) {
        User user = new User();
        user .setFullName(registerUser.getFullName());
        user.setEmail(registerUser.getEmail());
        user.setPassWord(registerUser.getPassWord());
        user.setPhoneNumber(registerUser.getPhoneNumber());
        user.setOtpGeneratedTime(LocalDateTime.now());
        user.setActivationCode(RandomCode.getSoNgauNhien());


        // xét dèault role USER cho ngưiof dùng
        Role role = roleRepository.findByName("ROLE_USER");
        Collection<Role> roles = new ArrayList<Role>();
        roles.add(role);
        user.setRoles(roles);

        // luư thanh công thì gủi email xác nhận tài khoản
        if(userRepository.save(user) != null){
            Map<String, Object> emailData = new HashMap<>();
            emailData.put("name",user.getFullName());
            emailData.put("url", EmailUtils.getVerificationUrl(user.getEmail(),user.getActivationCode()));
            emailSeviceIpml.sendHtmlEmail(user.getEmail(), EmailType.ACCOUNT_REGISTRATION_CONFIRMATION,emailData);
            return true;
        }

        return false;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }


    @Override
    public boolean verifyAccount(String email, String token) {
        User user = userRepository.findByEmail(email);
        //kiểm tra tài khoản đã tônf tại hay chưa
        if(user != null){

            log.info("kiểm tra tài khoản đã tônf tại hay chưa");
            // kiểm tra otp khớp hay không . và xem hiệu lục token đã hết hạn hay chưa
            if (user.getActivationCode().equals(token)
                    && Duration.between(user.getOtpGeneratedTime(),
                    LocalDateTime.now()).getSeconds() < (24 * 60 * 60)
            ) {
                log.info("kiểm tra otp khớp hay không . và xem hiệu lục token đã hết hạn hay chưa");
                user.setEnable(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;

    }

    @Override
    public User findById(int id) {
       User user = userRepository.findById(id).orElse(null);
        if(user == null){
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }

    @Override
    public void updateFromDto(AdminUpdateUserDto registerUser) {
        User user = userRepository.findById(registerUser.getId()).orElse(null);
        if(user == null){
            throw new UsernameNotFoundException("user not found");
        }
        user.setFullName(registerUser.getFullName());
        user.setEmail(registerUser.getEmail());
        user.setPhoneNumber(registerUser.getPhoneNumber());
        log.info("ROle :"+registerUser.getRole());
        Role role = roleRepository.findByName(registerUser.getRole());

        user.setRoles(new ArrayList<>(List.of(role)));
        userRepository.save(user);
    }

    public void regenerateOtp(String email) {
        User user = userRepository.findByEmail(email);

        user.setActivationCode(RandomCode.getSoNgauNhien());
        user.setOtpGeneratedTime(LocalDateTime.now());

        Map<String, Object> emailData = new HashMap<>();
        emailData.put("name",user.getFullName());
        emailData.put("url", EmailUtils.getVerificationUrl(user.getEmail(),user.getActivationCode()));
        emailSeviceIpml.sendHtmlEmail(user.getEmail(), EmailType.ACCOUNT_REGISTRATION_CONFIRMATION,emailData);

        userRepository.save(user);
    }



    @Override
    public Page<User> getAll(int pageNo) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);

        List<User> list = userRepository.findAll();

        int total = list.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);

        List<User> subList = list.subList(start, end);
        return new PageImpl<>(subList, pageable, total);
    }
    public void updateUserActiveStatus(int userId,boolean active){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng với ID: " + userId));

        // Cập nhật trạng thái isActive
        user.setEnable(active);

        // Lưu thay đổi vào cơ sở dữ liệu
        userRepository.save(user);
    }
}

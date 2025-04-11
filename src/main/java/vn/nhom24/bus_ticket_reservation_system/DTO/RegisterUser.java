package vn.nhom24.bus_ticket_reservation_system.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterUser {
    @NotBlank(message = "thông tin bắt buộc")
    @Size(min=1,message = " độ dài tối thiểu là 1")
    private String fullName;

    @NotBlank(message = "thông tin bắt buộc")
    @Pattern(regexp = "^(?=.*[!@#$%^&*()-_+=])(?=.*[0-9])[A-Za-z0-9!@#$%^&*()-_+=]+$",message = "Mật khẩu có chứa ít nhất 1 ký tự đặc biệt và một số")
    @Size(min=8,message = "Mật khẩu chứa ít nhất 8 ký tự")
    private String passWord;

    @NotBlank(message = "thông tin bắt buộc")
    @Pattern(regexp = "^0\\d{9}$", message = "số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotBlank(message = "thông tin bắt buộc")
    @Email(message = "Email không hợp lệ")
    private String email;


    public RegisterUser() {
    }

    public RegisterUser(String fullName, String passWord, String phoneNumber, String email) {
        this.fullName = fullName;
        this.passWord = passWord;
        this.phoneNumber = phoneNumber;
        this.email = email;
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
        this.passWord = passWord;
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
}

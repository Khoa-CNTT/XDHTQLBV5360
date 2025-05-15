package vn.nhom24.bus_ticket_reservation_system.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

}

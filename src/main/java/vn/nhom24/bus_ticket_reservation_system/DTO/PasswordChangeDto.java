package vn.nhom24.bus_ticket_reservation_system.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordChangeDto {
    @NotBlank(message = "Mật khẩu cũ không được để trống")
     String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 8, message = "Mật khẩu mới phải có ít nhất 8 ký tự")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
            message = "Mật khẩu mới phải chứa ít nhất một số và một ký tự đặc biệt")
     String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
     String confirmPassword;
}

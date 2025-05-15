package vn.nhom24.bus_ticket_reservation_system.DTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserDto { // Tên mới
    @NotNull(message = "ID người dùng không được trống") // Cần ID để biết user nào cần update
    private Integer id;

    @NotBlank(message = "Tên đầy đủ không được trống")
    @Size(min=1, message = "Độ dài tên tối thiểu là 1")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotBlank(message = "Email không được trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Vai trò không được trống")
    private String role;

    private boolean isActive; // Nếu bạn muốn cho phép update trạng thái active
}

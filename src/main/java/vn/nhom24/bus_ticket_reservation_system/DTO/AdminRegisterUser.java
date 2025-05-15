package vn.nhom24.bus_ticket_reservation_system.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterUser extends RegisterUser{
    /**
     * @param fullName
     * @param passWord
     * @param phoneNumber
     * @param email
     * @param role
     */
    private boolean isActive = true;
    @NotBlank(message = "thông tin bắt buộc")
    private String role;
    private Integer id;
}

package vn.nhom24.bus_ticket_reservation_system.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueRequest {
      @NotBlank(message = "Loại khoảng thời gian không được để trống")
      String periodType = "YEAR";

      @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Định dạng ngày phải là dd-MM-yyyy")
      String customStartDate;

      @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Định dạng ngày phải là dd-MM-yyyy")
      String customEndDate;
      String selectedRoute;
      Integer specificMonth;
      Integer specificYear;
      Integer specificQuarter;
}

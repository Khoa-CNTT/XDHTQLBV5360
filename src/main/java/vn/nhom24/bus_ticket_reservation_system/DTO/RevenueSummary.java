package vn.nhom24.bus_ticket_reservation_system.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueSummary {
      Long totalTicket;       // Đếm số vé -> Long
      BigDecimal totalRevenue; // Tiền tệ -> BigDecimal
      Long totalTrips;        // Đếm số chuyến -> Long
      Long totalCar;
}

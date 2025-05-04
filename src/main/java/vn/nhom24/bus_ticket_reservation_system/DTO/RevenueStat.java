package vn.nhom24.bus_ticket_reservation_system.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueStat {
     String route;
     Long totalTicket;
     BigDecimal totalRevenue;
     BigDecimal averageRating;
     Long totalTrips;
}

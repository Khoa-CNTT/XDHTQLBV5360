package vn.nhom24.bus_ticket_reservation_system.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewDashBoardResponse {
    int totalTrip;
    double monthlyRevenue;
    int totalNewUser;
    double totalBooking;
}

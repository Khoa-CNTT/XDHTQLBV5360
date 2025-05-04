package vn.nhom24.bus_ticket_reservation_system.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String fullName;
    String phoneNumber;
    String seatNumber;
    String totalPrice;
    String tiketStatus;
    String paymentStatus;
    String paymentMethod;
    String bookingId;
}

package vn.nhom24.bus_ticket_reservation_system.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class TripAdminDTO {
    private int tripId;
    private String tripNamel; // tên chuyến đi
    private String routeNamel; // tên tuyến đi
    private String departureLocation ; // Điểm xuất phát
    private String arrivalLocation ; // Điểm đến
    private LocalDateTime departureTime;  // Thời gian xuất phát
    private LocalDateTime arrivalTime;  // Thời gian đến nơi
    private int emptySeats; // sô ghế còn trống
    private int reservedSeats;// sô ghế đã đặt
    private int purchasedSeats;// sô ghế đã mua
    private double price; // giá vé
    private String vehicle; // bien so xe
}

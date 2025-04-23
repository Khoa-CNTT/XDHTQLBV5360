package vn.nhom24.bus_ticket_reservation_system.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class BookingDTO {
    private int bookingId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String tripName;
    private LocalTime starTime;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private String departureLocation;
    private String arrivalLocation;
    private String listSeat;
    private String bookingCode;

}

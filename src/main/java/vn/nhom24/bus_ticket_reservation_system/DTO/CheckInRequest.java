package vn.nhom24.bus_ticket_reservation_system.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInRequest {
    private String idBooking; // Hoặc ticketId, ticketCode
}
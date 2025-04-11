package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.Ticket;
import vn.nhom24.bus_ticket_reservation_system.enums.SeatStatus;

import java.util.List;

@Service
public interface TicketSevice {
    List<Ticket> getSeatsByRow(String row , int tripId);

    Ticket updateStatusById(int ticketId, SeatStatus status);
}

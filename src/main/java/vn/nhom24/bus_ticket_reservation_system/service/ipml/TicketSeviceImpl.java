package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.Ticket;
import vn.nhom24.bus_ticket_reservation_system.enums.SeatStatus;
import vn.nhom24.bus_ticket_reservation_system.repository.TicketRepository;
import vn.nhom24.bus_ticket_reservation_system.service.TicketSevice;

import java.util.List;
import java.util.Optional;

@Service
public class TicketSeviceImpl implements TicketSevice {

    @Autowired
    TicketRepository ticketRepository;

    @Override
    public List<Ticket> getSeatsByRow(String row, int  tripId) {
        List<Ticket> tickets = ticketRepository.findByRowAndCarId(row,tripId);
        return tickets ;
    }

    @Override
    public Ticket updateStatusById(int ticketId, SeatStatus status) {
        Optional<Ticket> t = ticketRepository.findById(ticketId);
        Ticket ticket = t.get();
        ticket.setSeatStatus(status);
        Ticket ticketResult =  ticketRepository.save(ticket);
        return ticketResult;
    }

}

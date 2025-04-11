package vn.nhom24.bus_ticket_reservation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.nhom24.bus_ticket_reservation_system.entity.Ticket;
import vn.nhom24.bus_ticket_reservation_system.enums.SeatStatus;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Integer> {
    @Query(value = "SELECT COUNT(t.ticket_id) " +
            "FROM ticket t " +
            "WHERE t.trip_id = ?1 AND t.ticket_status = ?2 ;" , nativeQuery = true)
    int countSeatEmptyByTripAndStatus(int tripId, String status);


    @Query("SELECT t FROM Ticket t WHERE t.trip.id = ?2 AND t.seatName LIKE ?1%")
    List<Ticket> findByRowAndCarId(String row, int tripId);

    @Query("UPDATE Ticket t SET t.seatStatus = :status WHERE t.id = :ticketId")
    int updateStatusById(int ticketId, SeatStatus status);

}

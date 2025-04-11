package vn.nhom24.bus_ticket_reservation_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.nhom24.bus_ticket_reservation_system.entity.Booking;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;

import java.util.List;

@ResponseBody
public interface BookingReposity extends JpaRepository<vn.nhom24.bus_ticket_reservation_system.entity.Booking,Integer> {
    List<Booking> findAllByOrderByDateCreatedDesc();

    Page<Booking> findAllByOrderByDateCreatedDesc(Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.email = :email AND b.status IN (:statuses)")
    List<Booking> findByEmailAndStatusIn(@Param("email") String email, @Param("statuses") List<BookingStatus> statuses);

    List<Booking> findByPhoneNumber(String phoneNumber);

    List<Booking> findByTripIdIn(List<Integer> tripIds);

    @Query("SELECT b FROM Booking b WHERE " +
            "LOWER(b.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Booking> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT b FROM Booking b WHERE " +
            "LOWER(b.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Booking> searchByKeyword(@Param("keyword") String keyword,Pageable pageable);

    @Query(value = "CALL GetTripsStartingInNext24Hours()", nativeQuery = true)
    List<Integer> getBookingsForTripsStartingInNext24Hours();

    Booking findByBookingCode(String bookingCode);
}

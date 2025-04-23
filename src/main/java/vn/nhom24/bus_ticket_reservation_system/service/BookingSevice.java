package vn.nhom24.bus_ticket_reservation_system.service;

import com.google.zxing.WriterException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.BookingDTO;
import vn.nhom24.bus_ticket_reservation_system.entity.Booking;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;

import java.io.IOException;
import java.util.List;

@Service
public interface BookingSevice {
    Page<Booking> findAllByOrderByDateCreatedDesc(int pageNo);
    Page<Booking> searchBookingsByKeyword(String keyword,int pageNo);
    List<Booking> searchBookingsByKeyword(String keyword);
    public List<Booking> getAll();

    List<Booking> findPaidOrBookedByEmail(String phoneNumber);
    public Booking getBookingById(int bookingId);

    public List<Booking> findByPhoneNumber(String phone);
    public void updateStatus(Booking booking, BookingStatus bookingStatus);

    public Booking saveBooking(int tripId, String phoneNumber, String fullName, String email, String[] listTicket,int departureId,int arrivalId) throws IOException, WriterException;

    BookingDTO findBookingById(int id) throws IOException, WriterException;

    BookingDTO checkInTicket(String idBooking, String currentTripCode) throws IOException, WriterException;
}

package vn.nhom24.bus_ticket_reservation_system.util;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.nhom24.bus_ticket_reservation_system.DTO.BookingDTO;
import vn.nhom24.bus_ticket_reservation_system.enums.EmailType;
import vn.nhom24.bus_ticket_reservation_system.repository.BookingReposity;
import vn.nhom24.bus_ticket_reservation_system.service.BookingSevice;
import vn.nhom24.bus_ticket_reservation_system.service.EmailSevice;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TripReminderScheduler {
    @Autowired
    private BookingReposity bookingReposity;

    @Autowired
    private BookingSevice bookingSevice;

    @Autowired
    private EmailSevice emailService;

    @Scheduled(cron = "0 50 21 * * ?")
    @Transactional
    public void sendTripReminders() throws IOException, WriterException {
        // Lấy danh sách booking các chuyến xe sẽ xuất phát trong 24 giờ tới
        List<Integer> bookingIds  = bookingReposity.getBookingsForTripsStartingInNext24Hours();

        for (Integer i : bookingIds ) {

            BookingDTO bookingDTO =  bookingSevice.findBookingById(i);
            Map<String,Object> inforBooking = new HashMap<>();

            inforBooking.put("bookingId",bookingDTO.getBookingId());
            inforBooking.put("customerName",bookingDTO.getCustomerName());
            inforBooking.put("customerPhone",bookingDTO.getCustomerPhone());
            inforBooking.put("listSeat",bookingDTO.getListSeat());
            inforBooking.put("tripName",bookingDTO.getTripName());
            inforBooking.put("starTime",bookingDTO.getStarTime());
            inforBooking.put("departureDate",bookingDTO.getDepartureDate());
            inforBooking.put("departureTime",bookingDTO.getDepartureTime());
            inforBooking.put("departureLocation",bookingDTO.getDepartureLocation());
            inforBooking.put("arrivalLocation",bookingDTO.getArrivalLocation());

            emailService.sendHtmlEmail(bookingDTO.getCustomerEmail(), EmailType.REMINDER_TRIP ,inforBooking);
        }
    }
}

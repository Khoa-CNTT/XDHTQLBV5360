package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.Booking;
import vn.nhom24.bus_ticket_reservation_system.entity.Rating;
import vn.nhom24.bus_ticket_reservation_system.entity.User;
import vn.nhom24.bus_ticket_reservation_system.repository.BookingReposity;
import vn.nhom24.bus_ticket_reservation_system.repository.RatingRepository;
import vn.nhom24.bus_ticket_reservation_system.service.BookingSevice;
import vn.nhom24.bus_ticket_reservation_system.service.RatingSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;
import vn.nhom24.bus_ticket_reservation_system.service.UserSevice;

@Service
public class RatingSeviceIpml implements RatingSevice {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BookingReposity bookingReposity;


    @Autowired
    UserSevice userSevice;
    @Autowired
    TripSevice tripSevice;
    @Autowired
    BookingSevice bookingSevice;

    @Transactional
    @Override
    public void saveRating(User user, int bookingId, byte point, String describe) {

        Booking booking = bookingSevice.getBookingById(bookingId);
        booking.setRated(true);
        Rating rating = new Rating();
        rating.setRatingNumber(point);
        rating.setBooking(booking);
        rating.setUser(user);
        rating.setDescribe(describe);

        bookingReposity.save(booking);
        ratingRepository.save(rating);
    }

    @Override
    public boolean checkRating(int bookingId) {
        Booking booking = bookingSevice.getBookingById(bookingId);
        return booking.isRated();
    }
}

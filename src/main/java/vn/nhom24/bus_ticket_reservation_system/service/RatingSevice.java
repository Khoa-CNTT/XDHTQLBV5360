package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.stereotype.Service;

@Service
public interface RatingSevice {
    void saveRating(String phoneNumber, int booking, byte point, String describe);
    boolean checkRating(int bookingId);
}

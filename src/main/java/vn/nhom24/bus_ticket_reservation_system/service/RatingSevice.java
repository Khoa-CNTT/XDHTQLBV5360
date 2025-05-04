package vn.nhom24.bus_ticket_reservation_system.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.User;

@Service
public interface RatingSevice {
    void saveRating(User user, int bookingId, byte point, String describe);

    boolean checkRating(int bookingId);
}

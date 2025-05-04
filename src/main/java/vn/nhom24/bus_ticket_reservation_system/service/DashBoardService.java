package vn.nhom24.bus_ticket_reservation_system.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.ViewDashBoardResponse;
import vn.nhom24.bus_ticket_reservation_system.entity.Payment;
import vn.nhom24.bus_ticket_reservation_system.repository.BookingReposity;
import vn.nhom24.bus_ticket_reservation_system.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DashBoardService {
    PaymentRepository paymentRepository;
    BookingReposity bookingReposity;

    public ViewDashBoardResponse getDashBoardData() {
        Double total = paymentRepository.getTotalAmountThisMonth();



        return ViewDashBoardResponse.builder()
                .monthlyRevenue(total != null ? total : 0.0)
                .build();
    }
}

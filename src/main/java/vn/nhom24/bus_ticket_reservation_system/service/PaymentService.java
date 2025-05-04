package vn.nhom24.bus_ticket_reservation_system.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.PaymentResponse;
import vn.nhom24.bus_ticket_reservation_system.entity.Booking;
import vn.nhom24.bus_ticket_reservation_system.entity.Payment;
import vn.nhom24.bus_ticket_reservation_system.entity.Trip;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;
import vn.nhom24.bus_ticket_reservation_system.enums.PaymentStatus;
import vn.nhom24.bus_ticket_reservation_system.repository.BookingReposity;
import vn.nhom24.bus_ticket_reservation_system.repository.PaymentRepository;
import vn.nhom24.bus_ticket_reservation_system.repository.TripRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {
    PaymentRepository paymentRepository;
    BookingReposity bookingRepository;
    TripRepository tripRepository;

    public void createPayment(Booking booking) {
        double totalAmount = booking.getTickets().stream()
                .mapToDouble(ticket -> ticket.getTrip().getPriceList().getPrice())
                .sum();

        Payment payment = Payment.builder()
                .paymentMethod("ON_BUS")
                .status(PaymentStatus.PENDING)
                .amount(totalAmount)
                .booking(booking)
                .build();

        booking.setPayment(payment);
        bookingRepository.save(booking);
    }

    public List<PaymentResponse> getAllPayments(String tripId) {
        Trip trip = tripRepository.findById(Integer.valueOf(tripId))
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        List<PaymentResponse> payments = trip.getBookings().stream()
                .filter(booking -> booking.getStatus() == BookingStatus.PAID
                        || booking.getStatus() == BookingStatus.BOOKED
                        || booking.getStatus() == BookingStatus.CHECKED_IN)
                .map(booking -> {
                    Payment payment = booking.getPayment();
                    return PaymentResponse.builder()
                            .bookingId(String.valueOf(booking.getId()))
                            .totalPrice(String.valueOf(booking.getTickets().stream()
                                    .mapToDouble(ticket -> ticket.getTrip().getPriceList().getPrice())
                                    .sum())
                            )
                            .tiketStatus(booking.getStatus().getDisplayName())
                            .fullName(booking.getFullName())
                            .paymentStatus(payment.getStatus().getDisplayName())
                            .phoneNumber(booking.getPhoneNumber())
                            .paymentMethod(payment.getPaymentMethod())
                            .seatNumber(booking.getTickets().stream()
                                    .map(ticket -> ticket.getSeatName())
                                    .reduce((first, second) -> first + ", " + second)
                                    .orElse(""))
                            .build();
                }).toList();
        return payments;
    }

    public Payment getPaymentById(int paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return payment;
    }

    public void updateStatus(Payment payment, PaymentStatus status,String note) {
        Payment pay = this.getPaymentById(payment.getId());
        pay.setStatus(status);
        pay.setNote(note);
        paymentRepository.save(pay);
    }
}

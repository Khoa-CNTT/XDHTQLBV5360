package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.nhom24.bus_ticket_reservation_system.enums.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int id;

    @Column(name = "amount")
    private double amount;

    @Column(name = "payment_method", length = 255)
    private String paymentMethod;

    @Column(name = "paydate")
    private LocalDateTime payDate;

    @OneToOne
    @JoinColumn(name = "booking_id",unique = true)
    private Booking booking;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PAID, UNPAID, PENDING

    @Column(name = "confirmed_by", nullable = true) // xác nhận bởi nhân viên vận hành (nếu có)
    private String confirmedBy;

    @Column(name = "confirm_date")
    private LocalDateTime confirmDate;

    @Column(name = "note", nullable = true)
    private String note;


}

package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bookings")
public class Booking{
    @Id
    @Column(name="booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="full_name")
    private String fullName;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="email")
    private String email;


    @Column(name="status", length = 20)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name="date_created")
    private LocalDateTime dateCreated;

    @Column(name="booking_code")
    private String bookingCode;

    @Column(name="rated ")
    private boolean rated ;

    @OneToMany(mappedBy = "booking",fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Rating> ratings;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_ticket",
            joinColumns = @JoinColumn(name="booking_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private List<Ticket> tickets;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "departure_id")
    private Stop departure;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrival_id")
    private Stop arrival;


    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    public void addPayment(Payment payment) {
        this.payment = payment;
        payment.setBooking(this);
    }

}

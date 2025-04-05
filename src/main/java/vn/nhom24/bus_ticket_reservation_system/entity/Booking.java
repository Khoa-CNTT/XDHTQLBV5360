package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

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



    public Booking() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Stop getDeparture() {
        return departure;
    }

    public void setDeparture(Stop departure) {
        this.departure = departure;
    }

    public Stop getArrival() {
        return arrival;
    }

    public void setArrival(Stop arrival) {
        this.arrival = arrival;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }
}

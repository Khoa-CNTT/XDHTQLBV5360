package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;
import vn.nhom24.bus_ticket_reservation_system.enums.TripStatus;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private int id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "trip_name" ,length = 50)
    private String name;

    @Column(name = "status" ,length = 30)
    @Enumerated(EnumType.STRING)
    private TripStatus tripStatus;

    @Column(name = "note" ,length = 255)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id")
    private PriceList priceList;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Booking> bookings;


    @OneToMany(mappedBy = "trip",fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Ticket> tickets;

    public Trip(int id, LocalDate startDate, LocalDate endDate, String name, TripStatus tripStatus, String note, Schedule schedule, Car car, PriceList priceList, List<Booking> bookings) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.tripStatus = tripStatus;
        this.note = note;
        this.schedule = schedule;
        this.car = car;
        this.priceList = priceList;
        this.bookings = bookings;
    }

    public Trip() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public TripStatus getStatus() {
        return tripStatus;
    }

    public void setStatus(TripStatus status) {
        this.tripStatus = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public PriceList getPriceList() {
        return priceList;
    }

    public void setPriceList(PriceList priceList) {
        this.priceList = priceList;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }


    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }
}

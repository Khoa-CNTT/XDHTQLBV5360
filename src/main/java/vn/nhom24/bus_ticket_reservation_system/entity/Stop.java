package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "stops")
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stop_id")
    private int id;

    @Column(name = "stop_name" ,length = 30)
    private String stopName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tinhtp_id")
    private Tinhtp tinhtp;

    @OneToMany(mappedBy = "stop",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    private List<ScheduleDetail> scheduleDetails;

    @OneToMany(mappedBy = "departure",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    private List<Booking> bookingsDeparture;

    @OneToMany(mappedBy = "arrival",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    private List<Booking> bookingsArrival;



    public Stop() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public List<ScheduleDetail> getScheduleDetails() {
        return scheduleDetails;
    }

    public void setScheduleDetails(List<ScheduleDetail> scheduleDetails) {
        this.scheduleDetails = scheduleDetails;
    }

    public Tinhtp getTinhtp() {
        return tinhtp;
    }

    public void setTinhtp(Tinhtp tinhtp) {
        this.tinhtp = tinhtp;
    }

    public List<Booking> getBookingsDeparture() {
        return bookingsDeparture;
    }

    public void setBookingsDeparture(List<Booking> bookingsDeparture) {
        this.bookingsDeparture = bookingsDeparture;
    }

    public List<Booking> getBookingsArrival() {
        return bookingsArrival;
    }

    public void setBookingsArrival(List<Booking> bookingsArrival) {
        this.bookingsArrival = bookingsArrival;
    }
}

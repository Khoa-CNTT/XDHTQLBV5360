package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @Column(name="car_id" , length = 11)
    private String id;

    @Column(name="name" , length = 30)
    private String name;

    @Column(name="opening_day" )
    private LocalDate openingDay;

    @Column(name="numberOfSeats")
    private byte number_of_seats;

    @Column(name="status" , length = 30)
    private String status;

    @Column(name="describe" , length = 255)
    private String describe;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "car",fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Trip> trips;

    @OneToMany(mappedBy = "car",fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Seat> seats;

    public Car() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getOpeningDay() {
        return openingDay;
    }

    public void setOpeningDay(LocalDate openingDay) {
        this.openingDay = openingDay;
    }

    public byte getNumber_of_seats() {
        return number_of_seats;
    }

    public void setNumber_of_seats(byte number_of_seats) {
        this.number_of_seats = number_of_seats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}

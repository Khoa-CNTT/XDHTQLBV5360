package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;
import vn.nhom24.bus_ticket_reservation_system.enums.SeatStatus;


@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private int id;

    @Column(name = "seat_name" ,length = 30)
    private String seatName;

    @Column(name = "seat_status", length = 20)
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_id")
    private Car car;


    public Seat(int id, String seatName, SeatStatus seatStatus, Car car) {
        this.id = id;
        this.seatName = seatName;
        this.seatStatus = seatStatus;
        this.car = car;
    }

    public Seat() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }



}

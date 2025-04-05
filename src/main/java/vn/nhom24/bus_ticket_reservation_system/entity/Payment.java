package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int id;

    @Column(name = "name" , length = 255)
    private String name;

    @Column(name = "amount")
    private double amount;

    @Column(name = "content", length = 255)
    private LocalDate content;

    @Column(name = "paydate")
    private LocalDate payDate;


}

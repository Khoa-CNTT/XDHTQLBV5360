package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Tinhtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tinhtp_id")
    private int id;

    @Column(name = "name" ,length = 30)
    private String name;

    @OneToMany(mappedBy = "tinhtp",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    private List<Stop> stops;

    public Tinhtp() {
    }

    public Tinhtp(int id, String stopName) {
        this.id = id;
        this.name = stopName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String stopName) {
        this.name = stopName;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }
}

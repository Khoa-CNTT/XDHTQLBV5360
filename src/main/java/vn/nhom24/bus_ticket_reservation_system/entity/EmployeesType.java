package vn.nhom24.bus_ticket_reservation_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "emp_type")
public class EmployeesType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emptype_id")
    private int id;

    @Column(name = "emptype_name", length = 30)
    private String name;

    public EmployeesType() {
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

    public void setName(String name) {
        this.name = name;
    }
}

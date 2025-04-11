package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.Stop;

import java.util.List;

@Service
public interface StopSevice {
    public List<Stop> findAll();

    public Stop findById(int id);

}

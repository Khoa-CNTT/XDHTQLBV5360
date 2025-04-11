package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;

import java.util.List;

@Service
public interface RouteSevice {
    public List<Route> findAll();
    public Route findById(int id);
}

package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.Car;

import java.util.List;

@Service
public interface CarService {
    List<Car> findAll();
}

package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.Car;
import vn.nhom24.bus_ticket_reservation_system.repository.CarRepository;
import vn.nhom24.bus_ticket_reservation_system.service.CarService;

import java.util.List;
@Service
@Slf4j
public class CarServiceIpml implements CarService {
    @Autowired
    CarRepository carRepository;
    @Override
    public List<Car> findAll() {
        List<Car> cars = carRepository.findAll();
        return cars;
    }
}

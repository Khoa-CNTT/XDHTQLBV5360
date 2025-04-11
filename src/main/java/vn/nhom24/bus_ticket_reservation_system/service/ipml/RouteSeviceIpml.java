package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;
import vn.nhom24.bus_ticket_reservation_system.repository.RouteRepository;
import vn.nhom24.bus_ticket_reservation_system.service.RouteSevice;

import java.util.List;

@Service
public class RouteSeviceIpml implements RouteSevice {
    @Autowired
    RouteRepository routeRepository;


    @Override
    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    @Override
    public Route findById(int id) {
        return routeRepository.findById(id).get();
    }
}

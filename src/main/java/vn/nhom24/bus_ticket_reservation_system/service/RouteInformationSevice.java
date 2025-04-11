package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.RouteInformation;

@Service
public interface RouteInformationSevice {
    public RouteInformation getRouteInformation(int routeId);
}

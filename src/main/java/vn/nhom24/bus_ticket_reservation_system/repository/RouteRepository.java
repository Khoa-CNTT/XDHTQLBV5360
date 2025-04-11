package vn.nhom24.bus_ticket_reservation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route,Integer> {
}

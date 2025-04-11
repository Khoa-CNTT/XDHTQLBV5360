package vn.nhom24.bus_ticket_reservation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.nhom24.bus_ticket_reservation_system.entity.PriceList;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;

import java.util.List;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList,Integer> {
    List<PriceList> findByRoute(Route route);
}

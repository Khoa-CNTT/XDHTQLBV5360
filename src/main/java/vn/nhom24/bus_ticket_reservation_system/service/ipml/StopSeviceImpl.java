package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.entity.Stop;
import vn.nhom24.bus_ticket_reservation_system.repository.StopRepository;
import vn.nhom24.bus_ticket_reservation_system.service.StopSevice;

import java.util.List;

@Service
public class StopSeviceImpl implements StopSevice {

    @Autowired
    StopRepository stopRepository;

    @Override
    public List<Stop> findAll() {
        return stopRepository.findAll();
    }

    @Override
    public Stop findById(int id) {
        return stopRepository.findById(id).get();
    }
}

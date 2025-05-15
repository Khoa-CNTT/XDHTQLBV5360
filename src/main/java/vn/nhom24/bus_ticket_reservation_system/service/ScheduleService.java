package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.ScheduleDetailViewDto;
import vn.nhom24.bus_ticket_reservation_system.DTO.ScheduleDisplayDto;
import vn.nhom24.bus_ticket_reservation_system.entity.Schedule;

import java.util.List;
import java.util.Optional;

@Service
public interface ScheduleService {
   List<ScheduleDisplayDto> findAllScheduleDisplayDtos();
   Optional<ScheduleDetailViewDto> findScheduleDetailViewById(int scheduleId);
}

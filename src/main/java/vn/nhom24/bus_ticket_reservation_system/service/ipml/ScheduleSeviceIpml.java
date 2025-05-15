package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.ScheduleDetailViewDto;
import vn.nhom24.bus_ticket_reservation_system.DTO.ScheduleDisplayDto;
import vn.nhom24.bus_ticket_reservation_system.entity.Schedule;
import vn.nhom24.bus_ticket_reservation_system.entity.ScheduleDetail;
import vn.nhom24.bus_ticket_reservation_system.entity.Stop;
import vn.nhom24.bus_ticket_reservation_system.repository.ScheduleRopository;
import vn.nhom24.bus_ticket_reservation_system.service.ScheduleService;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleSeviceIpml implements ScheduleService {
    @Autowired
    ScheduleRopository scheduleRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    @Override
    public List<ScheduleDisplayDto> findAllScheduleDisplayDtos() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(schedule -> {
                    ScheduleDisplayDto dto = new ScheduleDisplayDto();
                    dto.setId(schedule.getId());
                    dto.setScheduleName(schedule.getScheduleName());

                    // Process ScheduleDetails to find start and end points
                    List<ScheduleDetail> details = schedule.getScheduleDetails();

                    String startPoint = "N/A";
                    String endPoint = "N/A";

                    if (details != null && !details.isEmpty()) {
                        // Even if ORDER BY was used in the query, sorting here
                        // is a safer way to ensure the first/last stop is correctly identified
                        // based on stopNumber within the loaded collection.
                        details.sort(Comparator.comparingInt(ScheduleDetail::getStopNumber));

                        // Get the first ScheduleDetail and its Stop name
                        ScheduleDetail firstDetail = details.get(0);
                        if (firstDetail.getStop() != null) {
                            startPoint = firstDetail.getStop().getStopName();
                        }

                        // Get the last ScheduleDetail and its Stop name
                        ScheduleDetail lastDetail = details.get(details.size() - 1);
                        if (lastDetail.getStop() != null) {
                            endPoint = lastDetail.getStop().getStopName();
                        }
                    }

                    dto.setStartPoint(startPoint);
                    dto.setEndPoint(endPoint);
                    dto.setRoute(schedule.getRoute().getName());


                    return dto;
                })
               .toList();
    }

    @Override
    public Optional<ScheduleDetailViewDto> findScheduleDetailViewById(int scheduleId) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findByIdWithDetailsAndStops(scheduleId);
        return scheduleOptional
                .map(schedule -> {
                    ScheduleDetailViewDto dto = new ScheduleDetailViewDto();
                    dto.setId(schedule.getId());
                    dto.setScheduleName(schedule.getScheduleName());
                    dto.setRouteInfo(schedule.getRoute().getName());

                    // Chi tiết các điểm dừng
                    List<ScheduleDetailViewDto.StopDetailDto> stopDetailDtos = new ArrayList<>();
                    if (schedule.getScheduleDetails() != null && !schedule.getScheduleDetails().isEmpty()) {
                        // Mặc dù query đã ORDER BY, sắp xếp lại ở đây đảm bảo an toàn
                        schedule.getScheduleDetails().sort(Comparator.comparingInt(ScheduleDetail::getStopNumber));

                        for (ScheduleDetail detail : schedule.getScheduleDetails()) {
                            ScheduleDetailViewDto.StopDetailDto stopDto = new ScheduleDetailViewDto.StopDetailDto();
                            stopDto.setStopNumber(detail.getStopNumber());

                            Stop stop = detail.getStop();
                            if (stop != null) {
                                stopDto.setStopName(detail.getStop().getStopName());
                                stopDto.setProvinceName( stop.getTinhtp().getName());
                            } else {
                                stopDto.setStopName("N/A");
                            }
                            if (detail.getTime() != null) {
                                stopDto.setStopTime(detail.getTime().format(TIME_FORMATTER)); // Định dạng thời gian
                            } else {
                                stopDto.setStopTime("N/A");
                            }
                            stopDetailDtos.add(stopDto);
                        }
                    }
                    dto.setStopDetails(stopDetailDtos);

                    return dto;
            });
    }
}

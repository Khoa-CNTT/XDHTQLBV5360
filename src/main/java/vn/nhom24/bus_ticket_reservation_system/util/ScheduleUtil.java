package vn.nhom24.bus_ticket_reservation_system.util;

import vn.nhom24.bus_ticket_reservation_system.entity.Schedule;
import vn.nhom24.bus_ticket_reservation_system.entity.ScheduleDetail;

import java.time.LocalTime;

public class ScheduleUtil {
    public static LocalTime getStartTime(Schedule schedule) {
        return schedule.getScheduleDetails().stream()
                .map(ScheduleDetail::getTime)
                .min(LocalTime::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("Không có thời gian trong lịch trình"));
    }

    public static LocalTime getEndTime(Schedule schedule) {
        return schedule.getScheduleDetails().stream()
                .map(ScheduleDetail::getTime)
                .max(LocalTime::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("Không có thời gian trong lịch trình"));
    }
}

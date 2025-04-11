package vn.nhom24.bus_ticket_reservation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;
import vn.nhom24.bus_ticket_reservation_system.entity.Schedule;

import java.util.List;

@Repository
public interface ScheduleRopository extends JpaRepository<Schedule,Integer> {
    public List<Schedule> findByRoute(Route routeId);

    // tìm lịch trình phù hợp
    @Query(value = "SELECT DISTINCT s.* " +
            "FROM schedule s " +
            " JOIN schedule_detail sd1 ON s.schedule_id = sd1.schedule_id " +
            "JOIN schedule_detail sd2 ON sd1.schedule_id = sd2.schedule_id AND sd1.sche_detail_id <> sd2.sche_detail_id " +
            "JOIN stops st1 ON sd1.stop_id = st1.stop_id " +
            "JOIN stops st2 ON sd2.stop_id = st2.stop_id " +
            "JOIN tinhtp tp1 ON st1.tinhtp_id = tp1.tinhtp_id " +
            " JOIN tinhtp tp2 ON st2.tinhtp_id = tp2.tinhtp_id " +
            "WHERE (tp1.name = 'Đà Nẵng' OR tp2.name = 'Đà Nẵng') " +
            "AND st1.stop_id = ?1 " +
            "AND st2.stop_id = ?2 " +
            "AND sd2.stop_number > sd1.stop_number " , nativeQuery = true)
    // 1 trong stop phải thuộc tỉnh đà nẵng . 2 điểm có trong cùng 1 lịch trình . và thứ tụ điểm dùưng của to > from
    public List<Schedule> findMatchingSchedules( int fromStopId,  int toStopId);
}

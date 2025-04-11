package vn.nhom24.bus_ticket_reservation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.nhom24.bus_ticket_reservation_system.entity.Tinhtp;
@Repository
public interface TinhtpRepository extends JpaRepository<Tinhtp,Integer> {
    // lấy tỉnh xuất phát của lịch trình
    @Query(value = "SELECT t.* FROM tinhtp t" +
            " JOIN stops s ON t.tinhtp_id = s.tinhtp_id " +
            "JOIN schedule_detail sd ON s.stop_id = sd.stop_id " +
            "WHERE sd.schedule_id = ?1 ORDER BY sd.stop_number ASC LIMIT 1", nativeQuery = true)
    Tinhtp findTinhtpByScheduleIdWithMinStopNumber(int scheduleId);

    // lấy tỉnh kết thúc của lịch trình
    @Query(value = "SELECT t.* FROM tinhtp t JOIN stops s ON t.tinhtp_id = s.tinhtp_id " +
            "JOIN schedule_detail sd ON s.stop_id = sd.stop_id " +
            "WHERE sd.schedule_id = ?1 ORDER BY sd.stop_number DESC LIMIT 1", nativeQuery = true)
    Tinhtp findTinhtpByScheduleIdWithMaxStopNumber(int scheduleId);
}

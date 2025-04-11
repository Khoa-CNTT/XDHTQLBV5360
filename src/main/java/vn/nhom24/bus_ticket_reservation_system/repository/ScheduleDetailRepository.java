package vn.nhom24.bus_ticket_reservation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.nhom24.bus_ticket_reservation_system.entity.ScheduleDetail;

import java.util.List;

@Repository
public interface ScheduleDetailRepository extends JpaRepository<ScheduleDetail,Integer> {

    @Query("SELECT sd FROM ScheduleDetail sd WHERE sd.schedule.id = :scheduleId AND sd.stop.tinhtp.id = :tinhtpId ORDER BY sd.stopNumber ASC")
    List<ScheduleDetail> findScheduleDetailsByScheduleAndTinhtp(@Param("scheduleId") int scheduleId, @Param("tinhtpId") int tinhtpId);



    @Query("SELECT sd FROM ScheduleDetail sd WHERE sd.schedule.id = :scheduleId " +
            "AND sd.stop.id = :stopId ")
    ScheduleDetail findScheduleDetailByScheduleAndStop(@Param("scheduleId") int scheduleId, @Param("stopId") int stopId);

    @Query(value = "SELECT sd.* " +
            "FROM schedule_detail sd " +
            "WHERE sd.schedule_id = ?1 " +
            "ORDER BY stop_number desc " +
            "LIMIT 1" ,nativeQuery = true)
    ScheduleDetail getScheduleDetailStart(int scheduleId);

    List<ScheduleDetail> findByScheduleIdIn(List<Integer> scheduleIds);
}

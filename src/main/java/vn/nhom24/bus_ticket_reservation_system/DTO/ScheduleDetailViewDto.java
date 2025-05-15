package vn.nhom24.bus_ticket_reservation_system.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDetailViewDto {
    private int id;
    private String scheduleName;
    private String routeInfo; // Thông tin tuyến (ví dụ: "Điểm đầu - Điểm cuối" từ Route entity)
    private List<StopDetailDto> stopDetails; // Danh sách các điểm dừng chi tiết

    // DTO con cho mỗi điểm dừng
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StopDetailDto { // Nested static class
        private int stopNumber;
        private String stopName;
        private String stopTime;
        private String provinceName;
    }
}
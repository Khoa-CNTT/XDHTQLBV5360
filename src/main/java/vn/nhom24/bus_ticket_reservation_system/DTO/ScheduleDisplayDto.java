package vn.nhom24.bus_ticket_reservation_system.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDisplayDto {
    private int id;
    private String scheduleName;
    private String route; // Tên xe
    private String startPoint; // Tên điểm bắt đầu
    private String endPoint;   // Tên điểm kết thúc
}

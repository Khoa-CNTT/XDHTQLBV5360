package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.CreateTrip;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripAdminDTO;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripCard;
import vn.nhom24.bus_ticket_reservation_system.entity.Tinhtp;
import vn.nhom24.bus_ticket_reservation_system.entity.Trip;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TripSevice {
    List<TripCard> filterTrips(int from, int to, LocalDate date);

    public List<Tinhtp> findAll();

    // hàm set Trip_name theo scheduleId
    void setTripNameBySchedule(int tripId);

    // hàm lấy tên của tỉnh xuất phát và tỉnh kết thúc
    String getNameTripcard(int scheduleId);

    Trip findById(int tripId);

    List<TripAdminDTO> tripAdminDTOConver(List<Trip> trips);

    // lấy tripcard hiển thi ra giao diện user
    TripCard getTripCard(Trip trip, int fromId, int toId );

    // lấy số lượng ghế còn trống
    int getNumberOfEmptySeats(int tripId);

    // lấy TripAdminDTO hiển thi ra giao diện admin
    List<TripAdminDTO> getAll();


    Page<TripAdminDTO> getAll(int pageNo);

    // Tìm Trip theo điều kiện date
    List<TripAdminDTO> searchByDate(LocalDate date) ;
    List<TripAdminDTO> searchByDateForCheckin(LocalDate date) ;

    // Tìm Trip theo điều kiện rote
    List<TripAdminDTO> searchByRoute(int route) ;

    // Tìm Trip theo điều kiện date va route
    List<TripAdminDTO> searchByDateAndRoute(LocalDate date, int route) ;


    boolean addtrip(CreateTrip createTrip);

    Trip updateTrip(CreateTrip createTrip, int tripId);

    void deleteTrip(int tripId);
}

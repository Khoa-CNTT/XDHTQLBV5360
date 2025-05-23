package vn.nhom24.bus_ticket_reservation_system.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TripCard {
    private int tripId;
    private String tripNamel; // tên chuyến đi
    private LocalTime starTime;
    private double price; // giá vé
    private int emptySeats; // sô ghế còn trống
    private int reservedSeats;// sô ghế đã đặt
    private int purchasedSeats;// sô ghế đã mua
    private LocalDate departureDate; // ngày khởi hành
    private LocalTime departureTime;  // Thời gian xuất phát
    private LocalTime arrivalTime;  // Thời gian đến nơi
    private String departureLocation ; // Điểm xuất phát
    private String arrivalLocation ; // Điểm đến
    private String vehicleCategory; // loại xe
    private int routeId; // id tuyến đường

    public TripCard() {
    }

    public TripCard(int tripId, String tripNamel, double price, int emptySeats, LocalTime departureTime, LocalTime arrivalTime, String departureLocation, String arrivalLocation, String vehicleCategory) {
        this.tripId = tripId;
        this.tripNamel = tripNamel;
        this.price = price;
        this.emptySeats = emptySeats;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.vehicleCategory = vehicleCategory;
    }

    public LocalTime getStarTime() {
        return starTime;
    }

    public void setStarTime(LocalTime starTime) {
        this.starTime = starTime;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getTripNamel() {
        return tripNamel;
    }

    public void setTripNamel(String tripNamel) {
        this.tripNamel = tripNamel;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getEmptySeats() {
        return emptySeats;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public void setEmptySeats(int emptySeats) {
        this.emptySeats = emptySeats;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(String vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public int getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(int reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public int getPurchasedSeats() {
        return purchasedSeats;
    }

    public void setPurchasedSeats(int purchasedSeats) {
        this.purchasedSeats = purchasedSeats;
    }
    public int getRouteId() {
        return routeId;
    }
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    @Override
    public String toString() {
        return "TripCard{" +
                "tripId=" + tripId +
                ", tripNamel='" + tripNamel + '\'' +
                ", price=" + price +
                ", emptySeats=" + emptySeats +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", departureLocation='" + departureLocation + '\'' +
                ", arrivalLocation='" + arrivalLocation + '\'' +
                ", vehicleCategory='" + vehicleCategory + '\'' +
                '}';
    }
}

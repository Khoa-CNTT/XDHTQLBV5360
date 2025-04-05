package vn.nhom24.bus_ticket_reservation_system.enums;

public enum SeatStatus {
    EMPTY("EMPTY"), // Ghế trống
    BOOKED("BOOKED"), // Ghế đã được đặt , đã được nhân viên xác nhận
    RESERVED("RESERVED"), // Ghế đang giữ chỗ
    SOLD("SOLD"),// ghế đã có người mua và thanh toán
    NOT_FOR_SALE("NOT_FOR_SALE"), // ghế ko bán
    PENDING("PENDING");// khách hàng đặt ghế nhưng chưa được nhân viên xác nhận đang chờ xử lý

    private final String status;

    SeatStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }



}

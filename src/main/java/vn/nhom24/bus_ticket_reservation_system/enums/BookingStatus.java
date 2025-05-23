package vn.nhom24.bus_ticket_reservation_system.enums;

public enum BookingStatus {
    BOOKED("BOOKED","Xác nhận"), // GIỮ CHỖ NHƯNG CHỦA TAHNH TOÁN
    CANCELLED("CANCELLED", "hủy"),// ghế đã có người mua và thanh toán
    PAID("PAID", "Đã thanh toán"), //Booking đã được thanh toán hoàn toàn.
    PENDING("PENDING", "Đợi xác nhận"),
    COMPLETE("COMPLETE", "hoàn thành"),
    CHECKED_IN("CHECKED_IN","da check in"),;

    private final String status;
    private final String displayName;

    BookingStatus(String status, String displayName) {
        this.status = status;
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public String getDisplayName() {
        return displayName;
    }
}

package vn.nhom24.bus_ticket_reservation_system.enums;

public enum TripStatus {
    BOOKED("BOOKED","Chưa xuất phát"), // GIỮ CHỖ NHƯNG CHỦA TAHNH TOÁN
    COMPLETE("COMPLETE", "Đã hoàn thành");// ghế đã có người mua và thanh toán

    private final String status;
    private final String displayName;

    TripStatus(String status, String displayName) {
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

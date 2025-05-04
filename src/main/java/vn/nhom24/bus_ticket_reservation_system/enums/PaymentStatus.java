package vn.nhom24.bus_ticket_reservation_system.enums;

public enum PaymentStatus {
    PENDING("PENDING", "chờ"),
    CANCELLED("CANCELLED", "Đã hủy"),
    PAID("PAID", "Đã thanh toán");

    private final String status;
    private final String displayName;

    PaymentStatus(String status, String displayName) {
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

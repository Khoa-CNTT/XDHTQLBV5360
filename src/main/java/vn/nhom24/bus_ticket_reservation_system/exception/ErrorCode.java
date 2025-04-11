package vn.nhom24.bus_ticket_reservation_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    BOOKING_ALREADY_CHECKED_IN(1002, "Ticket already checked in", HttpStatus.BAD_REQUEST),
    BOOKING_CANCELED(1003, "Ticket has been canceled", HttpStatus.BAD_REQUEST),
    WRONG_BOOKING(1004, "Wrong booking for this ticket", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_FOUND(2001, "Booking not found", HttpStatus.NOT_FOUND),

    // Các lỗi chung
    UNAUTHORIZED(9999, "Unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_DATA(9001, "Invalid data", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(9000, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    ;

    private  int code;
    private  String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message , HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}

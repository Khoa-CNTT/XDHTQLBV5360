package vn.nhom24.bus_ticket_reservation_system.util;

public class EmailUtils {
    public static String getVerificationUrl(String email, String token) {
        return "http://localhost:8080/register/verify-account?email="+email+"&token="+token;
    }

}

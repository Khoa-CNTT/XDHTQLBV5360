package vn.nhom24.bus_ticket_reservation_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.nhom24.bus_ticket_reservation_system.enums.EmailType;
import vn.nhom24.bus_ticket_reservation_system.service.EmailSevice;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BusTicketReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusTicketReservationSystemApplication.class, args);
	}

	//@Bean
//	public CommandLineRunner commandLineRunner(EmailSevice emailSevice) {
//		return runner -> {
//			// Example usage of the email service
//			//gửi email
//			Map<String,Object> inforBooking = new HashMap<>();
//			String name = "Nguyễn Văn A";
//			inforBooking.put("name","Nguyễn Văn A");
//			inforBooking.put("phoneNumber","0971025649");
//			inforBooking.put("listSeat","1,2,3");
//			inforBooking.put("tripName","Hà Nội - Đà Nẵng");
//			inforBooking.put("starTime","08:00");
//			inforBooking.put("departureDate","2024-10-27");
//			inforBooking.put("departureTime","08:00");
//			inforBooking.put("departureLocation","Hà Nội");
//			inforBooking.put("arrivalLocation","Đà Nẵng");
//			inforBooking.put("bookingCode","123456");
//			emailSevice.sendHtmlEmail("hoangvantu2@dtu.edu.vn", EmailType.TICKET_CONFIRMATION, inforBooking);
//		};
//	}
}

package vn.nhom24.bus_ticket_reservation_system.controller.Admin;

import com.google.zxing.WriterException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.nhom24.bus_ticket_reservation_system.DTO.BookingDTO;
import vn.nhom24.bus_ticket_reservation_system.DTO.CheckInRequest;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripAdminDTO;
import vn.nhom24.bus_ticket_reservation_system.DTO.resonse.ApiResponse;
import vn.nhom24.bus_ticket_reservation_system.service.BookingSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RequestMapping(value = "/driver")
@Controller
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DriverController {
    TripSevice tripSevice;
    BookingSevice bookingSevice;

    @GetMapping
    public String driver(){
        return "admin/adminhomepage";
    }

    @GetMapping(value = "/bus")
    public String checkIn(Model model){
        List<TripAdminDTO> trips = tripSevice.searchByDateForCheckin(LocalDate.now());

        model.addAttribute("trips", trips);
        return "admin/bus";
    }

    @GetMapping("/checkin/{tripCode}")
    public String showCheckinPage(@PathVariable(value = "tripCode") String tripCode, Model model) {
        log.info("Trip code: " + tripCode);
        model.addAttribute("currentTripCode", tripCode);
        return "admin/checkin";
    }


    @PostMapping("/api/checkin/{tripCode}")
    @ResponseBody
    public ApiResponse<BookingDTO> checkInTicket(@RequestBody CheckInRequest request, @PathVariable(value = "tripCode") String tripCode) throws IOException, WriterException {

            String idBooking = request.getIdBooking();

            BookingDTO booking = bookingSevice.checkInTicket(idBooking, tripCode);

            log.info(booking.getBookingCode());
            return ApiResponse.<BookingDTO>builder()
                    .message("Check in successfully")
                    .result(booking)
                    .build();
    }
}

package vn.nhom24.bus_ticket_reservation_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripCard;
import vn.nhom24.bus_ticket_reservation_system.entity.Stop;
import vn.nhom24.bus_ticket_reservation_system.entity.Tinhtp;
import vn.nhom24.bus_ticket_reservation_system.entity.User;
import vn.nhom24.bus_ticket_reservation_system.service.StopSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;
import vn.nhom24.bus_ticket_reservation_system.service.UserSevice;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class TripController {
    @Autowired
    UserSevice userService;

    @Autowired
    TripSevice tripSevice;

    @Autowired
    StopSevice stopSevice;


    @GetMapping(value = "/public/schedule")
    public String showSearchForm(Model model){
        List<Tinhtp> tinhtps = tripSevice.findAll();

        model.addAttribute("listTinhTp",tinhtps);
        return "public/search-schedule";
    }


    @PostMapping(value = "/public/submit")
    public String handleSearchForm(@RequestParam(value = "departure",required = true) int departure,
                                   @RequestParam(value = "destination",required = true) int destination,
                                   @RequestParam(value = "departureDate",required = true)
                                   @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate  departureDate,
                                   Model model, Principal principal){
        if (principal != null) {
            // Giả sử bạn có service lấy thông tin user từ principal
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("currentUser", user);
        }

        String myError = "";

        // giữ lại điểm đi và điểm đến mà người dùng đã chon
        Stop deparTure = stopSevice.findById(departure);
        Stop destinaTion = stopSevice.findById(destination);

        // kiểm tra xem stop có tồn tại hay không
        if(deparTure == null || destinaTion == null){
            myError = "Điểm dừng không hợp lệ";
            model.addAttribute("my_error",myError);
            return "public/booking";
        }

        // dữ liệu cho form search để search lại
        List<Tinhtp> tinhtps = tripSevice.findAll();


        model.addAttribute("listTinhTp",tinhtps);
        model.addAttribute("deparTure",deparTure);
        model.addAttribute("destinaTion",destinaTion);
        model.addAttribute("departureDate",departureDate);


        List<TripCard> tripCards = tripSevice.filterTrips(departure, destination, departureDate);

        if(tripCards.isEmpty()){
            myError = "Không có chuyến xe phù hợp hoặc đã hết chỗ";
            model.addAttribute("my_error",myError);
            return "public/booking";
        }


        // các chuyến xe thảo mãn điều kiện
        model.addAttribute("ListTrip",tripCards);

        return "public/booking";
    }
}

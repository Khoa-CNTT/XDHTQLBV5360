package vn.nhom24.bus_ticket_reservation_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.nhom24.bus_ticket_reservation_system.entity.Booking;
import vn.nhom24.bus_ticket_reservation_system.entity.Tinhtp;
import vn.nhom24.bus_ticket_reservation_system.entity.User;
import vn.nhom24.bus_ticket_reservation_system.service.BookingSevice;
import vn.nhom24.bus_ticket_reservation_system.service.RatingSevice;
import vn.nhom24.bus_ticket_reservation_system.service.UserSevice;
import vn.nhom24.bus_ticket_reservation_system.service.ipml.TripSeviceImpl;

import java.security.Principal;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    UserSevice userSevice;

    @Autowired
    BookingSevice bookingSevice;

    @Autowired
    TripSeviceImpl tripSevice;

    @Autowired
    RatingSevice ratingSevice;

    @GetMapping()
    public String showHomePage(Model model, Principal principal){
        String email = principal.getName();
        User user = userSevice.findByEmail(email);

        List<Tinhtp> tinhtps = tripSevice.findAll();

        model.addAttribute("listTinhTp",tinhtps);
        return "public/user";
    }


    @GetMapping("/history")
    public String showHistoryPage(Model model, Principal principal){
        String email = principal.getName();
        User user = userSevice.findByEmail(email);


        List<Booking> bookings = bookingSevice.findPaidOrBookedByEmail(email);


        model.addAttribute("bookings", bookings);


        return "public/history";
    }

    @GetMapping("/info")
    public String userInfo(Model model, Principal principal) {
        String username = principal.getName();
        User user = userSevice.findByEmail(username);
        model.addAttribute("user", user);
        return "user/userinfo";
    }


    @PostMapping("/rating")
    public String rating(Model model,
                         Principal principal,
                         @RequestParam("bookingId")  int bookingId,
                         @RequestParam("rating")  byte point,
                         @RequestParam("describe")  String describe) {

        String phoneNumber = principal.getName();
        User user = userSevice.findByPhoneNumber(phoneNumber);

        String my_error = "đánh giá thành công";
        if(ratingSevice.checkRating(bookingId)){
            my_error ="Vé này đã được đánh giá đánh giá";
        }
        ratingSevice.saveRating(phoneNumber,bookingId,point,describe);

        List<Booking> bookings = bookingSevice.findByPhoneNumber(phoneNumber);
        model.addAttribute("bookings", bookings);
        model.addAttribute("my_error",my_error );

        return "user/userhomepage";
    }
}

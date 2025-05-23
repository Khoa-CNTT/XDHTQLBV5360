package vn.nhom24.bus_ticket_reservation_system.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.nhom24.bus_ticket_reservation_system.DTO.PasswordChangeDto;
import vn.nhom24.bus_ticket_reservation_system.entity.Booking;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;
import vn.nhom24.bus_ticket_reservation_system.entity.Tinhtp;
import vn.nhom24.bus_ticket_reservation_system.entity.User;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;
import vn.nhom24.bus_ticket_reservation_system.enums.TripStatus;
import vn.nhom24.bus_ticket_reservation_system.service.*;
import vn.nhom24.bus_ticket_reservation_system.service.ipml.TripSeviceImpl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    UserSevice userSevice;

    @Autowired
    BookingSevice bookingSevice;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TripSeviceImpl tripSevice;

    @Autowired
    RatingSevice ratingSevice;
    RouteSevice routeSevice;


    @GetMapping("/info")
    public String userInfo(Model model, Principal principal) {
        String username = principal.getName();
        User user = userSevice.findByEmail(username);
        model.addAttribute("user", user);
        return "user/profile-static";
    }

    @GetMapping("/tickets")
    public String manageTickets(Model model, Principal principal) {
        String username = principal.getName();
        User user = userSevice.findByEmail(username);
        List<Booking> bookings = bookingSevice.findPaidByEmailNotRate(user.getEmail(),List.of(BookingStatus.PAID), TripStatus.BOOKED);

        model.addAttribute("bookings", bookings);
        model.addAttribute("user", user);
        return "user/ticket-management";
    }

    @PostMapping("/rating")
    public String rating(Model model,
                         Principal principal,
                         @RequestParam("bookingId") int bookingId,
                         @RequestParam("rating") byte point,
                         @RequestParam("describe") String describe) {

        String username = principal.getName();
        User user = userSevice.findByEmail(username);

        String my_error = "Đánh giá thành công";
        if (ratingSevice.checkRating(bookingId)) {
            my_error = "Vé này đã được đánh giá đánh giá";
        }
        ratingSevice.saveRating(user, bookingId, point, describe);

        List<Booking> bookings = bookingSevice.findPaidByEmailNotRate(user.getEmail(), List.of(BookingStatus.PAID), TripStatus.COMPLETE);
        bookings.stream()
                .filter(booking -> !booking.isRated())
                .toList();
        model.addAttribute("bookings", bookings);
        model.addAttribute("my_error", my_error);
        model.addAttribute("user", user);

        return "user/review";
    }




    @GetMapping("/reviews")
    public String reviewTickets(Model model, Principal principal) {
        String username = principal.getName();
        User user = userSevice.findByEmail(username);
        List<Booking> bookings = bookingSevice.findPaidByEmailNotRate(user.getEmail(), List.of(BookingStatus.PAID), TripStatus.COMPLETE);
        bookings = bookings.stream()
                .filter(booking -> !booking.isRated())
                .toList();
        bookings.forEach(booking -> {log.info("boking : "+booking.getId());});
        model.addAttribute("bookings", bookings);
        model.addAttribute("user", user);
        return "user/review";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model, Principal principal) {
        String username = principal.getName();
        User user = userSevice.findByEmail(username);


        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        model.addAttribute("user", user);
        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String processChangePassword(Model model, Principal principal,
                                        @Valid @ModelAttribute("passwordChangeDto") PasswordChangeDto passwordDto,
                                        BindingResult bindingResult) {
        String username = principal.getName();
        User user = userSevice.findByEmail(username);

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "user/change-password";
        }
        String message = "";
        // 2. Kiểm tra logic nghiệp vụ (mật khẩu mới và xác nhận có khớp không)
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            message = "Mật khẩu xác nhận không khớp";
            model.addAttribute("Message", message);
            model.addAttribute("user", user);
            return "user/change-password";
        }



        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassWord())) {
            message = "Mật khẩu cũ không chính xác";
            model.addAttribute("Message", message);
            model.addAttribute("user", user);
            return "user/change-password";
        }


        try {

            // 6. Cập nhật mật khẩu mới cho user
            user.setPassWord(passwordDto.getConfirmPassword());
            userSevice.save(user); // Lưu thay đổi vào DB

            message =  "Thay đổi mật khẩu thành công!";
            model.addAttribute("Message", message);
            model.addAttribute("user", user);
            return "user/change-password";

        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi
            message =  "Đã xảy ra lỗi trong quá trình thay đổi mật khẩu. Vui lòng thử lại.";
            model.addAttribute("Message", message);
            model.addAttribute("user", user);
            return "user/change-password";
        }

    }
}

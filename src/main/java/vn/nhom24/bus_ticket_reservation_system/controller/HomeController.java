package vn.nhom24.bus_ticket_reservation_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;
import vn.nhom24.bus_ticket_reservation_system.entity.Tinhtp;
import vn.nhom24.bus_ticket_reservation_system.entity.User;
import vn.nhom24.bus_ticket_reservation_system.service.RouteSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;
import vn.nhom24.bus_ticket_reservation_system.service.UserSevice;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserSevice userService;

    @Autowired
    RouteSevice routeSevice;

    @Autowired
    TripSevice tripSevice;

    @RequestMapping(value = "/")
    public String showHomePage(Model model, Principal principal){
        List<Route> routes = routeSevice.findAll();
        List<Tinhtp> tinhtps = tripSevice.findAll();
        model.addAttribute("listTinhTp",tinhtps);
        if (principal != null) {
            // Giả sử bạn có service lấy thông tin user từ principal
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("currentUser", user);
        }

        model.addAttribute("routes" , routes);
        return "public/client";
    }
}

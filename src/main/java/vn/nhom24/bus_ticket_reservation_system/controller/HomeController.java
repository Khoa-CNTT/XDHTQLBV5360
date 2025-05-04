package vn.nhom24.bus_ticket_reservation_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;
import vn.nhom24.bus_ticket_reservation_system.entity.Tinhtp;
import vn.nhom24.bus_ticket_reservation_system.service.RouteSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    RouteSevice routeSevice;

    @Autowired
    TripSevice tripSevice;

    @RequestMapping(value = "/")
    public String showHomePage(Model model){
        List<Route> routes = routeSevice.findAll();
        List<Tinhtp> tinhtps = tripSevice.findAll();

        model.addAttribute("listTinhTp",tinhtps);


        model.addAttribute("routes" , routes);
        return "public/client";
    }
}

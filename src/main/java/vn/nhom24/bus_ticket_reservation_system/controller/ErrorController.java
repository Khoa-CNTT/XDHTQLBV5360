package vn.nhom24.bus_ticket_reservation_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping(value = "/showPage403")
    public String page403(){
        return "public/403";
    }

    @GetMapping(value = "/404")
    public String page404(){
        return "public/404";
    }

    //  oki
    @GetMapping(value = "/401")
    public String page401(){
        return "public/401";
    }

    //  oki
    @GetMapping(value = "/500")
    public String page500(){
        return "public/500";
    }

}

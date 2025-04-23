package vn.nhom24.bus_ticket_reservation_system.controller.Admin;

import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import vn.nhom24.bus_ticket_reservation_system.DTO.CreateTrip;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripAdminDTO;
import vn.nhom24.bus_ticket_reservation_system.entity.*;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;
import vn.nhom24.bus_ticket_reservation_system.repository.CarRepository;
import vn.nhom24.bus_ticket_reservation_system.repository.PriceListRepository;
import vn.nhom24.bus_ticket_reservation_system.repository.RouteRepository;
import vn.nhom24.bus_ticket_reservation_system.repository.ScheduleRopository;
import vn.nhom24.bus_ticket_reservation_system.service.BookingSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {

    BookingSevice bookingSevice;
    TripSevice tripSevice;
    PriceListRepository priceListRepository;
    RouteRepository routeRepository;
    ScheduleRopository scheduleRopository;
    CarRepository carRepository;


    @GetMapping()
    public String showHomePage(Model model){
        return "admin/adminhomepage";
    }

    @GetMapping(value = "/tables")
    public String table(){
        return "admin/tables";
    }


    @GetMapping(value = "/booking")
    public String booking(Model model,@RequestParam(name = "page",defaultValue = "1") int pageNo,
                          @RequestParam(name = "keyword", required = false) String keyword){

        int range = 1; // Số lượng trang hiển thị xung quanh trang hiện tại

        Page<Booking> bookings = null;

        if(keyword != null){
            bookings = bookingSevice.searchBookingsByKeyword(keyword,pageNo);
            model.addAttribute("keyword",keyword);
        }else{
            bookings = bookingSevice.findAllByOrderByDateCreatedDesc(pageNo);
        }

        int totalPages = bookings.getTotalPages();

        // Tính toán trang bắt đầu và kết thúc
        int startPage = Math.max(1, pageNo - range);
        int endPage = Math.min(totalPages, pageNo + range);


        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("listBooking",bookings);
        return "admin/booking";
    }

    @GetMapping(value = "/search" )
    public String table(Model model, @RequestParam(name = "keyword", required = false) String keyword){
        List<Booking> bookings = bookingSevice.searchBookingsByKeyword(keyword);

        model.addAttribute("listBooking",bookings);

        return "admin/booking";
    }


    @GetMapping(value = "/updateStatus")
    public String updateStatus(Model model,@RequestParam ("id") int bookingId,
                               @RequestParam("action") String action,
                               @RequestParam(name = "page",defaultValue = "1") int pageNo) throws IOException, WriterException {
        Booking booking = bookingSevice.getBookingById(bookingId);

        if ("confirm".equals(action)) {
            bookingSevice.updateStatus(booking, BookingStatus.BOOKED);

        } else if ("cancel".equals(action)) {
            bookingSevice.updateStatus(booking, BookingStatus.CANCELLED);
        } else {
            return "redirect:/error";
        }

        return "redirect:/admin/booking";
    }

    @GetMapping(value = "/trip")
    public String trip(Model model,
                        @RequestParam(name = "page",defaultValue = "1") int pageNo,
                       @RequestParam (value = "date",required = false ) LocalDate date,
                       @RequestParam (value = "route",required = false,defaultValue = "-1") int route
    ){
        Route routeInput = null;
        List<Route> routes = routeRepository.findAll();

        // Tìm theo điều kiện date nếu route không được cung cấp
        if (date != null && route == -1) {
            List<TripAdminDTO> listTrip = tripSevice.searchByDate(date);
            model.addAttribute("listTrip",listTrip);

            // Tìm theo điều kiện route nếu date không được cung cấp
        } else if (date == null && route > 0) {
            List<TripAdminDTO> listTrip = tripSevice.searchByRoute(route);
            model.addAttribute("listTrip",listTrip);
            // route đã chọn
            routeInput =  routes.stream()
                    .filter(route1 -> route1.getId() == route)
                    .findFirst()
                    .get();
            // Tìm theo cả hai điều kiện date và route nếu cả hai điều kiện đều tồn tại
        } else if (date != null && route >= 0) {

            System.out.println(" day là loi của tìm theo date");
            if(route == 0){
                System.out.println(" day là loi của tìm theo route = 0");
                List<TripAdminDTO> listTrip = tripSevice.searchByDate(date);
                model.addAttribute("listTrip",listTrip);
            }else{
                List<TripAdminDTO> listTrip = tripSevice.searchByDateAndRoute(date,route);
                model.addAttribute("listTrip",listTrip);
                routeInput =  routes.stream()
                        .filter(route1 -> route1.getId() == route)
                        .findFirst()
                        .get();
            }

        } else {

            System.out.println(" day là loi của tìm BANTUMLUON ");
            Page<TripAdminDTO> listTrip = tripSevice.getAll(pageNo);
            model.addAttribute("listTrip",listTrip);

            if (listTrip != null) {
                int totalPages = listTrip.getTotalPages();
                int range = 1;
                // Tính toán trang bắt đầu và kết thúc
                int startPage = Math.max(1, pageNo - range);
                int endPage = Math.min(totalPages, pageNo + range);


                model.addAttribute("currentPage", pageNo);
                model.addAttribute("totalPages", totalPages);
                model.addAttribute("startPage", startPage);
                model.addAttribute("endPage", endPage);
            }
        }

        model.addAttribute("date",date);
        model.addAttribute("routeInput",routeInput);
        model.addAttribute("routes",routes);

        return "admin/trip";
    }

    @InitBinder
    public void initBinder(WebDataBinder data){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        data.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping(value = "/trip/register")
    public String showForm(Model model){

        List<Car> carList = carRepository.findAll();
        List<Schedule> scheduleList = scheduleRopository.findAll();
        List<PriceList> priceListList = priceListRepository.findAll();


        model.addAttribute("carList",carList);
        model.addAttribute("scheduleList",scheduleList);
        model.addAttribute("priceListList",priceListList);
        model.addAttribute("createTrip",new CreateTrip());

        return "admin/createTrip";
    }

    @PostMapping (value = "/trip/register")
    public String saveTrip(@Valid @ModelAttribute("createTrip") CreateTrip createTrip, BindingResult result,Model model
                           ){
        List<Car> carList = carRepository.findAll();
        List<Schedule> scheduleList = scheduleRopository.findAll();
        List<PriceList> priceListList = priceListRepository.findAll();
        if (result.hasErrors()) {

            model.addAttribute("carList",carList);
            model.addAttribute("scheduleList",scheduleList);
            model.addAttribute("priceListList",priceListList);
            return "admin/createTrip";
        }
        String my_error = "";
        if(tripSevice.addtrip(createTrip)){
            my_error = "them moi thanh cong ";
        }else{
            my_error = "them moi thất bại ";
        }


        model.addAttribute("carList",carList);
        model.addAttribute("scheduleList",scheduleList);
        model.addAttribute("priceListList",priceListList);
        model.addAttribute("my_error",my_error);
        return "admin/createTrip";
    }


    @GetMapping(value = "/chart")
    public String chart(){
        return "admin/charts";
    }


    @GetMapping(value = "/schedule")
    public String schedule(){
        return "admin/list-schedule";
    }


}

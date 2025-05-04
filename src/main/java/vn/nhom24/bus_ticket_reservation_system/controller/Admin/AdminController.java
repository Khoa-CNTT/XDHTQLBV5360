package vn.nhom24.bus_ticket_reservation_system.controller.Admin;

import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import vn.nhom24.bus_ticket_reservation_system.DTO.*;
import vn.nhom24.bus_ticket_reservation_system.entity.*;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;
import vn.nhom24.bus_ticket_reservation_system.enums.PaymentStatus;
import vn.nhom24.bus_ticket_reservation_system.enums.TripStatus;
import vn.nhom24.bus_ticket_reservation_system.repository.*;
import vn.nhom24.bus_ticket_reservation_system.service.BookingSevice;
import vn.nhom24.bus_ticket_reservation_system.service.PaymentService;
import vn.nhom24.bus_ticket_reservation_system.service.RevenueService;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
    PaymentService paymentService;
    RevenueService revenueService;
    private final TripRepository tripRepository;


    @GetMapping()
    public String showHomePage(Model model){



        model.addAttribute("viewDashboardDto", ViewDashBoardResponse.builder().build());
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
            paymentService.createPayment(booking);
        } else if ("cancel".equals(action)) {
            bookingSevice.updateStatus(booking, BookingStatus.CANCELLED);
        } else {
            return "redirect:/error";
        }

        return "redirect:/admin/booking";
    }








    // endpoint cho việc lay lại thông tin chuyến đi và tìm kiếm
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

    @GetMapping(value = "/trip/updateStatus")
    public String tripUpdateStatus(Model model,@RequestParam ("id") int bookingId,
                                   @RequestParam("action") String action,
                                   @RequestParam("tripId") String tripId) throws IOException, WriterException {

        Trip trip = tripSevice.findById(Integer.parseInt(tripId));

        List<Car> carList = carRepository.findAll();
        List<Schedule> scheduleList = scheduleRopository.findAll();
        List<PriceList> priceListList = priceListRepository.findAll();
        List<Booking> bookings = bookingSevice.findByTripId(trip.getId());

        CreateTrip createTrip = this.fromTrip(trip);

        model.addAttribute("carList",carList);
        model.addAttribute("scheduleList",scheduleList);
        model.addAttribute("priceListList",priceListList);
        model.addAttribute("createTrip",createTrip);
        model.addAttribute("bookings",bookings);
        model.addAttribute("trip",trip);

        Booking booking = bookingSevice.getBookingById(bookingId);

        if ("confirm".equals(action)) {
            bookingSevice.updateStatus(booking, BookingStatus.BOOKED);
            paymentService.createPayment(booking);
        } else if ("cancel".equals(action)) {
            bookingSevice.updateStatus(booking, BookingStatus.CANCELLED);
        } else {
            return "redirect:/error";
        }

        return "admin/updateTrip";
    }



    @PostMapping(value ="/trip/{tripId}/updateStatus")
    public String updateStatus(@PathVariable("tripId") String tripId,
                                   @RequestParam("action") String action
    , Model model) throws IOException, WriterException {
        Trip trip = tripSevice.findById(Integer.parseInt(tripId));
        List<PaymentResponse> payments = paymentService.getAllPayments(tripId);

        if ("start".equals(action)) {
           trip.setStatus(TripStatus.RUNNING);
        } else if ("complete".equals(action)) {
            trip.setStatus(TripStatus.COMPLETE);
        } else {
            return "redirect:/error";
        }

        tripRepository.save(trip);

        model.addAttribute("Payments",payments);
        model.addAttribute("trip",trip);
        return "admin/payment";
    }

    @GetMapping(value = "/trip/{tripId}")
    public String getTrip(@PathVariable(value = "tripId") int tripId, Model model){
        Trip trip = tripSevice.findById(tripId);
        List<Car> carList = carRepository.findAll();
        List<Schedule> scheduleList = scheduleRopository.findAll();
        List<PriceList> priceListList = priceListRepository.findAll();
        List<Booking> bookings = bookingSevice.findByTripId(trip.getId());

        CreateTrip createTrip = this.fromTrip(trip);

        model.addAttribute("carList",carList);
        model.addAttribute("scheduleList",scheduleList);
        model.addAttribute("priceListList",priceListList);
        model.addAttribute("createTrip",createTrip);
        model.addAttribute("bookings",bookings);
        model.addAttribute("trip",trip);
        return "admin/updateTrip";
    }

    @GetMapping(value = "/trip/{tripId}/detail")
    public String getTripDetaile(@PathVariable(value = "tripId") int tripId, Model model){
        Trip trip = tripSevice.findById(tripId);
        List<Booking> bookings = bookingSevice.findByTripId(trip.getId());

        model.addAttribute("bookings",bookings);
        model.addAttribute("trip",trip);
        return "admin/tripdetail";
    }

    @PostMapping("/trip/{tripId}")
    public String updateProduct(@PathVariable("tripId") int tripId, @ModelAttribute("createTrip") CreateTrip createTrip,
                                BindingResult result, Model model) {
        Trip trip = tripSevice.findById(tripId);
        List<Car> carList = carRepository.findAll();
        List<Schedule> scheduleList = scheduleRopository.findAll();
        List<PriceList> priceListList = priceListRepository.findAll();
        List<Booking> bookings = bookingSevice.findByTripId(trip.getId());
        if (result.hasErrors()) {
            model.addAttribute("carList",carList);
            model.addAttribute("scheduleList",scheduleList);
            model.addAttribute("priceListList",priceListList);
            model.addAttribute("createTrip",createTrip);
            model.addAttribute("bookings",bookings);
            return "admin/updateTrip";
        }

        log.info("tripId: " + tripId);

        String my_error = null;
        try {
            var tripUpdate = tripSevice.updateTrip(createTrip, tripId);
            createTrip = this.fromTrip(trip);
        } catch (Exception e) {
            my_error = e.getMessage();
        }

        my_error = my_error != null ? my_error : "Cập nhật thành công";

        model.addAttribute("carList",carList);
        model.addAttribute("scheduleList",scheduleList);
        model.addAttribute("priceListList",priceListList);
        model.addAttribute("createTrip",createTrip);
        model.addAttribute("bookings",bookings);
        model.addAttribute("trip",trip);
        model.addAttribute("my_error", my_error);
        return "admin/updateTrip";
    }

    @GetMapping(value = "/trip/delete/{tripId}")
    public String deleteTrip(@PathVariable("tripId") int tripId, Model model) {
        try{
            tripSevice.deleteTrip(tripId);
        }catch (Exception e){
            log.error("Error deleting trip: " + e.getMessage());
            model.addAttribute("error", "Error deleting trip: " + e.getMessage());
            return "admin/trip";
        }

        return "redirect:/admin/trip";
    }


    // endpoint cho việc lấy danh sach những booking đẵ đặt vé cuủa chuyến xe tripId
    @GetMapping(value = "/tripdetail")
    public String payment(Model model,
                          @RequestParam (value = "tripId",required = false ) String tripId
    ) {
        Trip trip = tripSevice.findById(Integer.parseInt(tripId));
        List<PaymentResponse> payments = paymentService.getAllPayments(tripId);
        model.addAttribute("Payments",payments);
        model.addAttribute("trip",trip);
        return "admin/payment";
    }

    @InitBinder
    public void initBinder(WebDataBinder data){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        data.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    @PostMapping(value = "/payment")
    public String payment(@RequestParam("paymentId") String bookingId,
                          @RequestParam("action") String action,
                          @RequestParam(name = "note", required = false) String note,
                          Model model) throws IOException, WriterException {
        Booking booking = bookingSevice.getBookingById(Integer.parseInt(bookingId));
        log.info("bookingId: " + booking.getId());
        Payment payment = booking.getPayment();

        log.info("paymentId: " + payment.getId());
                String note1 = note == null ? "" : note;
        if ("confirm".equals(action)) {
            log.info("note1: confirm ");
            paymentService.updateStatus(payment, PaymentStatus.PAID,note);
            bookingSevice.updateStatus(booking, BookingStatus.PAID);
        } else if ("cancel".equals(action)) {
            log.info("note1: cancel ");
            paymentService.updateStatus(payment, PaymentStatus.CANCELLED, note);
            bookingSevice.updateStatus(booking, BookingStatus.CANCELLED);
        } else {
            return "redirect:/error";
        }
        return "redirect:/admin/tripdetail?tripId=" + booking.getTrip().getId();
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

    @GetMapping(value = "/statistics")
    public String statistics(){
        return "admin/statistics";
    }


    @GetMapping(value = "/schedule")
    public String schedule(){
        return "admin/list-schedule";
    }


    public CreateTrip fromTrip(Trip trip) {
        CreateTrip createTrip = new CreateTrip();
        createTrip.setName(trip.getName());
        createTrip.setCarId(trip.getCar().getId());
        createTrip.setEndDate(trip.getEndDate());
        createTrip.setStartDate(trip.getStartDate());
        createTrip.setPriceListId(trip.getPriceList().getId());
        createTrip.setScheduleId(trip.getSchedule().getId());
        return createTrip;
    }




    // revenue
    @GetMapping("/dashboard")
    public String showDashboard(
            @Valid @ModelAttribute RevenueRequest request,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            // Thu thập tất cả thông báo lỗi
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errors", errors);
            return setupModel(model,request);
        }

        try {
            LocalDate startDate;
            LocalDate endDate;
            if (request.getPeriodType() == null || request.getPeriodType().isEmpty()) {
                model.addAttribute("error", "Vui lòng chọn loại khoảng thời gian");
                return setupModel(model,request);
            }

            switch (request.getPeriodType()) {
                case "WEEK":
                    startDate = LocalDate.now().minusWeeks(1);
                    endDate = LocalDate.now();
                    break;
                case "MONTH":
                    startDate = LocalDate.now().minusMonths(1);
                    endDate = LocalDate.now();
                    break;
                case "YEAR":
                    startDate = LocalDate.now().minusYears(1);
                    endDate = LocalDate.now();
                    break;
                case "SPECIFIC_MONTH":
                    if (request.getSpecificMonth() == null || request.getSpecificYear() == null) {
                        model.addAttribute("error", "Vui lòng chọn đủ tháng và năm");
                        return setupModel(model,request);
                    }
                    startDate = LocalDate.of(request.getSpecificYear(), request.getSpecificMonth(), 1);
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    break;
                case "CUSTOM":
                    if (request.getCustomStartDate() == null || request.getCustomEndDate() == null) {
                        model.addAttribute("error", "Vui lòng nhập đủ ngày bắt đầu và kết thúc");
                        return setupModel(model,request);
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    startDate = LocalDate.parse(request.getCustomStartDate(), formatter);
                    endDate = LocalDate.parse(request.getCustomEndDate(), formatter);
                    break;
                default:
                    startDate = LocalDate.now().minusDays(7);
                    endDate = LocalDate.now();
            }

            List<RevenueStat> stats = revenueService.getFilteredStats(
                    startDate,
                    endDate,
                    request.getSelectedRoute()
            );

            model.addAttribute("stats", stats);
            model.addAttribute("summary", revenueService.getRevenueSummary());

        } catch (Exception e) {
            log.info("Lỗi định dạng ngày: " + e.getMessage());
            model.addAttribute("error", "Lỗi định dạng ngày: " + e.getMessage());
        }

        return setupModel(model,request);
    }

    private String setupModel(Model model,RevenueRequest request) {
        model.addAttribute("request", request);
        model.addAttribute("routes", revenueService.getAllRoutes());
        return "admin/revenue";
    }
}

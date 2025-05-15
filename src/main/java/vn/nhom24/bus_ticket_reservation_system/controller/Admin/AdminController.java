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
import vn.nhom24.bus_ticket_reservation_system.service.*;
import vn.nhom24.bus_ticket_reservation_system.service.ipml.UserSeviceIpml;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    ScheduleService scheduleService;
    CarService carService;
    BookingSevice bookingSevice;
    TripSevice tripSevice;
    PriceListRepository priceListRepository;
    RouteRepository routeRepository;
    ScheduleRopository scheduleRopository;
    CarRepository carRepository;
    PaymentService paymentService;
    RevenueService revenueService;
    private final TripRepository tripRepository;
    private final UserSevice userSevice;
    private final RoleRepository roleRepository;
    private final UserSeviceIpml userSeviceIpml;


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
                case "QUARTER": // <-- Thêm case này
                    // Kiểm tra xem quý và năm đã được chọn chưa
                    if (request.getSpecificQuarter() == null || request.getSpecificYear() == null) {
                        model.addAttribute("error", "Vui lòng chọn đủ quý và năm");
                        return setupModel(model, request);
                    }

                    int specificQuarter = request.getSpecificQuarter();
                    int specificYear = request.getSpecificYear();
                    int startMonth;
                    int endMonth;

                    // Xác định tháng bắt đầu và kết thúc của quý
                    switch (specificQuarter) {
                        case 1: // Quý 1: Tháng 1, 2, 3
                            startMonth = 1;
                            endMonth = 3;
                            break;
                        case 2: // Quý 2: Tháng 4, 5, 6
                            startMonth = 4;
                            endMonth = 6;
                            break;
                        case 3: // Quý 3: Tháng 7, 8, 9
                            startMonth = 7;
                            endMonth = 9;
                            break;
                        case 4: // Quý 4: Tháng 10, 11, 12
                            startMonth = 10;
                            endMonth = 12;
                            break;
                        default:
                            // Xử lý trường hợp quý không hợp lệ
                            model.addAttribute("error", "Quý không hợp lệ (phải từ 1 đến 4)");
                            return setupModel(model, request);
                    }

                    // Tính ngày bắt đầu (ngày 1 của tháng bắt đầu)
                    startDate = LocalDate.of(specificYear, startMonth, 1);
                    // Tính ngày kết thúc (ngày cuối cùng của tháng kết thúc)
                    endDate = LocalDate.of(specificYear, endMonth, 1)
                            .with(TemporalAdjusters.lastDayOfMonth()); // Sử dụng TemporalAdjusters

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



    // manager user
    @GetMapping(value = "/users")
    public String users(Model model,
                        @RequestParam(name = "page",defaultValue = "1") int pageNo
    ){
        Page<User> users = userSevice.getAll(pageNo);

        if (users != null) {
            int totalPages = users.getTotalPages();
            int range = 1;
            // Tính toán trang bắt đầu và kết thúc
            int startPage = Math.max(1, pageNo - range);
            int endPage = Math.min(totalPages, pageNo + range);


            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        model.addAttribute("users",users);
        return "admin/users";
    }


    @GetMapping(value = "/users/showRegisterForm")
    public String showRegisterForm(@RequestParam(value = "error", required = false) String error,Model model){
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("registerUser",new AdminRegisterUser());
        model.addAttribute("roles",roles);
        return "admin/register-user";
    }



    @PostMapping(value = "/users/process")
    public String process(@Valid @ModelAttribute("registerUser") AdminRegisterUser registerUser,
                          BindingResult result,
                          Model model
    ){
        // form validation
        if(result.hasErrors()){
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles",roles);
            return "admin/register-user";
        }

        // kiểm tra xem số email đã tồn tại hay chưa
        User userExisting = userSevice.findByEmail(registerUser.getEmail());

        if(userExisting != null){
            model.addAttribute("my_error", "Email đã được đăng ký");
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles",roles);
            return "admin/register-user";
        }

        userSeviceIpml.save(registerUser);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles",roles);
        model.addAttribute("my_error", "đăng ký tài khoản không thành công");
        return "admin/register-user";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable int id, Model model) {
        User user = userSevice.findById(id);

        // Kiểm tra nếu không tìm thấy người dùng
        if (user == null) {
            // Xử lý trường hợp không tìm thấy người dùng, ví dụ: chuyển hướng về danh sách
            return "redirect:/admin/users?error=UserNotFound"; // Hoặc thêm thông báo lỗi vào flash attributes
        }

        // Tạo một instance của AdminUpdateUserDto (DTO mới dùng cho update)
        AdminUpdateUserDto dto = new AdminUpdateUserDto();

        // Ánh xạ dữ liệu từ entity User sang DTO
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRoles().iterator().next().getName());


        List<Role> roles = roleRepository.findAll();
        model.addAttribute("adminUpdateUserDto", dto);
        model.addAttribute("roles", roles);
        return "admin/edit-user";
    }


    @PostMapping("/users/update")
    public String updateUser(@Valid @ModelAttribute("adminUpdateUserDto") AdminUpdateUserDto adminUpdateUserDto, // <-- Sử dụng DTO mới
                             BindingResult result,
                             Model model) {

        // Luôn thêm roles vào model
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);


        if (result.hasErrors()) {
            model.addAttribute("registerUser", adminUpdateUserDto); // <-- Thêm lại DTO vào model
            return "admin/edit-user"; // Trả về trang sửa với lỗi validation
        }

        User existingUser = userSevice.findByEmail(adminUpdateUserDto.getEmail());
        boolean isSameUser =  existingUser.getId() == adminUpdateUserDto.getId();
        if (existingUser != null && !isSameUser) {
            model.addAttribute("my_error", "Email đã được đăng ký cho người dùng khác");
            model.addAttribute("registerUser", adminUpdateUserDto);
            log.info("Email đã được đăng ký cho người dùng khác");
            return "admin/edit-user";
        }


        // Cập nhật - Cần phương thức update trong service chấp nhận AdminUpdateUserDto
        // Bạn có thể cần ánh xạ từ adminUpdateUserDto sang entity User
        userSevice.updateFromDto(adminUpdateUserDto); // Tên phương thức mới trong service

        // Nếu OK thì quay về danh sách
        return "redirect:/admin/users";
    }

    @GetMapping("/users/activate/{id}")
    public String setActiveUser(@PathVariable int id) {

        userSevice.updateUserActiveStatus(id, true);

        return "redirect:/admin/users";
    }

    @GetMapping("/users/deactivate/{id}")
    public String deactivateUser(@PathVariable int id) {

        userSevice.updateUserActiveStatus(id, false);

        return "redirect:/admin/users";
    }


    // quản lý xe
    @GetMapping("/cars")
    public String listCars(Model model) {
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);
        return "admin/car-list"; // trỏ tới car-list.html
    }

    @GetMapping("/schedules") // Endpoint cho danh sách lịch trình
    public String listSchedules(Model model) {
        // Lấy danh sách tất cả lịch trình từ service (hoặc repository)
        List<ScheduleDisplayDto> scheduleDtos = scheduleService.findAllScheduleDisplayDtos();
        log.info("scheduleDtos size: " + scheduleDtos.size());

        // Thêm danh sách lịch trình vào model với tên "schedules"
        model.addAttribute("schedules", scheduleDtos);

        // Trả về tên view template (tạo file này ở bước 4)
        return "admin/schedule-list";
    }


    @GetMapping("/schedules/details/{id}")
    public String viewScheduleDetails(@PathVariable("id") int scheduleId, Model model) {
        Optional<ScheduleDetailViewDto> scheduleDetailOpt = scheduleService.findScheduleDetailViewById(scheduleId);

        if (scheduleDetailOpt.isPresent()) {
            model.addAttribute("schedule", scheduleDetailOpt.get());
            return "admin/schedule-details"; // Tên view template mới
        } else {
            // Xử lý trường hợp không tìm thấy lịch trình
            model.addAttribute("errorMessage", "Không tìm thấy lịch trình với ID: " + scheduleId);
            return "redirect:/admin/schedules"; // Chuyển hướng về trang danh sách
        }
    }



}

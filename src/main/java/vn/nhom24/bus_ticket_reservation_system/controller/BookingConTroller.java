package vn.nhom24.bus_ticket_reservation_system.controller;

import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.nhom24.bus_ticket_reservation_system.DTO.BookingDTO;
import vn.nhom24.bus_ticket_reservation_system.entity.Booking;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;
import vn.nhom24.bus_ticket_reservation_system.enums.EmailType;
import vn.nhom24.bus_ticket_reservation_system.service.*;
import vn.payos.PayOS;
import vn.payos.type.PaymentLinkData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BookingConTroller {
    private final VNPaySevice vnPaySevice;
    private final PayosService  payosService;
    private final BookingSevice bookingSevice;
    private final TicketSevice ticketSevice;
    private final TripSevice tripSevice;
    private final EmailSevice emailSevice;
    private final PayOS payOS;

    public BookingConTroller(VNPaySevice vnPaySevice, PayosService payosService, BookingSevice bookingSevice, TicketSevice ticketSevice, TripSevice tripSevice, EmailSevice emailSevice, PayOS payOS) {
        this.vnPaySevice = vnPaySevice;
        this.payosService = payosService;
        this.bookingSevice = bookingSevice;
        this.ticketSevice = ticketSevice;
        this.tripSevice = tripSevice;
        this.emailSevice = emailSevice;
        this.payOS = payOS;
    }

    @PostMapping("/public/bookSeats")
    public String bookSeats(@RequestParam (value = "seat", required = false) String[] litsTicket ,
                            @RequestParam (value ="tripid", required = true) int tripId,
                            @RequestParam (value ="phonenumber", required = true) String phoneNumber,
                            @RequestParam (value ="fullname", required = true) String fullName,
                            @RequestParam (value ="email", required = true) String email,
                            @RequestParam (value ="departureid", required = true) String departureId,
                            @RequestParam (value ="arrivalid", required = true) String arrivalId,
                            @RequestParam (value ="pay", required = true) String pay,
                            @RequestParam ("totalamount") int totalAmount,
                            HttpServletRequest request,
                            HttpServletResponse response
                          ) throws Exception {
        String productName = tripSevice.findById(tripId).getName();
        Booking booking = bookingSevice.saveBooking(tripId,phoneNumber,fullName,email,litsTicket,Integer.valueOf(departureId) ,Integer.valueOf(arrivalId));
        String payUrl = "";
        if(pay.equals("vnpay")){
            payUrl = vnPaySevice.createpayment(totalAmount,productName,booking.getId(),pay,request);
        }else if(pay.equals("payos")){
            payUrl = payosService.checkout(request,productName,totalAmount,booking.getId(),pay);
        }
        return "redirect:" + payUrl;
    }



    @GetMapping("/public/payment-return-vnpay")
    public String showResult(Model model, @RequestParam("vnp_Amount") String amount,
                             @RequestParam("vnp_OrderInfo") String bookingId,
                             @RequestParam("vnp_ResponseCode") String responseCode,
                             final HttpServletRequest request
    ) throws IOException, WriterException {
        if (responseCode.equals("00")){

            Booking booking = bookingSevice.getBookingById(Integer.valueOf(bookingId));
            if(booking != null){
                bookingSevice.updateStatus(booking,BookingStatus.PAID);
            }

            BookingDTO bookingDTO = bookingSevice.findBookingById(booking.getId());


            //gửi email
            Map<String,Object> inforBooking = new HashMap<>();

            inforBooking.put("name",bookingDTO.getCustomerName());
            inforBooking.put("phoneNumber",bookingDTO.getCustomerPhone());
            inforBooking.put("listSeat",bookingDTO.getListSeat());
            inforBooking.put("tripName",bookingDTO.getTripName());
            inforBooking.put("starTime",bookingDTO.getStarTime());
            inforBooking.put("departureDate",bookingDTO.getDepartureDate());
            inforBooking.put("departureTime",bookingDTO.getDepartureTime());
            inforBooking.put("departureLocation",bookingDTO.getDepartureLocation());
            inforBooking.put("arrivalLocation",bookingDTO.getArrivalLocation());
            inforBooking.put("bookingCode",bookingDTO.getBookingCode());

            // gửi mail
            emailSevice.sendHtmlEmail(bookingDTO.getCustomerEmail(), EmailType.TICKET_CONFIRMATION,inforBooking);


            model.addAttribute("name",bookingDTO.getCustomerName());
            model.addAttribute("tripName",bookingDTO.getTripName());
            model.addAttribute("booking",bookingDTO.getBookingCode());
            model.addAttribute("phoneNumber",bookingDTO.getCustomerPhone());
            model.addAttribute("departureDate",bookingDTO.getDepartureDate());
            model.addAttribute("departureTime",bookingDTO.getDepartureTime());
            model.addAttribute("departureLocation",bookingDTO.getDepartureLocation());
            model.addAttribute("arrivalLocation",bookingDTO.getArrivalLocation());
            model.addAttribute("listSeat",bookingDTO.getListSeat());
            return "public/result-payment";
        } else if (responseCode.equals("24")){
            model.addAttribute("error", "Giao dịch đã bị hủy");
            return "public/result-payment";
        }else{
            model.addAttribute("error", "Xảy ra lỗi trong quá trình thanh toán");
            return "public/result-payment";
        }
    }





    @GetMapping("/public/payment-return-payos")
    public String Result(Model model,
                         @RequestParam Map<String, String> params,
                         @RequestParam("bookingId") String bookingId
    ) throws Exception {
        String cancel = params.get("cancel");
        String orderId = params.get("orderCode");
        if (cancel.equals("false")){

            // cập nhật trạng thái booking
            Booking booking = bookingSevice.getBookingById(Integer.valueOf(bookingId));
            if(booking != null){
                bookingSevice.updateStatus(booking,BookingStatus.PAID);
            }

            BookingDTO bookingDTO = bookingSevice.findBookingById(booking.getId());


            //gửi email
            Map<String,Object> inforBooking = new HashMap<>();

            inforBooking.put("name",bookingDTO.getCustomerName());
            inforBooking.put("phoneNumber",bookingDTO.getCustomerPhone());
            inforBooking.put("listSeat",bookingDTO.getListSeat());
            inforBooking.put("tripName",bookingDTO.getTripName());
            inforBooking.put("starTime",bookingDTO.getStarTime());
            inforBooking.put("departureDate",bookingDTO.getDepartureDate());
            inforBooking.put("departureTime",bookingDTO.getDepartureTime());
            inforBooking.put("departureLocation",bookingDTO.getDepartureLocation());
            inforBooking.put("arrivalLocation",bookingDTO.getArrivalLocation());
            inforBooking.put("bookingCode",bookingDTO.getBookingCode());

            // gửi mail
            emailSevice.sendHtmlEmail(bookingDTO.getCustomerEmail(), EmailType.TICKET_CONFIRMATION,inforBooking);


           PaymentLinkData paymentData = payOS.getPaymentLinkInformation(Long.valueOf(orderId));



            model.addAttribute("name",bookingDTO.getCustomerName());
            model.addAttribute("tripName",bookingDTO.getTripName());
            model.addAttribute("booking",bookingDTO.getBookingCode());
            model.addAttribute("phoneNumber",bookingDTO.getCustomerPhone());
            model.addAttribute("departureDate",bookingDTO.getDepartureDate());
            model.addAttribute("departureTime",bookingDTO.getDepartureTime());
            model.addAttribute("departureLocation",bookingDTO.getDepartureLocation());
            model.addAttribute("arrivalLocation",bookingDTO.getArrivalLocation());
            model.addAttribute("listSeat",bookingDTO.getListSeat());
            return "public/result-payment";
        } else {
            model.addAttribute("error", "Giao dịch đã bị hủy");
            return "public/result-payment";
        }
    }


}

package vn.nhom24.bus_ticket_reservation_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripCard;
import vn.nhom24.bus_ticket_reservation_system.entity.Ticket;
import vn.nhom24.bus_ticket_reservation_system.entity.Trip;
import vn.nhom24.bus_ticket_reservation_system.service.TicketSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SeatController {

    @Autowired
    TicketSevice ticketSevice;

    @Autowired
    TripSevice tripSevice;

    @GetMapping(value = "/public/seat")
    public String getSeat(
            @RequestParam ("tripId") int tripId,
            @RequestParam("departureLocation") int departureLocation , // Điểm đón
            @RequestParam("arrivalLocation") int arrivalLocation , // điểm trả
            Model model
            ){
        Trip trip = tripSevice.findById(tripId);


        List<Ticket> listSeatA = ticketSevice.getSeatsByRow("A",trip.getId());
        List<Ticket> listSeatB = ticketSevice.getSeatsByRow("B",trip.getId());
        List<Ticket> listSeatC = ticketSevice.getSeatsByRow("C",trip.getId());
        List<Ticket> listSeatD = ticketSevice.getSeatsByRow("D",trip.getId());

        List<Ticket> listSeatA1 =  new ArrayList<>();
        List<Ticket> listSeatA2 =  new ArrayList<>();
        List<Ticket> listSeatB1 =  new ArrayList<>();
        List<Ticket> listSeatB2 =  new ArrayList<>();
        List<Ticket> listSeatC1 =  new ArrayList<>();
        List<Ticket> listSeatC2 =  new ArrayList<>();
        List<Ticket> listSeatD1 =  new ArrayList<>();
        List<Ticket> listSeatD2 =  new ArrayList<>();


        listSeatA.forEach(seat->{
            String soGhe = seat.getSeatName().substring(1);
            if(Integer.valueOf(soGhe) % 2 == 0){
                listSeatA2.add(seat);
            }else listSeatA1.add(seat);
        });

        listSeatB.forEach(seat->{
            String soGhe = seat.getSeatName().substring(1);
            if(Integer.valueOf(soGhe) % 2 == 0){
                listSeatB2.add(seat);
            }else listSeatB1.add(seat);
        });

        listSeatC.forEach(seat->{
            String soGhe = seat.getSeatName().substring(1);
            if(Integer.valueOf(soGhe) % 2 == 0){
                listSeatC2.add(seat);
            }else listSeatC1.add(seat);
        });

        listSeatD.forEach(seat->{
            String soGhe = seat.getSeatName().substring(1);
            if(Integer.valueOf(soGhe) % 2 == 0){
                listSeatD2.add(seat);
            }else listSeatD1.add(seat);
        });





        TripCard tripCard = tripSevice.getTripCard(trip,departureLocation,arrivalLocation);

        model.addAttribute("listSeatA1",listSeatA1);
        model.addAttribute("listSeatA2",listSeatA2);
        model.addAttribute("listSeatB1",listSeatB1);
        model.addAttribute("listSeatB2",listSeatB2);
        model.addAttribute("listSeatC1",listSeatC1);
        model.addAttribute("listSeatC2",listSeatC2);
        model.addAttribute("listSeatD1",listSeatD1);
        model.addAttribute("listSeatD2",listSeatD2);
        model.addAttribute("tripCard",tripCard);
        //id của 2 diểm dùng
        model.addAttribute("departureLocation",departureLocation);
        model.addAttribute("arrivalLocation",arrivalLocation);

        return "public/seat-booking";
    }


}

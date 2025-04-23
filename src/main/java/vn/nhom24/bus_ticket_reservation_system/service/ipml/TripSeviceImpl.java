package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.CreateTrip;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripAdminDTO;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripCard;
import vn.nhom24.bus_ticket_reservation_system.entity.*;
import vn.nhom24.bus_ticket_reservation_system.enums.BookingStatus;
import vn.nhom24.bus_ticket_reservation_system.enums.SeatStatus;
import vn.nhom24.bus_ticket_reservation_system.enums.TripStatus;
import vn.nhom24.bus_ticket_reservation_system.repository.*;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripSeviceImpl implements TripSevice {
    @Autowired
    TinhtpRepository tinhtpRepository;

    @Autowired
    StopRepository stopRepository;

    @Autowired
    ScheduleRopository scheduleRopository;

    @Autowired
    ScheduleDetailRepository scheduleDetailRepository ;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    BookingReposity bookingReposity;

    @Autowired
    CarRepository carRepository;

    @Autowired
    PriceListRepository priceListRepository;

    @Autowired
    SeatReepository seatReepository;



    @Override
    @Transactional
    public List<TripCard> filterTrips(int fromId, int toId, LocalDate date) {
         // tìm kiếm lịch trinhf phù hợp
        List<Schedule> scheduleId = scheduleRopository.findMatchingSchedules(fromId,toId);
        scheduleId.forEach(schedule ->{
            System.out.println("schedule ID :  "+ schedule.getId() );
        });

        System.out.println("so luong cho troong :   "+ scheduleId.size() );



        // dánh sachs các chuyến xe phù hợp
        List<Trip> trips = new ArrayList<>();

        //  chuyến xe có lịch trình trên và thời gian (ngày : giờ)
        scheduleId.forEach(schedule -> {
            List<Trip> trip = tripRepository.findTripsByScheduleAndDepartureDateTimeGreaterThan(schedule.getId(),date);
            trips.addAll(trip);
        });


        trips.forEach(trip -> {
            System.out.println("trip name :  "+trip.getName());
        });

        // tìm kiếm các chuyến xe có lịch trình trên và thời gian (ngày giờ)
        // dữ liệu tra về controller
        List<TripCard> tripCards = new ArrayList<>();


        if(trips == null){
            return null;
        }else{

            // set từng dũ liệu từ trip vào tripcard
            trips.forEach(trip ->{
                TripCard tripCard = getTripCard(trip,fromId,toId);
                if(tripCard.getEmptySeats() == 0){
                    tripCard = null;
                }
                tripCards.add(tripCard);
            });
        }

        return tripCards;
    }


    // convert dữ liệu vào tripCard
    @Transactional
    public TripCard getTripCard(Trip trip,int fromId, int toId){
        TripCard tripCard = new TripCard();
        // Id
        tripCard.setTripId(trip.getId());
        System.out.println(" "+trip.getName());

        // name
        tripCard.setTripNamel(getNameTripcard(trip.getSchedule().getId()));

        // price
        tripCard.setPrice(trip.getPriceList().getPrice());

        // start time
        tripCard.setStarTime(scheduleDetailRepository.getScheduleDetailStart(trip.getSchedule().getId()).getTime());

        // lịch trình chi tiết của diểm bắt đầu mà người dùng chọn
        ScheduleDetail sdStart = scheduleDetailRepository.findScheduleDetailByScheduleAndStop(trip.getSchedule().getId(),fromId);
        // lịch trình chi tiết của điểm đến mà người dùng chọn
        ScheduleDetail sdEnd = scheduleDetailRepository.findScheduleDetailByScheduleAndStop(trip.getSchedule().getId(),toId);

        // emptySeats; // sô ghế còn trống
        tripCard.setEmptySeats(getNumberOfEmptySeats(trip.getId()));

        // DepartureDate // ngày khởi hành
        tripCard.setDepartureDate(trip.getStartDate());

        // departureTime
        tripCard.setDepartureTime(sdStart.getTime());
        // arrivalTime
        tripCard.setArrivalTime(sdEnd.getTime());
        // departureLocation
        tripCard.setDepartureLocation(sdStart.getStop().getStopName());
        // arrivalLocation
        tripCard.setArrivalLocation(sdEnd.getStop().getStopName());
        // vehicleCategory

        tripCard.setVehicleCategory(trip.getCar().getCategory().getName());

        System.out.println(tripCard.toString());

        System.out.println(tripCard.getEmptySeats());
        return tripCard;
    }

    // lấy số ượng ghế trong của 1 chuyến xe
    public int getNumberOfEmptySeats(int tripId) {
        //  số lượng ghế trống
        int availableSeats = ticketRepository.countSeatEmptyByTripAndStatus(tripId, SeatStatus.EMPTY.getStatus());

        return availableSeats;
    }

    @Override
    public List<TripAdminDTO> getAll() {
        // danh sách tất cả chuyến đi
        List<Trip> trips = tripRepository.findAll();

        return tripAdminDTOConver(trips);
    }

    @Override
    public Page<TripAdminDTO> getAll(int pageNo) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);

        List<TripAdminDTO>  list = tripAdminDTOConver(tripRepository.findAll());

        int total = list.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);

        List<TripAdminDTO> subList = list.subList(start, end);
        return new PageImpl<>(subList, pageable, total);
    }

    @Override
    public List<TripAdminDTO> searchByDate(LocalDate date) {
        List<Trip> trips = tripRepository.findByStartDate(date);

        return tripAdminDTOConver(trips);
    }

    @Override
    public List<TripAdminDTO> searchByRoute(int route) {
        List<Trip> trips = tripRepository.findByRoute(route);

        return tripAdminDTOConver(trips);
    }

    @Override
    public List<TripAdminDTO> searchByDateAndRoute(LocalDate date, int route) {
        List<Trip> trips = tripRepository.findByStartDateAndRoute(date,route);

        return tripAdminDTOConver(trips);
    }


    public List<TripAdminDTO> tripAdminDTOConver(List<Trip> trips) {
        List<TripAdminDTO> tripCardsList = new ArrayList<>();

        // danh sách id chuyểns đi
        List<Integer> tripIds = trips.stream()
                .map(Trip::getId)
                .collect(Collectors.toList());

        // danh sách id schedule
        List<Integer> scheduleIds = trips.stream()
                .map(trip -> trip.getSchedule().getId())
                .distinct()
                .collect(Collectors.toList());
        // danh sách các booking có id thuộc danh sách trên
        List<ScheduleDetail> scheduleDetails = scheduleDetailRepository.findByScheduleIdIn(scheduleIds);

        // danh sách các booking có id thuộc danh sách trên
        List<Booking> bookings = bookingReposity.findByTripIdIn(tripIds);


        trips.forEach(trip ->{
            TripAdminDTO tripAdmin = new TripAdminDTO();

            // sô ghế đã đặt
            int reservedSeats = bookings.stream()
                    .filter(booking -> booking.getTrip().getId() == trip.getId() &&
                            booking.getStatus() == BookingStatus.BOOKED )
                    .mapToInt(booking -> booking.getTickets().size())
                    .sum();
            // sô ghế đã đặt
            int purchasedSeats = bookings.stream()
                    .filter(booking -> booking.getTrip().getId() == trip.getId() &&
                            booking.getStatus() == BookingStatus.PAID )
                    .mapToInt(booking -> booking.getTickets().size())
                    .sum();
            // ghé trống
            int emptySeats = trip.getTickets().size() - reservedSeats - purchasedSeats;

            // điểm xuất phát của chuyến đi
            ScheduleDetail departure = scheduleDetails.stream()
                    .filter(detail -> detail.getSchedule().getId() == trip.getSchedule().getId())
                    .min(Comparator.comparing(ScheduleDetail::getStopNumber))
                    .get();

            // điểm đến của chuyến đi
            ScheduleDetail arrival = scheduleDetails.stream()
                    .filter(detail -> detail.getSchedule().getId() == trip.getSchedule().getId())
                    .max(Comparator.comparing(ScheduleDetail::getStopNumber))
                    .get();

            tripAdmin.setTripNamel(trip.getName());
            tripAdmin.setTripId(trip.getId());
            tripAdmin.setRouteNamel(trip.getSchedule().getRoute().getName());
            tripAdmin.setDepartureLocation(departure.getStop().getStopName());
            tripAdmin.setArrivalLocation(arrival.getStop().getStopName());
            tripAdmin.setDepartureTime(trip.getStartDate().atTime(departure.getTime()));
            tripAdmin.setArrivalTime(trip.getEndDate().atTime(arrival.getTime()));
            tripAdmin.setPrice(trip.getPriceList().getPrice());
            tripAdmin.setVehicle(trip.getCar().getId());
            tripAdmin.setPurchasedSeats(purchasedSeats);
            tripAdmin.setReservedSeats(reservedSeats);
            tripAdmin.setEmptySeats(emptySeats);


            tripCardsList.add(tripAdmin);
        });
        return tripCardsList;
    }

    @Override
    public boolean addtrip(CreateTrip createTrip) {
        Optional<Car> car = carRepository.findById(createTrip.getCarId());
        if(!car.isPresent()){
            return false;
        }
        Optional<Schedule> schedule = scheduleRopository.findById(createTrip.getScheduleId());
        if(!schedule.isPresent()){
            return false;
        }
        Optional<PriceList> priceList = priceListRepository.findById(createTrip.getPriceListId());
        if(!priceList.isPresent()){
            return false;
        }
        Trip trip = new Trip();
        trip.setName(createTrip.getName());
        trip.setNote(createTrip.getNote());
        trip.setStartDate(createTrip.getStartDate());
        trip.setEndDate(createTrip.getEndDate());
        trip.setCar(car.get());
        trip.setSchedule(schedule.get());
        trip.setPriceList(priceList.get());
        trip.setStatus(TripStatus.BOOKED);

        tripRepository.save(trip);

        List<Ticket> ticketList = new ArrayList<>();
        List<Seat> seats = car.get().getSeats();
        seats.forEach(seat ->{
            Ticket ticket = Ticket.fromSeat(seat,trip);
            ticketList.add(ticket);
        });

        ticketRepository.saveAll(ticketList);


        return true;
    }

    @Override
    public List<Tinhtp> findAll() {
        return tinhtpRepository.findAll();
    }


    @Override
    public void setTripNameBySchedule(int tripId) {
        Trip trip = tripRepository.findById(tripId).get();

        String h1 =stopRepository.findFirstStopNameByScheduleIdMax(trip.getSchedule().getId());
        String h2 =stopRepository.findFirstStopNameByScheduleIdMin(trip.getSchedule().getId());

        trip.setName(h1+ " - "+h2);

        tripRepository.save(trip);
    }

    @Override
    public String getNameTripcard(int scheduleId) {
        Tinhtp tinhtpStart = tinhtpRepository.findTinhtpByScheduleIdWithMinStopNumber(scheduleId);
        Tinhtp tinhtpEnd = tinhtpRepository.findTinhtpByScheduleIdWithMaxStopNumber(scheduleId);

        return tinhtpStart.getName()+ " - "+tinhtpEnd.getName();
    }

    @Override
    public Trip findById(int tripId) {
        return tripRepository.findById(tripId).get();
    }


}

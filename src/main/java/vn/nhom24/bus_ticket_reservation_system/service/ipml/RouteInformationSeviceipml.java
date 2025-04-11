package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.RouteInformation;
import vn.nhom24.bus_ticket_reservation_system.entity.*;
import vn.nhom24.bus_ticket_reservation_system.repository.*;
import vn.nhom24.bus_ticket_reservation_system.service.RouteInformationSevice;

import java.util.List;

@Service
public class RouteInformationSeviceipml implements RouteInformationSevice {

    private RouteRepository routeRepository;
    private ScheduleRopository scheduleRopository;
    private ScheduleDetailRepository scheduleDetailRepository;
    private TinhtpRepository tinhtpRepository;
    private PriceListRepository priceListRepository;

    @Autowired
    public RouteInformationSeviceipml(RouteRepository routeRepository, ScheduleRopository scheduleRopository, ScheduleDetailRepository scheduleDetailRepository, TinhtpRepository tinhtpRepository, PriceListRepository priceListRepository) {
        this.routeRepository = routeRepository;
        this.scheduleRopository = scheduleRopository;
        this.scheduleDetailRepository = scheduleDetailRepository;
        this.tinhtpRepository = tinhtpRepository;
        this.priceListRepository = priceListRepository;
    }

    @Override
    public RouteInformation getRouteInformation(int routeId) {
        RouteInformation routeInformation = new RouteInformation();

        Route route = routeRepository.findById(routeId).get();
        if (route == null) {
            return null;
        }

        List<Schedule> schedules = scheduleRopository.findByRoute(route);


        int departureScheduleId =-1;
        int returnScheduleId =-1;

        // LẤY schedule xuất phát từ đà nẵng
        for (Schedule schedule : schedules) {
            if(schedule.getScheduleName().startsWith("Đà Nẵng")){
                // lịch trình xuất phát từ đà nẵng
                departureScheduleId = schedule.getId();
            }else{
                returnScheduleId = schedule.getId();
                // lịch trình xuất phát theo chiều ngược lại
            }
        }

        if (departureScheduleId == -1 || returnScheduleId == -1) {
            return null;
        }

        // tỉnh xuất phát
        Tinhtp start = tinhtpRepository.findTinhtpByScheduleIdWithMinStopNumber(departureScheduleId);

        // tỉnh Điểm đến
        Tinhtp end = tinhtpRepository.findTinhtpByScheduleIdWithMaxStopNumber(departureScheduleId);

        if (start == null || end == null) {
            return null;
        }


        // Tìm thời gian đi và đến để biết lịch trình di
        List<ScheduleDetail> scheduleDetailsDepartureTime = scheduleDetailRepository.findScheduleDetailsByScheduleAndTinhtp(departureScheduleId,start.getId());
        List<ScheduleDetail> scheduleDetailsArrivalTime = scheduleDetailRepository.findScheduleDetailsByScheduleAndTinhtp(departureScheduleId,end.getId());

        // Tìm thời gian đi và đến để biết lịch trình về
        List<ScheduleDetail> scheduleDetailsDepartureTime1 = scheduleDetailRepository.findScheduleDetailsByScheduleAndTinhtp(returnScheduleId,end.getId());
        List<ScheduleDetail> scheduleDetailsArrivalTime1 = scheduleDetailRepository.findScheduleDetailsByScheduleAndTinhtp(returnScheduleId,start.getId());


        String  departureTime = generateTimeRangeString(scheduleDetailsDepartureTime);
        String arrivalTime = generateTimeRangeString(scheduleDetailsArrivalTime,end);
        String departureTime1 = generateTimeRangeString(scheduleDetailsDepartureTime1);
        String arrivalTime1 = generateTimeRangeString(scheduleDetailsArrivalTime1,end);



       List<PriceList> priceLists = priceListRepository.findByRoute(route);



        routeInformation.setDepartureLocation(start.getName());
        routeInformation.setArrivalLocation(end.getName());
        routeInformation.setDepartureTime(departureTime);
        routeInformation.setArrivalTime(arrivalTime);
        routeInformation.setDepartureTime1(departureTime1);
        routeInformation.setArrivalTime1(arrivalTime1);
        routeInformation.setPriceLists(priceLists);


        return routeInformation;
    }

    private String generateTimeRangeString(List<ScheduleDetail> scheduleDetails) {
        if (scheduleDetails.isEmpty()) {
            return "";
        }
        return scheduleDetails.get(0).getTime() + " - " + scheduleDetails.get(scheduleDetails.size() - 1).getTime();
    }

    private String generateTimeRangeString(List<ScheduleDetail> scheduleDetails, Tinhtp location) {
        if (scheduleDetails.isEmpty()) {
            return "";
        }
        if (scheduleDetails.size() > 2) {
            return scheduleDetails.get(0).getTime() + " - " + scheduleDetails.get(scheduleDetails.size() - 1).getTime() + " ( " + location.getName() + " )";
        } else {
            return scheduleDetails.get(0).getTime() + " - " + scheduleDetails.get(scheduleDetails.size() - 1).getTime() + " ( " + scheduleDetails.get(scheduleDetails.size() - 1).getStop().getStopName() + " )";
        }
    }


}

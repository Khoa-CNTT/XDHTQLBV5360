package vn.nhom24.bus_ticket_reservation_system.DTO;

import vn.nhom24.bus_ticket_reservation_system.entity.PriceList;

import java.util.List;

public class RouteInformation {
    private String departureLocation;
    private String arrivalLocation;
    private String departureTime;
    private String arrivalTime;
    private String departureTime1;
    private String arrivalTime1;
    private List<PriceList> priceLists;

    public RouteInformation() {
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime1() {
        return departureTime1;
    }

    public void setDepartureTime1(String departureTime1) {
        this.departureTime1 = departureTime1;
    }

    public String getArrivalTime1() {
        return arrivalTime1;
    }

    public void setArrivalTime1(String arrivalTime1) {
        this.arrivalTime1 = arrivalTime1;
    }

    public List<PriceList> getPriceLists() {
        return priceLists;
    }

    public void setPriceLists(List<PriceList> priceLists) {
        this.priceLists = priceLists;
    }
}

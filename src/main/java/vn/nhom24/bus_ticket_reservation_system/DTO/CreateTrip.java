package vn.nhom24.bus_ticket_reservation_system.DTO;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateTrip {

    @NotNull(message = "không được để trống.")
    private LocalDate startDate;

    @NotNull(message = "không được để trống.")
    private LocalDate endDate;

    @NotNull(message = "không được để trống.")
    private String name;

    private String note;

    @NotNull
    @Min(value = 1, message = "không được để trống.")
    private int scheduleId;

    @NotNull(message = "không được để trống.")
    private String carId;

    @NotNull(message = "không được để trống.")
    private int priceListId;

    public CreateTrip() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public int getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(int priceListId) {
        this.priceListId = priceListId;
    }
}

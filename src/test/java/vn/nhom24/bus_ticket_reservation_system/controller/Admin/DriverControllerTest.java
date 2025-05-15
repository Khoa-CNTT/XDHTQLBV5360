package vn.nhom24.bus_ticket_reservation_system.controller.Admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.nhom24.bus_ticket_reservation_system.DTO.BookingDTO;
import vn.nhom24.bus_ticket_reservation_system.DTO.CheckInRequest;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripAdminDTO;
import vn.nhom24.bus_ticket_reservation_system.DTO.resonse.ApiResponse;
import vn.nhom24.bus_ticket_reservation_system.service.BookingSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DriverControllerTest {

    @Mock
    private TripSevice tripSevice;

    @Mock
    private BookingSevice bookingSevice;

    @InjectMocks
    private DriverController driverController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(driverController).build();
    }

    @Test
    void driver_ShouldReturnAdminHomePage() throws Exception {
        mockMvc.perform(get("/driver"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/adminhomepage"));
    }

    @Test
    void checkIn_ShouldReturnBusView() throws Exception {
        List<TripAdminDTO> trips = Arrays.asList(new TripAdminDTO());
        when(tripSevice.searchByDate(any(LocalDate.class))).thenReturn(trips);

        mockMvc.perform(get("/driver/bus"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/bus"))
                .andExpect(model().attribute("trips", trips));
    }

    @Test
    void showCheckinPage_ShouldReturnCheckinView() throws Exception {
        String tripCode = "TRIP123";
        mockMvc.perform(get("/driver/checkin/" + tripCode))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/checkin"))
                .andExpect(model().attribute("currentTripCode", tripCode));
    }

    @Test
    void checkInTicket_WithValidData_ShouldReturnSuccess() throws Exception {
        String tripCode = "TRIP123";
        CheckInRequest request = new CheckInRequest();
        request.setIdBooking("BOOKING123");

        BookingDTO bookingDTO = BookingDTO.builder()
                .bookingCode("BOOKING123")
                .build();

        when(bookingSevice.checkInTicket(anyString(), anyString())).thenReturn(bookingDTO);

        mockMvc.perform(post("/driver/api/checkin/" + tripCode)
                .contentType("application/json")
                .content("{\"idBooking\":\"BOOKING123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Check in successfully"))
                .andExpect(jsonPath("$.result.bookingCode").value("BOOKING123"));
    }
} 
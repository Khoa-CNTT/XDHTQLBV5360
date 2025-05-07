package vn.nhom24.bus_ticket_reservation_system.controller.Admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.nhom24.bus_ticket_reservation_system.DTO.CreateTrip;
import vn.nhom24.bus_ticket_reservation_system.DTO.RevenueRequest;
import vn.nhom24.bus_ticket_reservation_system.DTO.RevenueSummary;
import vn.nhom24.bus_ticket_reservation_system.DTO.TripAdminDTO;
import vn.nhom24.bus_ticket_reservation_system.entity.*;
import vn.nhom24.bus_ticket_reservation_system.repository.*;
import vn.nhom24.bus_ticket_reservation_system.service.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest {

    @Mock
    private TripSevice tripSevice;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private ScheduleRopository scheduleRopository;

    @Mock
    private PriceListRepository priceListRepository;

    @Mock
    private BookingSevice bookingSevice;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RevenueService revenueService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void showDashboard_WithValidPeriod_ShouldReturnDashboard() throws Exception {
        RevenueSummary revenueSummary = new RevenueSummary();
        when(revenueService.getRevenueSummary()).thenReturn(revenueSummary);

        mockMvc.perform(get("/admin/dashboard")
                .param("periodType", "WEEK"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/revenue"))
                .andExpect(model().attributeExists("revenueSummary"));

        verify(revenueService).getRevenueSummary();
    }

    @Test
    void showDashboard_WithInvalidPeriod_ShouldReturnDashboard() throws Exception {
        RevenueSummary revenueSummary = new RevenueSummary();
        when(revenueService.getRevenueSummary()).thenReturn(revenueSummary);

        mockMvc.perform(get("/admin/dashboard")
                .param("periodType", "INVALID"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/revenue"))
                .andExpect(model().attributeExists("revenueSummary"));

        verify(revenueService).getRevenueSummary();
    }

    @Test
    void trip_WithValidParameters_ShouldReturnTripList() throws Exception {
        Route mockRoute = new Route();
        mockRoute.setId(1);
        List<Route> routes = Arrays.asList(mockRoute);
        List<TripAdminDTO> trips = Arrays.asList(new TripAdminDTO());
        
        when(routeRepository.findAll()).thenReturn(routes);
        when(routeRepository.findById(anyInt())).thenReturn(Optional.of(mockRoute));
        when(tripSevice.searchByDateAndRoute(any(LocalDate.class), anyInt())).thenReturn(trips);

        mockMvc.perform(get("/admin/trip")
                .param("page", "1")
                .param("date", "2024-12-25")
                .param("route", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/trip"))
                .andExpect(model().attributeExists("routes"))
                .andExpect(model().attribute("trips", trips));

        verify(routeRepository).findAll();
        verify(routeRepository).findById(1);
        verify(tripSevice).searchByDateAndRoute(any(LocalDate.class), eq(1));
    }

    @Test
    void trip_WithInvalidRoute_ShouldReturnTripList() throws Exception {
        List<Route> routes = Arrays.asList(new Route());
        when(routeRepository.findAll()).thenReturn(routes);
        when(routeRepository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/trip")
                .param("page", "1")
                .param("date", "2024-12-25")
                .param("route", "999"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/trip"))
                .andExpect(model().attributeExists("routes"));

        verify(routeRepository).findAll();
        verify(routeRepository).findById(999);
        verify(tripSevice, never()).searchByDateAndRoute(any(LocalDate.class), anyInt());
    }

    @Test
    void showCreateTripForm_ShouldReturnCreateTripView() throws Exception {
        List<Car> cars = Arrays.asList(new Car());
        List<Schedule> schedules = Arrays.asList(new Schedule());
        List<PriceList> priceLists = Arrays.asList(new PriceList());

        when(carRepository.findAll()).thenReturn(cars);
        when(scheduleRopository.findAll()).thenReturn(schedules);
        when(priceListRepository.findAll()).thenReturn(priceLists);

        mockMvc.perform(get("/admin/trip/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/createTrip"))
                .andExpect(model().attributeExists("carList", "scheduleList", "priceListList", "createTrip"));

        verify(carRepository).findAll();
        verify(scheduleRopository).findAll();
        verify(priceListRepository).findAll();
    }

    @Test
    void createTrip_WithValidData_ShouldRedirectToTripList() throws Exception {
        CreateTrip createTrip = new CreateTrip();
        when(tripSevice.addtrip(any(CreateTrip.class))).thenReturn(true);

        mockMvc.perform(post("/admin/trip/save")
                .flashAttr("createTrip", createTrip))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/trip"));

        verify(tripSevice).addtrip(any(CreateTrip.class));
    }

    @Test
    void createTrip_WithInvalidData_ShouldReturnToCreateForm() throws Exception {
        CreateTrip createTrip = new CreateTrip();
        when(tripSevice.addtrip(any(CreateTrip.class))).thenReturn(false);

        mockMvc.perform(post("/admin/trip/save")
                .flashAttr("createTrip", createTrip))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/createTrip"))
                .andExpect(model().attributeExists("error"));

        verify(tripSevice).addtrip(any(CreateTrip.class));
    }
} 
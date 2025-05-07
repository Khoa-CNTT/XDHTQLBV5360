package vn.nhom24.bus_ticket_reservation_system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;
import vn.nhom24.bus_ticket_reservation_system.entity.Tinhtp;
import vn.nhom24.bus_ticket_reservation_system.service.RouteSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest {

    @Mock
    private RouteSevice routeSevice;

    @Mock
    private TripSevice tripSevice;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void showHomePage_ShouldReturnClientView() throws Exception {
        // Arrange
        List<Route> routes = Arrays.asList(new Route(), new Route());
        List<Tinhtp> tinhtps = Arrays.asList(new Tinhtp(), new Tinhtp());

        when(routeSevice.findAll()).thenReturn(routes);
        when(tripSevice.findAll()).thenReturn(tinhtps);

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/client"))
                .andExpect(model().attribute("routes", routes))
                .andExpect(model().attribute("listTinhTp", tinhtps));
    }
} 
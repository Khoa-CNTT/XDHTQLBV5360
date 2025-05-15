package vn.nhom24.bus_ticket_reservation_system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.nhom24.bus_ticket_reservation_system.DTO.RouteInformation;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;
import vn.nhom24.bus_ticket_reservation_system.service.RouteSevice;
import vn.nhom24.bus_ticket_reservation_system.service.ipml.RouteInformationSeviceipml;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RouteControllerTest {

    @Mock
    private RouteSevice routeSevice;

    @Mock
    private RouteInformationSeviceipml routeInformationSeviceipml;

    @InjectMocks
    private RouteController routeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(routeController).build();
    }

    @Test
    void route_WithValidRouteId_ShouldReturnRouteView() throws Exception {
        int routeId = 1;
        Route route = new Route();
        RouteInformation routeInformation = new RouteInformation();

        when(routeSevice.findById(routeId)).thenReturn(route);
        when(routeInformationSeviceipml.getRouteInformation(routeId)).thenReturn(routeInformation);

        mockMvc.perform(get("/public/" + routeId))
                .andExpect(status().isOk())
                .andExpect(view().name("public/route"))
                .andExpect(model().attribute("route", route))
                .andExpect(model().attribute("routeInformation", routeInformation));
    }

    @Test
    void route_WithInvalidRouteId_ShouldReturnError() throws Exception {
        int routeId = 999;
        when(routeSevice.findById(routeId)).thenReturn(null);

        mockMvc.perform(get("/public/" + routeId))
                .andExpect(status().isOk())
                .andExpect(view().name("public/route"));
    }
} 
package vn.nhom24.bus_ticket_reservation_system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.nhom24.bus_ticket_reservation_system.entity.Tinhtp;
import vn.nhom24.bus_ticket_reservation_system.service.StopSevice;
import vn.nhom24.bus_ticket_reservation_system.service.TripSevice;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TripControllerTest {

    @Mock
    private TripSevice tripSevice;

    @Mock
    private StopSevice stopSevice;

    @InjectMocks
    private TripController tripController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tripController).build();
    }

    @Test
    void showSearchForm_ShouldReturnSearchScheduleView() throws Exception {
        List<Tinhtp> tinhtps = Arrays.asList(new Tinhtp(), new Tinhtp());
        when(tripSevice.findAll()).thenReturn(tinhtps);

        mockMvc.perform(get("/public/schedule"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/search-schedule"))
                .andExpect(model().attribute("listTinhTp", tinhtps));
    }

    @Test
    void handleSearchForm_WithValidParameters_ShouldReturnSearchResults() throws Exception {
        mockMvc.perform(post("/public/submit")
                .param("departure", "1")
                .param("destination", "2")
                .param("departureDate", "12/25/2024"))
                .andExpect(status().isOk());
        // Add more specific assertions based on your implementation
    }
} 
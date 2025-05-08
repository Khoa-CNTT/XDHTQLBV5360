package vn.nhom24.bus_ticket_reservation_system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ErrorControllerTest {

    private ErrorController errorController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        errorController = new ErrorController();
        mockMvc = MockMvcBuilders.standaloneSetup(errorController).build();
    }

    @Test
    void page403_ShouldReturn403View() throws Exception {
        mockMvc.perform(get("/showPage403"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/403"));
    }

    @Test
    void page404_ShouldReturn404View() throws Exception {
        mockMvc.perform(get("/404"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/404"));
    }

    @Test
    void page401_ShouldReturn401View() throws Exception {
        mockMvc.perform(get("/401"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/401"));
    }

    @Test
    void page500_ShouldReturn500View() throws Exception {
        mockMvc.perform(get("/500"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/500"));
    }
} 
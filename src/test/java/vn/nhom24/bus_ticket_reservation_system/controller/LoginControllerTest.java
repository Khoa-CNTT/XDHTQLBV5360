package vn.nhom24.bus_ticket_reservation_system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoginControllerTest {

    private LoginController loginController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        loginController = new LoginController();
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void showLoginPage_WithoutError_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/login"))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    void showLoginPage_WithError_ShouldReturnLoginViewWithError() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/login"))
                .andExpect(model().attribute("error", "true"));
    }
} 
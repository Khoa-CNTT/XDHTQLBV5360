package vn.nhom24.bus_ticket_reservation_system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.nhom24.bus_ticket_reservation_system.entity.Trip;
import vn.nhom24.bus_ticket_reservation_system.entity.Booking;
import vn.nhom24.bus_ticket_reservation_system.service.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingControllerTest {

    @Mock
    private VNPaySevice vnPaySevice;

    @Mock
    private PayosService payosService;

    @Mock
    private BookingSevice bookingSevice;

    @Mock
    private TicketSevice ticketSevice;

    @Mock
    private TripSevice tripSevice;

    @Mock
    private EmailSevice emailSevice;

    @InjectMocks
    private BookingConTroller bookingController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void bookSeats_WithValidDataAndVNPay_ShouldProcessBooking() throws Exception {
        // Arrange
        Trip mockTrip = new Trip();
        mockTrip.setName("Test Trip");
        Booking mockBooking = new Booking();
        mockBooking.setId(1);

        when(tripSevice.findById(anyInt())).thenReturn(mockTrip);
        when(bookingSevice.saveBooking(anyInt(), anyString(), anyString(), anyString(), any(), anyInt(), anyInt())).thenReturn(mockBooking);
        when(vnPaySevice.createpayment(anyLong(), anyString(), anyInt(), anyString(), any(HttpServletRequest.class))).thenReturn("http://test-payment-url");

        // Act & Assert
        mockMvc.perform(post("/public/bookSeats")
                .param("seat[]", "1", "2")
                .param("tripid", "1")
                .param("phonenumber", "1234567890")
                .param("fullname", "Test User")
                .param("email", "test@example.com")
                .param("departureid", "1")
                .param("arrivalid", "2")
                .param("pay", "vnpay")
                .param("totalamount", "100000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://test-payment-url"));

        // Verify
        verify(tripSevice).findById(eq(1));
        verify(bookingSevice).saveBooking(eq(1), anyString(), anyString(), anyString(), any(), eq(1), eq(2));
        verify(vnPaySevice).createpayment(eq(100000L), eq("Test Trip"), eq(1), eq("vnpay"), any(HttpServletRequest.class));
    }

    @Test
    void bookSeats_WithValidDataAndPayOS_ShouldProcessBooking() throws Exception {
        // Arrange
        Trip mockTrip = new Trip();
        mockTrip.setName("Test Trip");
        Booking mockBooking = new Booking();
        mockBooking.setId(1);

        when(tripSevice.findById(anyInt())).thenReturn(mockTrip);
        when(bookingSevice.saveBooking(anyInt(), anyString(), anyString(), anyString(), any(), anyInt(), anyInt())).thenReturn(mockBooking);
        when(payosService.checkout(any(HttpServletRequest.class), anyString(), anyInt(), anyInt())).thenReturn("http://test-payos-url");

        // Act & Assert
        mockMvc.perform(post("/public/bookSeats")
                .param("seat[]", "1", "2")
                .param("tripid", "1")
                .param("phonenumber", "1234567890")
                .param("fullname", "Test User")
                .param("email", "test@example.com")
                .param("departureid", "1")
                .param("arrivalid", "2")
                .param("pay", "payos")
                .param("totalamount", "100000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://test-payos-url"));

        // Verify
        verify(tripSevice).findById(eq(1));
        verify(bookingSevice).saveBooking(eq(1), anyString(), anyString(), anyString(), any(), eq(1), eq(2));
        verify(payosService).checkout(any(HttpServletRequest.class), eq("Test Trip"), eq(100000), eq(1));
    }

    @Test
    void bookSeats_WithInvalidTrip_ShouldReturnError() throws Exception {
        // Arrange
        when(tripSevice.findById(anyInt())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/public/bookSeats")
                .param("seat[]", "1")
                .param("tripid", "999")
                .param("phonenumber", "1234567890")
                .param("fullname", "Test User")
                .param("email", "test@example.com")
                .param("departureid", "1")
                .param("arrivalid", "2")
                .param("pay", "vnpay")
                .param("totalamount", "100000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/public/error"));

        // Verify
        verify(tripSevice).findById(eq(999));
        verify(bookingSevice, never()).saveBooking(anyInt(), anyString(), anyString(), anyString(), any(), anyInt(), anyInt());
    }

    @Test
    void bookSeats_WithInvalidData_ShouldReturnError() throws Exception {
        // Arrange
        Trip mockTrip = new Trip();
        mockTrip.setName("Test Trip");
        when(tripSevice.findById(anyInt())).thenReturn(mockTrip);
        when(bookingSevice.saveBooking(anyInt(), anyString(), anyString(), anyString(), any(), anyInt(), anyInt())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/public/bookSeats")
                .param("seat[]", "1")
                .param("tripid", "1")
                .param("phonenumber", "1234567890")
                .param("fullname", "Test User")
                .param("email", "invalid-email")
                .param("departureid", "1")
                .param("arrivalid", "2")
                .param("pay", "vnpay")
                .param("totalamount", "100000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/public/error"));

        // Verify
        verify(tripSevice).findById(eq(1));
        verify(bookingSevice).saveBooking(eq(1), anyString(), anyString(), anyString(), any(), eq(1), eq(2));
    }
} 
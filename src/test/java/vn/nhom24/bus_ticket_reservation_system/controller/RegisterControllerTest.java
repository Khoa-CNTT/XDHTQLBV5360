package vn.nhom24.bus_ticket_reservation_system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.nhom24.bus_ticket_reservation_system.DTO.RegisterUser;
import vn.nhom24.bus_ticket_reservation_system.entity.User;
import vn.nhom24.bus_ticket_reservation_system.service.ipml.UserSeviceIpml;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegisterControllerTest {

    @Mock
    private UserSeviceIpml userSeviceIpml;

    @InjectMocks
    private RegisterController registerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
    }

    @Test
    void showRegisterForm_WithError_ShouldReturnRegisterViewWithError() throws Exception {
        mockMvc.perform(get("/register/showRegisterForm")
                .param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/register"))
                .andExpect(model().attributeExists("registerUser"));
    }

    @Test
    void showRegisterForm_WithoutError_ShouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register/showRegisterForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/register"))
                .andExpect(model().attributeExists("registerUser"));
    }

    @Test
    void process_WithValidData_ShouldRedirectToConfirm() throws Exception {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail("test@example.com");
        registerUser.setPassWord("password123");
        registerUser.setFullName("Test User");
        registerUser.setPhoneNumber("1234567890");

        when(userSeviceIpml.findByEmail(any())).thenReturn(null);
        when(userSeviceIpml.save(any(RegisterUser.class))).thenReturn(true);

        mockMvc.perform(post("/register/process")
                .flashAttr("registerUser", registerUser))
                .andExpect(status().isOk())
                .andExpect(view().name("public/confirm"))
                .andExpect(model().attribute("notification", "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản"));
    }

    @Test
    void process_WithExistingEmail_ShouldReturnToRegister() throws Exception {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail("existing@example.com");
        registerUser.setPassWord("password123");
        registerUser.setFullName("Test User");
        registerUser.setPhoneNumber("1234567890");

        User existingUser = new User();
        existingUser.setEmail("existing@example.com");
        when(userSeviceIpml.findByEmail(any())).thenReturn(existingUser);

        mockMvc.perform(post("/register/process")
                .flashAttr("registerUser", registerUser))
                .andExpect(status().isOk())
                .andExpect(view().name("public/register"))
                .andExpect(model().attribute("my_error", "Email đã được đăng ký"));
    }

    @Test
    void process_WithSaveFailure_ShouldReturnToRegister() throws Exception {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail("test@example.com");
        registerUser.setPassWord("password123");
        registerUser.setFullName("Test User");
        registerUser.setPhoneNumber("1234567890");

        when(userSeviceIpml.findByEmail(any())).thenReturn(null);
        when(userSeviceIpml.save(any(RegisterUser.class))).thenReturn(false);

        mockMvc.perform(post("/register/process")
                .flashAttr("registerUser", registerUser))
                .andExpect(status().isOk())
                .andExpect(view().name("public/register"))
                .andExpect(model().attribute("my_error", "đăng ký tài khoản không thành công"));
    }

    @Test
    void verifyAccount_WithValidToken_ShouldShowSuccessMessage() throws Exception {
        when(userSeviceIpml.verifyAccount(any(), any())).thenReturn(true);

        mockMvc.perform(get("/register/verify-account")
                .param("email", "test@example.com")
                .param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/verifyaccount"))
                .andExpect(model().attribute("notification", "XIN CHÚC MỪNG , TÀI KHOẢN CỦA BẠN ĐÃ ĐƯỢC XÁC MINH."));
    }

    @Test
    void verifyAccount_WithInvalidToken_ShouldShowErrorMessage() throws Exception {
        when(userSeviceIpml.verifyAccount(any(), any())).thenReturn(false);

        mockMvc.perform(get("/register/verify-account")
                .param("email", "test@example.com")
                .param("token", "invalid-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/verifyaccount"))
                .andExpect(model().attribute("notification", "RẤT TIẾC, CHÚNG TÔI KHÔNG THỂ XÁC MINH TÀI KHOẢN. VUI LÒNG TẠO LẠI MÃ XÁC THỰC VÀ THỬ LẠI"))
                .andExpect(model().attribute("email", "test@example.com"));
    }

    @Test
    void regenerateOtp_ShouldCallServiceAndReturnConfirmView() throws Exception {
        String email = "test@example.com";
        doNothing().when(userSeviceIpml).regenerateOtp(email);

        mockMvc.perform(get("/register/regenerate-otp")
                .param("email", email))
                .andExpect(status().isOk())
                .andExpect(view().name("public/confirm"));

        verify(userSeviceIpml, times(1)).regenerateOtp(email);
    }
} 
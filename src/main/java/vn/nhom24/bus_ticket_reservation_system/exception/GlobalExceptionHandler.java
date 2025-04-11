package vn.nhom24.bus_ticket_reservation_system.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.nhom24.bus_ticket_reservation_system.DTO.resonse.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public String handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex, RedirectAttributes redirectAttributes) {
        String errorMessage = "Lỗi xác thực: " + ex.getCause().getMessage();
        redirectAttributes.addFlashAttribute("error", errorMessage);
        return "redirect:/login";
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(response);
    }

}
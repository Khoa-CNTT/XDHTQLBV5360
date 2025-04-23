package vn.nhom24.bus_ticket_reservation_system.service;

import vn.nhom24.bus_ticket_reservation_system.enums.EmailType;

import java.util.Map;

public interface EmailSevice {
    void sendSimpleMailMessage(String name, String to, String token);
    void sendMimeMessageWithAttachments(String name, String to, String token);
    void sendMimeMessageWithEmbeddedFiles(String name, String to, String token);
    void sendHtmlEmail(String to, EmailType emailType, Map<String, Object> info);

    void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);
}

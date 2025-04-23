package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    void store(MultipartFile file) throws IOException;
}

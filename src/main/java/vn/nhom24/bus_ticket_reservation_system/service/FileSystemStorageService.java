package vn.nhom24.bus_ticket_reservation_system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystemStorageService implements StorageService{
    // lấy dường dẫn thư mục mà úng dụng đang làm việc
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));



    @Override
    public void store(MultipartFile image) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");

        // kiểm tra đường dẫn đã tồn tại chưa
        if(!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            // tạo nếu fodel chưa tồn tại
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(image.getOriginalFilename());

        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
    }
}

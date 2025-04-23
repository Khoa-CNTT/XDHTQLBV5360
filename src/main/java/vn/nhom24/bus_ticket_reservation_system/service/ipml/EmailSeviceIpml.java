package vn.nhom24.bus_ticket_reservation_system.service.ipml;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vn.nhom24.bus_ticket_reservation_system.enums.EmailType;
import vn.nhom24.bus_ticket_reservation_system.service.EmailSevice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class EmailSeviceIpml implements EmailSevice {
    public static final String UTF_8_ENCODING = "UTF-8";

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;



    public EmailSeviceIpml() {
    }

    @Override
    public void sendSimpleMailMessage(String name, String to, String token) {
    }

    @Override
    public void sendMimeMessageWithAttachments(String name, String to, String token) {

    }

    @Override
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {

    }
    @Override
    public void sendHtmlEmail(String to, EmailType emailType, Map<String, Object> info) {
        try {
            Context context = new Context();

            context.setVariables(info);

            String text = templateEngine.process(getTemplateName(emailType), context);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);

            helper.setPriority(1);
            helper.setSubject(getSubject(emailType));
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);

            if (info.containsKey("bookingCode")) {
                String bookingCode = (String) info.get("bookingCode");
                try {
                    byte[] qrCodeImage = generateQRCodeImage(bookingCode, 200, 200); // Tạo ảnh QR
                    ByteArrayResource imageResource = new ByteArrayResource(qrCodeImage);

                    helper.addInline("qrCodeImage", imageResource, "image/png"); // Đính kèm
                } catch (WriterException | IOException e) {
                    System.out.println("Error generating QR code: " + e.getMessage());
                }
            }

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }


    @Override
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {

    }

    private String getSubject(EmailType emailType) {
        switch (emailType) {
            case REMINDER_TRIP:
                return "[XURYBUS] Nhắc nhở chuyến xe";
            case TICKET_CONFIRMATION:
                return "[XURYBUS] Mua vé xe thành công";
            case ACCOUNT_REGISTRATION_CONFIRMATION:
                return "[XURYBUS] Xác nhận đăng ký tài khoản XURYBUS";
            default:
                return "Thông báo";
        }
    }

    private String getTemplateName(EmailType emailType) {
        switch (emailType) {
            case REMINDER_TRIP:
                return "public/reminder_email";
            case TICKET_CONFIRMATION:
                return "public/email-successful-ticket";
            case ACCOUNT_REGISTRATION_CONFIRMATION:
                return "public/emailtemplate";
            default:
                return "default_template";
        }
    }



    private byte[] generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }
}

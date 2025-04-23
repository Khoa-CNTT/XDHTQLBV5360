package vn.nhom24.bus_ticket_reservation_system.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.Date;

@Service
public class PayosService {
    private final PayOS payOS;

    public PayosService(PayOS payOS) {
        this.payOS = payOS;
    }
    public String checkout(HttpServletRequest request, String productName, int price ,int bookingId, String paymentType) throws Exception {

            String baseUrl = getBaseUrl(request);
            String prcName = productName;
            String description = "Thanh toan don hang";
            String returnUrl = baseUrl + "/public/payment-return-payos?bookingId=" + bookingId ;
            String cancelUrl = baseUrl + "/public/payment-return-payos?bookingId=" + bookingId ;
            int prcprice = price;
            // Gen order code
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = ItemData.builder().name(prcName).quantity(1).price(prcprice).build();
            PaymentData paymentData = PaymentData.builder().orderCode(orderCode).amount(price).description(description)
                    .returnUrl(returnUrl).cancelUrl(cancelUrl).item(item).build();
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            return  data.getCheckoutUrl();

    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }

}

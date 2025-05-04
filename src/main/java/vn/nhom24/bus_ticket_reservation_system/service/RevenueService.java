package vn.nhom24.bus_ticket_reservation_system.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.nhom24.bus_ticket_reservation_system.DTO.RevenueStat;
import vn.nhom24.bus_ticket_reservation_system.DTO.RevenueSummary;
import vn.nhom24.bus_ticket_reservation_system.entity.Route;
import vn.nhom24.bus_ticket_reservation_system.repository.PaymentRepository;
import vn.nhom24.bus_ticket_reservation_system.repository.RouteRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RevenueService {
    PaymentRepository paymentRepository;
    RouteRepository routeRepository;

    public List<RevenueStat> getFilteredStats(LocalDate startDate, LocalDate endDate, String routeName) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        log.info("Start date: {}, End date: {}", startDateTime, endDateTime);
        log.info("Route name: {}", routeName);

        return paymentRepository.findRevenueStatsByPeriodAndRoute(
                startDateTime,
                endDateTime,
                routeName
        );
    }

    public RevenueSummary getRevenueSummary() {
        return paymentRepository.findOverallRevenueSummary();
    }

    public List<String> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(Route::getName)
                .collect(Collectors.toList());
    }
}

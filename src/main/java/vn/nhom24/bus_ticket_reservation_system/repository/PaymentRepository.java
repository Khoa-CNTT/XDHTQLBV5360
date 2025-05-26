package vn.nhom24.bus_ticket_reservation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.nhom24.bus_ticket_reservation_system.DTO.RevenueStat;
import vn.nhom24.bus_ticket_reservation_system.DTO.RevenueSummary;
import vn.nhom24.bus_ticket_reservation_system.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE MONTH(p.payDate) = MONTH(CURRENT_DATE) AND YEAR(p.payDate) = YEAR(CURRENT_DATE)")
    Double getTotalAmountThisMonth();
    Optional<Payment> findByBookingId(Integer bookingId);

    @Query(value = """
        SELECT
            r.route_name AS route,
            -- Ép kiểu kết quả SUM(COUNT) thành kiểu số nguyên lớn (SIGNED/BIGINT)
            CAST(COALESCE(SUM(bt_count.ticket_count), 0) AS SIGNED) AS totalTicket,
            -- Ép kiểu SUM(amount) sang DECIMAL để khớp BigDecimal
            CAST(COALESCE(SUM(paid_payments.amount), 0) AS DECIMAL(19, 2)) AS totalRevenue,
            -- AVG() trả về DECIMAL/NUMERIC, sẽ khớp với BigDecimal trong DTO
            COALESCE(AVG(rt.rating_number), 0.0) AS averageRating,
             COUNT(DISTINCT t.trip_id) AS totalTrips
        FROM
            routes r
        LEFT JOIN
            schedule s ON r.route_id = s.route_id
        LEFT JOIN
            trips t ON s.schedule_id = t.schedule_id
        LEFT JOIN
            bookings b ON t.trip_id = b.trip_id
        LEFT JOIN
            (SELECT booking_id, amount
             FROM payment
             WHERE status = 'PAID'
               AND paydate BETWEEN :start AND :end
            ) AS paid_payments ON b.booking_id = paid_payments.booking_id
        LEFT JOIN
            (SELECT booking_id, COUNT(*) as ticket_count
             FROM booking_ticket
             GROUP BY booking_id
            ) AS bt_count ON b.booking_id = bt_count.booking_id
        LEFT JOIN
            rating rt ON b.booking_id = rt.booking_id
        WHERE
            (:routeName IS NULL OR r.route_name = :routeName)
            AND paid_payments.booking_id IS NOT NULL
            AND t.status IN ('COMPLETE')
        GROUP BY
            r.route_id, r.route_name
        ORDER BY
            r.route_name
        """, nativeQuery = true)
    List<RevenueStat> findRevenueStatsByPeriodAndRoute(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("routeName") String routeName
    );


    @Query(value = """
        SELECT
            -- COUNT trả về BIGINT, CAST AS SIGNED tốt -> Khớp Long
            COALESCE(CAST((SELECT COUNT(bt.ticket_id)
                           FROM booking_ticket bt
                           WHERE bt.booking_id IN (SELECT p.booking_id FROM payment p WHERE p.status = 'PAID')) AS SIGNED), 0) AS totalTicket,

            -- SUM(amount) trả về DECIMAL/DOUBLE, CAST AS DECIMAL -> Khớp BigDecimal
            COALESCE(CAST((SELECT SUM(p.amount)
                           FROM payment p
                           WHERE p.status = 'PAID') AS DECIMAL(19,2)), 0.00) AS totalRevenue,

            -- COUNT trả về BIGINT, CAST AS SIGNED tốt -> Khớp Long
            COALESCE(CAST((SELECT COUNT(DISTINCT t.trip_id)
                           FROM trips t
                           WHERE t.status = 'COMPLETE') AS SIGNED), 0) AS totalTrips,

            -- COUNT trả về BIGINT, CAST AS SIGNED tốt -> Khớp Long
            COALESCE(CAST((SELECT COUNT(DISTINCT c.car_id)
                           FROM cars c) AS SIGNED), 0) AS totalCar
        """, nativeQuery = true)
    RevenueSummary findOverallRevenueSummary();
}

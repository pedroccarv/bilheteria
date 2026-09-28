package com.bilheteria.model;

import com.bilheteria.enums.ReservationStatus;
import com.bilheteria.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationTest {

    private final LocalDateTime createdAt = LocalDateTime.of(2026, 12, 2, 10, 0);

    @Test
    void confirmPaymentHonorsStatusAndDeadline() {
        Reservation pending = reservation();
        pending.confirmPayment(createdAt.plusMinutes(5));
        assertEquals(ReservationStatus.PAID, pending.getStatus());
        assertEquals(createdAt.plusMinutes(5), pending.getPaidAt());
        assertEquals("reserva não está pendente", reject(pending, createdAt.plusMinutes(6)));

        Reservation expired = reservation();
        expired.expire();
        assertEquals("reserva não está pendente", reject(expired, createdAt.plusMinutes(1)));

        Reservation cancelled = reservation();
        cancelled.cancel();
        assertEquals("reserva não está pendente", reject(cancelled, createdAt.plusMinutes(1)));

        Reservation late = reservation();
        assertEquals("reserva expirada", reject(late, createdAt.plusMinutes(11)));
        assertEquals(ReservationStatus.PENDING, late.getStatus());
    }

    private String reject(Reservation reservation, LocalDateTime now) {
        return assertThrows(BusinessRuleException.class, () -> reservation.confirmPayment(now)).getMessage();
    }

    private Reservation reservation() {
        Event event = new Event("Show", "Teatro", LocalDateTime.of(2026, 12, 15, 20, 0));
        Lot lot = new Lot(
                event,
                "1º lote",
                new BigDecimal("80.00"),
                10,
                LocalDateTime.of(2026, 12, 1, 10, 0),
                null);
        return new Reservation(lot, "a@email.com", 2, createdAt);
    }
}

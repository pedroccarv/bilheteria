package com.bilheteria.service;

import com.bilheteria.dto.request.CreateEventRequest;
import com.bilheteria.dto.request.CreateLotRequest;
import com.bilheteria.dto.request.CreateReservationRequest;
import com.bilheteria.dto.response.LotResponse;
import com.bilheteria.enums.ReservationStatus;
import com.bilheteria.model.Event;
import com.bilheteria.model.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class ReservationFlowTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private EventService eventService;

    @Autowired
    private LotService lotService;

    @Autowired
    private ReservationService reservationService;

    @Test
    void happyPathReservesAndPays() {
        Event event = eventService.create(new CreateEventRequest(
                "Show",
                "Teatro",
                LocalDateTime.now().plusDays(30)));
        LotResponse lot = lotService.create(event.getId(), new CreateLotRequest(
                "1º lote",
                new BigDecimal("50.00"),
                10,
                LocalDateTime.now().minusDays(1),
                null));

        Reservation reservation = reservationService.create(
                new CreateReservationRequest(lot.id(), "a@email.com", 2));
        Reservation paid = reservationService.pay(reservation.getId());

        assertEquals(ReservationStatus.PAID, paid.getStatus());
        assertEquals(8, lotService.get(lot.id()).availableQuantity());
    }
}

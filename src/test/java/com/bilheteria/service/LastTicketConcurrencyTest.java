package com.bilheteria.service;

import com.bilheteria.dto.request.CreateEventRequest;
import com.bilheteria.dto.request.CreateLotRequest;
import com.bilheteria.dto.request.CreateReservationRequest;
import com.bilheteria.dto.response.LotResponse;
import com.bilheteria.enums.ReservationStatus;
import com.bilheteria.exception.BusinessRuleException;
import com.bilheteria.model.Event;
import com.bilheteria.repository.ReservationRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.OptimisticLockingFailureException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class LastTicketConcurrencyTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private EventService eventService;

    @Autowired
    private LotService lotService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void onlyOneReservationWinsTheLastTicket() throws Exception {
        Event event = eventService.create(new CreateEventRequest(
                "Show",
                "Teatro",
                LocalDateTime.now().plusDays(30)));
        LotResponse lot = lotService.create(event.getId(), new CreateLotRequest(
                "Último ingresso",
                new BigDecimal("50.00"),
                1,
                LocalDateTime.now().minusDays(1),
                null));

        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger created = new AtomicInteger();
        List<Throwable> failures = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threads; i++) {
            String email = "pessoa" + i + "@email.com";
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    reservationService.create(new CreateReservationRequest(lot.id(), email, 1));
                    created.incrementAndGet();
                } catch (Throwable ex) {
                    failures.add(ex);
                }
            });
        }

        assertTrue(ready.await(10, TimeUnit.SECONDS));
        start.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(60, TimeUnit.SECONDS));

        assertEquals(1, created.get());
        assertEquals(9, failures.size());
        assertTrue(failures.stream().allMatch(this::isStockRace));
        assertEquals(0, lotService.get(lot.id()).availableQuantity());
        assertEquals(1, pendingReservations(lot.id()));
    }

    private boolean isStockRace(Throwable failure) {
        for (Throwable current = failure; current != null; current = current.getCause()) {
            if (current instanceof BusinessRuleException
                    || current instanceof OptimisticLockingFailureException
                    || current instanceof OptimisticLockException) {
                return true;
            }
        }
        return false;
    }

    private long pendingReservations(UUID lotId) {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getLot().getId().equals(lotId))
                .filter(reservation -> reservation.getStatus() == ReservationStatus.PENDING)
                .count();
    }
}

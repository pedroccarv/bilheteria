package com.bilheteria.service;

import com.bilheteria.enums.ReservationStatus;
import com.bilheteria.model.Reservation;
import com.bilheteria.repository.ReservationRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationExpirationJob {

    private final ReservationRepository reservationRepository;
    private final ReservationExpirationWorker worker;
    private final Clock clock;

    @Scheduled(fixedDelay = 60_000)
    public void expireDueReservations() {
        for (Reservation reservation : dueReservations()) {
            try {
                expireOne(reservation.getId());
            } catch (OptimisticLockingFailureException | OptimisticLockException ignored) {
            }
        }
    }

    public void expireOne(UUID reservationId) {
        worker.expireOne(reservationId);
    }

    public List<Reservation> dueReservations() {
        return reservationRepository.findAllByStatusAndExpiresAtBefore(
                ReservationStatus.PENDING,
                LocalDateTime.now(clock));
    }
}

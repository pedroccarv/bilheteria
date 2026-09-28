package com.bilheteria.service;

import com.bilheteria.exception.NotFoundException;
import com.bilheteria.model.Reservation;
import com.bilheteria.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationExpirationWorker {

    private final ReservationRepository reservationRepository;

    @Transactional
    public void expireOne(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("reserva não encontrada"));
        reservation.expire();
        reservation.getLot().release(reservation.getQuantity());
    }
}

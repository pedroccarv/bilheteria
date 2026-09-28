package com.bilheteria.service;

import com.bilheteria.dto.request.CreateReservationRequest;
import com.bilheteria.exception.NotFoundException;
import com.bilheteria.model.Reservation;
import com.bilheteria.repository.LotRepository;
import com.bilheteria.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LotRepository lotRepository;
    private final Clock clock;

    @Transactional
    public Reservation create(CreateReservationRequest request) {
        throw new UnsupportedOperationException("TODO RN-08: ReservationService.create");
    }

    @Transactional(readOnly = true)
    public Reservation get(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("reserva não encontrada"));
    }

    @Transactional
    public Reservation pay(UUID id) {
        throw new UnsupportedOperationException("TODO RN-09: ReservationService.pay");
    }

    @Transactional
    public Reservation cancel(UUID id) {
        throw new UnsupportedOperationException("TODO RN-10: ReservationService.cancel");
    }

    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }
}

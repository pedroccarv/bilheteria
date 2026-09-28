package com.bilheteria.service;

import com.bilheteria.dto.request.CreateReservationRequest;
import com.bilheteria.exception.BusinessRuleException;
import com.bilheteria.exception.NotFoundException;
import com.bilheteria.model.Lot;
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
        Lot lot = lotRepository.findById(request.lotId())
                .orElseThrow(() -> new NotFoundException("lote não encontrado"));
        LocalDateTime now = now();
        if (!lot.isOpenForSalesAt(now)) {
            throw new BusinessRuleException("lote fora da janela de vendas");
        }
        lot.reserve(request.quantity());
        Reservation reservation = new Reservation(lot, request.customerEmail(), request.quantity(), now);
        return reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public Reservation get(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("reserva não encontrada"));
    }

    @Transactional
    public Reservation pay(UUID id) {
        Reservation reservation = get(id);
        reservation.confirmPayment(now());
        return reservation;
    }

    @Transactional
    public Reservation cancel(UUID id) {
        Reservation reservation = get(id);
        reservation.cancel();
        reservation.getLot().release(reservation.getQuantity());
        return reservation;
    }

    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }
}

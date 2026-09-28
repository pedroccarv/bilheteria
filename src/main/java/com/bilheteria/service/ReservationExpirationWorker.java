package com.bilheteria.service;

import com.bilheteria.repository.LotRepository;
import com.bilheteria.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationExpirationWorker {

    private final ReservationRepository reservationRepository;
    private final LotRepository lotRepository;

    @Transactional
    public void expireOne(UUID reservationId) {
        throw new UnsupportedOperationException("TODO RN-11: ReservationExpirationWorker.expireOne");
    }
}

package com.bilheteria.service;

import com.bilheteria.dto.request.CreateLotRequest;
import com.bilheteria.dto.response.LotResponse;
import com.bilheteria.exception.NotFoundException;
import com.bilheteria.model.Event;
import com.bilheteria.model.Lot;
import com.bilheteria.repository.EventRepository;
import com.bilheteria.repository.LotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LotService {

    private final LotRepository lotRepository;
    private final EventRepository eventRepository;
    private final Clock clock;

    @Transactional
    public LotResponse create(UUID eventId, CreateLotRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("evento não encontrado"));
        Lot lot = new Lot(
                event,
                request.name(),
                request.price(),
                request.totalQuantity(),
                request.salesStart(),
                request.salesEnd());
        return toResponse(lotRepository.save(lot));
    }

    @Transactional(readOnly = true)
    public LotResponse get(UUID id) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("lote não encontrado"));
        return toResponse(lot);
    }

    private LotResponse toResponse(Lot lot) {
        boolean openForSales = lot.isOpenForSalesAt(LocalDateTime.now(clock));
        return LotResponse.from(lot, openForSales);
    }
}

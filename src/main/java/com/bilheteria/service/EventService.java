package com.bilheteria.service;

import com.bilheteria.dto.request.CreateEventRequest;
import com.bilheteria.exception.NotFoundException;
import com.bilheteria.model.Event;
import com.bilheteria.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public Event create(CreateEventRequest request) {
        Event event = new Event(request.name(), request.venue(), request.startsAt());
        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<Event> list() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Event get(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("evento não encontrado"));
    }
}

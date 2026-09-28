package com.bilheteria.dto.response;

import com.bilheteria.model.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String name,
        String venue,
        LocalDateTime startsAt,
        LocalDateTime createdAt
) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getVenue(),
                event.getStartsAt(),
                event.getCreatedAt());
    }
}

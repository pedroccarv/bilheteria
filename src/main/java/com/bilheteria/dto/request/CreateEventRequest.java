package com.bilheteria.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateEventRequest(
        @NotBlank(message = "obrigatório") String name,
        @NotBlank(message = "obrigatório") String venue,
        @NotNull(message = "obrigatório") @Future(message = "deve estar no futuro") LocalDateTime startsAt
) {
}

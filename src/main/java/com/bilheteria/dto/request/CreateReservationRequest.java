package com.bilheteria.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateReservationRequest(
        @NotNull(message = "obrigatório") UUID lotId,
        @NotBlank(message = "obrigatório") @Email(message = "e-mail inválido") String customerEmail,
        @NotNull(message = "obrigatório") @Min(value = 1, message = "mínimo 1") @Max(value = 4, message = "máximo 4") Integer quantity
) {
}

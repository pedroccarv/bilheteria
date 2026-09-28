package com.bilheteria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateLotRequest(
        @NotBlank(message = "obrigatório") String name,
        @NotNull(message = "obrigatório") @Positive(message = "deve ser maior que zero") BigDecimal price,
        @NotNull(message = "obrigatório") @Positive(message = "deve ser maior que zero") Integer totalQuantity,
        LocalDateTime salesStart,
        LocalDateTime salesEnd
) {
}

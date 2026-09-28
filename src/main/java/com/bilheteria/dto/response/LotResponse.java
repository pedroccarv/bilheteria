package com.bilheteria.dto.response;

import com.bilheteria.model.Lot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record LotResponse(
        UUID id,
        UUID eventId,
        String name,
        BigDecimal price,
        int totalQuantity,
        int availableQuantity,
        LocalDateTime salesStart,
        LocalDateTime salesEnd,
        boolean openForSales
) {
    public static LotResponse from(Lot lot, boolean openForSales) {
        return new LotResponse(
                lot.getId(),
                lot.getEvent().getId(),
                lot.getName(),
                lot.getPrice(),
                lot.getTotalQuantity(),
                lot.getAvailableQuantity(),
                lot.getSalesStart(),
                lot.getSalesEnd(),
                openForSales);
    }
}

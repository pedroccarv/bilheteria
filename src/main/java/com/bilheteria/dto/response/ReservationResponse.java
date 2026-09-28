package com.bilheteria.dto.response;

import com.bilheteria.enums.ReservationStatus;
import com.bilheteria.model.Reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID lotId,
        String customerEmail,
        int quantity,
        BigDecimal unitPrice,
        ReservationStatus status,
        LocalDateTime expiresAt,
        LocalDateTime createdAt,
        LocalDateTime paidAt
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getLot().getId(),
                reservation.getCustomerEmail(),
                reservation.getQuantity(),
                reservation.getUnitPrice(),
                reservation.getStatus(),
                reservation.getExpiresAt(),
                reservation.getCreatedAt(),
                reservation.getPaidAt());
    }
}

package com.bilheteria.model;

import com.bilheteria.enums.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lot_id", nullable = false)
    private Lot lot;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public Reservation(Lot lot, String customerEmail, int quantity, LocalDateTime now) {
        this.id = UUID.randomUUID();
        this.lot = lot;
        this.customerEmail = customerEmail;
        this.quantity = quantity;
        this.unitPrice = lot.getPrice();
        this.status = ReservationStatus.PENDING;
        this.createdAt = now;
        this.expiresAt = now.plusMinutes(10);
    }

    public void confirmPayment(LocalDateTime now) {
        throw new UnsupportedOperationException("TODO RN-09: Reservation.confirmPayment");
    }

    public void cancel() {
        throw new UnsupportedOperationException("TODO RN-10: Reservation.cancel");
    }

    public void expire() {
        throw new UnsupportedOperationException("TODO RN-11: Reservation.expire");
    }
}

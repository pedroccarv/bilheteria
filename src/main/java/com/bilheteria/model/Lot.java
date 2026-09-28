package com.bilheteria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lot {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Column(name = "sales_start")
    private LocalDateTime salesStart;

    @Column(name = "sales_end")
    private LocalDateTime salesEnd;

    @Version
    private Long version;

    public Lot(
            Event event,
            String name,
            BigDecimal price,
            int totalQuantity,
            LocalDateTime salesStart,
            LocalDateTime salesEnd) {
        this.id = UUID.randomUUID();
        this.event = event;
        this.name = name;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity;
        this.salesStart = salesStart;
        this.salesEnd = salesEnd;
    }

    public void reserve(int quantity) {
        throw new UnsupportedOperationException("TODO RN-05: Lot.reserve");
    }

    public void release(int quantity) {
        throw new UnsupportedOperationException("TODO RN-05: Lot.release");
    }

    public boolean isOpenForSalesAt(LocalDateTime now) {
        if (salesStart != null && salesEnd != null) {
            if (!now.isAfter(salesEnd) && !now.isAfter(event.getStartsAt())) {
                return true;
            }
        }
        return false;
    }
}

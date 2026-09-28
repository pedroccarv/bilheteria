package com.bilheteria.model;

import com.bilheteria.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LotTest {

    @Test
    void reserveReducesBalanceAndRejectsWhenUnavailable() {
        Lot lot = lot(10, start(), end(), eventStart());

        lot.reserve(2);

        assertEquals(8, lot.getAvailableQuantity());
        assertThrows(BusinessRuleException.class, () -> lot.reserve(9));
        assertEquals(8, lot.getAvailableQuantity());
    }

    @Test
    void releaseRestoresBalanceAndRejectsAboveTotal() {
        Lot lot = lot(10, start(), end(), eventStart());
        lot.reserve(4);

        lot.release(3);

        assertEquals(9, lot.getAvailableQuantity());
        assertThrows(BusinessRuleException.class, () -> lot.release(2));
        assertEquals(9, lot.getAvailableQuantity());
    }

    @Test
    void isOpenForSalesAtCoversTheFourCases() {
        Lot lot = lot(10, start(), end(), eventStart());

        assertFalse(lot.isOpenForSalesAt(start().minusMinutes(1)));
        assertTrue(lot.isOpenForSalesAt(start().plusDays(1)));
        assertFalse(lot.isOpenForSalesAt(end().plusMinutes(1)));

        Lot closingAtEvent = lot(10, start(), end().plusDays(20), start().plusDays(2));
        assertFalse(closingAtEvent.isOpenForSalesAt(start().plusDays(3)));
    }

    @Test
    void nullSalesStartMeansSalesHaveNotBegun() {
        Lot lot = lot(10, null, end(), eventStart());

        assertFalse(lot.isOpenForSalesAt(start().plusDays(1)));
    }

    @Test
    void nullSalesEndStaysOpenUntilTheEventStarts() {
        Lot lot = lot(10, start(), null, eventStart());

        assertTrue(lot.isOpenForSalesAt(start().plusDays(1)));
        assertFalse(lot.isOpenForSalesAt(eventStart().plusMinutes(1)));
    }

    private static Lot lot(int quantity, LocalDateTime salesStart, LocalDateTime salesEnd, LocalDateTime eventStart) {
        Event event = new Event("Show", "Teatro", eventStart);
        return new Lot(event, "1º lote", new BigDecimal("80.00"), quantity, salesStart, salesEnd);
    }

    private static LocalDateTime start() {
        return LocalDateTime.of(2026, 12, 1, 10, 0);
    }

    private static LocalDateTime end() {
        return LocalDateTime.of(2026, 12, 10, 18, 0);
    }

    private static LocalDateTime eventStart() {
        return LocalDateTime.of(2026, 12, 15, 20, 0);
    }
}

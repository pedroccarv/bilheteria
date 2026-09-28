package com.bilheteria.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class LastTicketConcurrencyTest {

    @Test
    @Disabled("Lote com 1 ingresso, 10 threads pedindo 1. Exatamente 1 PENDING, saldo 0, as outras 9 falham")
    void onlyOneReservationWinsTheLastTicket() {
    }
}

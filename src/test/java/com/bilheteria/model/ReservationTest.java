package com.bilheteria.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @Disabled("RN-09: confirmPayment aceita PENDING no prazo e recusa PAID, EXPIRED, CANCELLED e fora do prazo")
    void confirmPaymentHonorsStatusAndDeadline() {
    }
}

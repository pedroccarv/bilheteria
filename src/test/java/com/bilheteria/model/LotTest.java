package com.bilheteria.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class LotTest {

    @Test
    @Disabled("RN-05: reserve reduz o saldo e recusa quando a quantidade passa do disponível")
    void reserveReducesBalanceAndRejectsWhenUnavailable() {
    }

    @Test
    @Disabled("RN-05: release devolve o saldo e recusa se passar do total")
    void releaseRestoresBalanceAndRejectsAboveTotal() {
    }

    @Test
    @Disabled("RN-06: antes de salesStart, dentro da janela, depois de salesEnd, depois do evento")
    void isOpenForSalesAtCoversTheFourCases() {
    }
}

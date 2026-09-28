# Bilheteria

API de venda de ingressos com lotes, reserva de 10 minutos e estoque que não fica negativo.

Spring Boot 3.5 e Java 21.

## Como rodar

```bash
docker compose up --build
```

Swagger: http://localhost:8080/swagger-ui.html

Health: http://localhost:8080/actuator/health

O arquivo `requests.http` tem o fluxo criar evento, criar lote, reservar e pagar.

Para compilar e rodar os testes na máquina, use JDK 21 e `.\mvnw.cmd test`.

## Pacotes

`controller`, `service`, `repository`, `model`, `dto.request`, `dto.response`, `enums`, `exception`, `config`.

## O que você implementa

1. `Lot.isOpenForSalesAt` ainda precisa aceitar `salesStart` e `salesEnd` nulos e respeitar o início da janela.
2. `Lot.reserve` e `Lot.release`. O service não altera `availableQuantity`.
3. `Reservation.confirmPayment`, `cancel` e `expire`.
4. `ReservationService.create`, `pay` e `cancel`.
5. O corpo de `ReservationExpirationJob.expireDueReservations` e de `ReservationExpirationWorker.expireOne`.
6. Os quatro testes em `src/test`, hoje `@Disabled`.

O construtor de `Lot` já iguala `availableQuantity` a `totalQuantity`. O de `Reservation` já nasce `PENDING`, copia o preço do lote e define `expiresAt` como agora mais 10 minutos.

## Decisões para reescrever com as suas palavras

- Por que lock otimista no lote.
- Por que o estoque sai na reserva, não no pagamento.
- Por que um job de 1 minuto em vez de fila.
- Por que Testcontainers em vez de H2.

## Com mais tempo

Autenticação, gateway de pagamento, ShedLock se houver mais de uma instância, métrica de reservas expiradas.

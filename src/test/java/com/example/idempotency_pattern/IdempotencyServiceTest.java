package com.example.idempotency_pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.idempotency_pattern.service.IdempotencyService;

public class IdempotencyServiceTest {

    private IdempotencyService idempotencyService;

    @BeforeEach
    void setUp() {
        idempotencyService = new IdempotencyService();
    }

    @Test
    void testFirstRequestProcessedSuccessfully() {
        String idempotencyKey = "12345";
        String transactionDetails = "PaymentRequest{amount=100.0, currency='USD', cardNumber='4111111111111111'}";

        assertTrue(idempotencyService.checkAndProcessTransaction(idempotencyKey, transactionDetails));
    }

    @Test
    void testDuplicateRequestRejected() {
        String idempotencyKey = "12345";
        String transactionDetails = "PaymentRequest{amount=100.0, currency='USD', cardNumber='4111111111111111'}";

        idempotencyService.checkAndProcessTransaction(idempotencyKey, transactionDetails);

        assertFalse(idempotencyService.checkAndProcessTransaction(idempotencyKey, transactionDetails));
    }
}

package com.example.idempotency_pattern.controller;

import com.example.idempotency_pattern.entity.PaymentRequest;
import com.example.idempotency_pattern.service.IdempotencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IdempotencyService idempotencyService;

    @PostMapping
    public ResponseEntity<String> processPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                                 @RequestBody PaymentRequest paymentRequest) {
        boolean isProcessed = idempotencyService.checkAndProcessTransaction(idempotencyKey, paymentRequest.toString());

        if (isProcessed) {
            return ResponseEntity.ok("Payment processed successfully");
        } else {
            return ResponseEntity.status(409).body("Duplicate request, Payment already processed with the given Idempotency Key.");
        }
    }
}

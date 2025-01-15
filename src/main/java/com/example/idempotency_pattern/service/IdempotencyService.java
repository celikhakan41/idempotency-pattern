package com.example.idempotency_pattern.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdempotencyService {

    private final ConcurrentHashMap<String, String> transactionCache = new ConcurrentHashMap<>();

    public boolean checkAndProcessTransaction(String idempotencyKey, String transactionDetails) {
        String existingTransaction = transactionCache.get(idempotencyKey);
        if (existingTransaction != null) {
            return false;
        }

        transactionCache.put(idempotencyKey, transactionDetails);
        return true;
    }
}

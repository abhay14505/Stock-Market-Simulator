package com.example.stocksimulator.factory;

import java.time.LocalDateTime;

import com.example.stocksimulator.model.Transaction;

// DESIGN PATTERN: Factory Pattern (Creational)
// Encapsulates the creation of Transaction objects so that
// TransactionService does not instantiate Transaction directly.
public final class TransactionFactory {

    private TransactionFactory() {
    }

    public static Transaction createTransaction(Long traderId, Long stockId, int quantity, double price, String type) {
        Transaction transaction = new Transaction();
        transaction.setTraderId(traderId);
        transaction.setStockId(stockId);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
}

package com.example.stocksimulator.observer;

import org.springframework.stereotype.Component;

import com.example.stocksimulator.model.Transaction;

// DESIGN PATTERN: Observer Pattern (Behavioral)
// TradeObserver implementations are notified after every buy/sell transaction.
// This decouples post-trade actions (auditing, alerts) from core trade logic.
@Component
public class AuditLogObserver implements TradeObserver {

    @Override
    public void onTrade(Transaction transaction) {
        System.out.println("[AUDIT] Trade executed: " + transaction.getType() + " "
                + transaction.getQuantity() + " shares of stock " + transaction.getStockId());
    }
}

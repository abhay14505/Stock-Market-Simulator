package com.example.stocksimulator.observer;

import org.springframework.stereotype.Component;

import com.example.stocksimulator.model.Transaction;

// DESIGN PATTERN: Observer Pattern (Behavioral)
// TradeObserver implementations are notified after every buy/sell transaction.
// This decouples post-trade actions (auditing, alerts) from core trade logic.
@Component
public class BalanceAlertObserver implements TradeObserver {

    @Override
    public void onTrade(Transaction transaction) {
        double tradeValue = transaction.getQuantity() * transaction.getPrice();
        if (tradeValue > 10000) {
            System.out.println("[ALERT] High value trade detected: " + tradeValue);
        }
    }
}

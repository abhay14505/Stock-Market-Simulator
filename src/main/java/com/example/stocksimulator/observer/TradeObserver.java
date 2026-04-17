package com.example.stocksimulator.observer;

import com.example.stocksimulator.model.Transaction;

// DESIGN PATTERN: Observer Pattern (Behavioral)
// TradeObserver implementations are notified after every buy/sell transaction.
// This decouples post-trade actions (auditing, alerts) from core trade logic.
// DESIGN PRINCIPLE: Open/Closed Principle (OCP)
// New post-trade features are added via new implementations, without modifying core service logic.
// DESIGN PRINCIPLE: Dependency Inversion Principle (DIP)
// High-level trade workflow depends on this interface abstraction.
public interface TradeObserver {

    void onTrade(Transaction transaction);
}

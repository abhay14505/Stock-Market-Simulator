package com.example.stocksimulator.model;

import jakarta.persistence.Entity;

@Entity
// DESIGN PRINCIPLE: Liskov Substitution Principle (LSP)
// Trader extends User while preserving expected base-user behavior.
public class Trader extends User {

    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
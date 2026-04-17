package com.example.stocksimulator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "portfolio")
public class Portfolio {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private Long traderId;
private Long stockId;
private int quantity;

// Getters & Setters

public Long getId() {
    return id;
}

public Long getTraderId() {
    return traderId;
}

public void setTraderId(Long traderId) {
    this.traderId = traderId;
}

public Long getStockId() {
    return stockId;
}

public void setStockId(Long stockId) {
    this.stockId = stockId;
}

public int getQuantity() {
    return quantity;
}

public void setQuantity(int quantity) {
    this.quantity = quantity;
}

}

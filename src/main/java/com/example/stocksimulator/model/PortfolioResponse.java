package com.example.stocksimulator.model;

public class PortfolioResponse {

    private String stockName;
    private int quantity;
    private double price;
    private double totalValue;

    public PortfolioResponse(String stockName, int quantity, double price) {
        this.stockName = stockName;
        this.quantity = quantity;
        this.price = price;
        this.totalValue = quantity * price;
    }

    public String getStockName() {
        return stockName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalValue() {
        return totalValue;
    }
}
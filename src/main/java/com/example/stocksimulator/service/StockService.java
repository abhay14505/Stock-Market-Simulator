package com.example.stocksimulator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stocksimulator.exception.BadRequestException;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.repository.StockRepository;

@Service
// DESIGN PRINCIPLE: Single Responsibility Principle (SRP)
// StockService encapsulates stock validation and stock persistence logic.
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public Stock saveStock(Stock stock) {
        if (stock.getName() == null || stock.getName().trim().isEmpty()) {
            throw new BadRequestException("Stock name is required");
        }

        String normalizedName = stock.getName().trim();
        stock.setName(normalizedName);

        if (stockRepository.findByNameIgnoreCase(normalizedName).isPresent()) {
            throw new BadRequestException("Stock already exists!");
        }

        return stockRepository.save(stock);
    }

    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Stock not found"));
    }

    public Stock updateStock(Stock stock) {
        Stock existingStock = getStockById(stock.getStockId());
        existingStock.setPrice(stock.getPrice());
        existingStock.setAvailableQuantity(stock.getAvailableQuantity());
        return stockRepository.save(existingStock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}

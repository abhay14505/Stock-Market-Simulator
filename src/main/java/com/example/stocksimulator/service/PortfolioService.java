package com.example.stocksimulator.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stocksimulator.model.Portfolio;
import com.example.stocksimulator.model.PortfolioResponse;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.repository.PortfolioRepository;

@Service
// DESIGN PRINCIPLE: Single Responsibility Principle (SRP)
// PortfolioService is responsible only for portfolio projection/aggregation.
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockService stockService;

    public List<PortfolioResponse> getPortfolio(Long traderId) {
        List<Portfolio> portfolios = portfolioRepository.findByTraderId(traderId);

        return portfolios.stream()
                .filter(portfolio -> portfolio.getQuantity() > 0)
                .map(portfolio -> {
                    Stock stock = stockService.getStockById(portfolio.getStockId());
                    return new PortfolioResponse(stock.getName(), portfolio.getQuantity(), stock.getPrice());
                })
                .collect(Collectors.toList());
    }
}

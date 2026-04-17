package com.example.stocksimulator.facade;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stocksimulator.model.PortfolioResponse;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.model.Trader;
import com.example.stocksimulator.model.Transaction;
import com.example.stocksimulator.service.PortfolioService;
import com.example.stocksimulator.service.StockService;
import com.example.stocksimulator.service.TraderService;
import com.example.stocksimulator.service.TransactionService;

// DESIGN PATTERN: Facade Pattern (Structural)
// TradingFacade provides a simplified interface to the complex
// subsystem of TraderService, StockService, and TransactionService.
// The UI/frontend calls TradingFacade instead of individual services.
@Service
public class TradingFacade {

    @Autowired
    private TraderService traderService;

    @Autowired
    private StockService stockService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PortfolioService portfolioService;

    public Transaction buyStock(Long traderId, Long stockId, int quantity) {
        return transactionService.buyStock(traderId, stockId, quantity);
    }

    public Transaction sellStock(Long traderId, Long stockId, int quantity) {
        return transactionService.sellStock(traderId, stockId, quantity);
    }

    public List<PortfolioResponse> getPortfolio(Long traderId) {
        return portfolioService.getPortfolio(traderId);
    }

    public Map<String, Object> getTraderDashboard(Long traderId) {
        Trader trader = getTraderById(traderId);
        List<PortfolioResponse> portfolio = getPortfolio(traderId);
        List<Transaction> recentTransactions = getTransactionsByTrader(traderId).stream().limit(10).toList();
        double portfolioValue = portfolio.stream().mapToDouble(PortfolioResponse::getTotalValue).sum();

        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("trader", trader);
        dashboard.put("portfolio", portfolio);
        dashboard.put("transactions", recentTransactions);
        dashboard.put("portfolioValue", portfolioValue);
        dashboard.put("netWorth", trader.getBalance() + portfolioValue);
        return dashboard;
    }

    public Trader createTrader(Trader trader) {
        return traderService.saveTrader(trader);
    }

    public Stock createStock(Stock stock) {
        return stockService.saveStock(stock);
    }

    public List<Trader> getAllTraders() {
        return traderService.getAllTraders();
    }

    public Trader getTraderById(Long traderId) {
        return traderService.getTraderById(traderId);
    }

    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    public Stock getStockById(Long stockId) {
        return stockService.getStockById(stockId);
    }

    public Stock updateStock(Stock stock) {
        return stockService.updateStock(stock);
    }

    public List<Transaction> getTransactionsByTrader(Long traderId) {
        return transactionService.getTransactionsByTrader(traderId);
    }
}

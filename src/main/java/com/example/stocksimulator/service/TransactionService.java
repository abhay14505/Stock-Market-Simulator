package com.example.stocksimulator.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.stocksimulator.exception.BadRequestException;
import com.example.stocksimulator.factory.TransactionFactory;
import com.example.stocksimulator.model.Portfolio;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.model.Trader;
import com.example.stocksimulator.model.Transaction;
import com.example.stocksimulator.observer.TradeObserver;
import com.example.stocksimulator.repository.PortfolioRepository;
import com.example.stocksimulator.repository.TransactionRepository;

@Service
// DESIGN PRINCIPLE: Single Responsibility Principle (SRP)
// TransactionService manages the trade lifecycle (buy/sell + portfolio/balance updates).
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TraderService traderService;

    @Autowired
    private StockService stockService;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    // DESIGN PRINCIPLE: Dependency Inversion Principle (DIP)
    // Depends on TradeObserver abstraction, not concrete observer types.
    private List<TradeObserver> tradeObservers;

    @Transactional
    public Transaction buyStock(Long traderId, Long stockId, int quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }

        Trader trader = traderService.getTraderById(traderId);
        Stock stock = stockService.getStockById(stockId);
        double totalPrice = stock.getPrice() * quantity;

        if (trader.getBalance() < totalPrice) {
            throw new BadRequestException("Insufficient balance");
        }
        if (stock.getAvailableQuantity() < quantity) {
            throw new BadRequestException("Not enough stock available");
        }

        trader.setBalance(trader.getBalance() - totalPrice);
        traderService.updateTrader(trader);

        stock.setAvailableQuantity(stock.getAvailableQuantity() - quantity);
        stockService.updateStock(stock);

        Portfolio portfolio = portfolioRepository.findByTraderIdAndStockId(traderId, stockId)
                .orElseGet(() -> {
                    Portfolio item = new Portfolio();
                    item.setTraderId(traderId);
                    item.setStockId(stockId);
                    item.setQuantity(0);
                    return item;
                });

        portfolio.setQuantity(portfolio.getQuantity() + quantity);
        portfolioRepository.save(portfolio);

        return saveAndNotify(traderId, stockId, quantity, stock.getPrice(), "BUY");
    }

    @Transactional
    public Transaction sellStock(Long traderId, Long stockId, int quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }

        Trader trader = traderService.getTraderById(traderId);
        Stock stock = stockService.getStockById(stockId);
        Portfolio portfolio = portfolioRepository.findByTraderIdAndStockId(traderId, stockId)
                .orElseThrow(() -> new BadRequestException("No stock owned"));

        if (portfolio.getQuantity() < quantity) {
            throw new BadRequestException("Not enough stock to sell");
        }

        double totalPrice = stock.getPrice() * quantity;
        trader.setBalance(trader.getBalance() + totalPrice);
        traderService.updateTrader(trader);

        stock.setAvailableQuantity(stock.getAvailableQuantity() + quantity);
        stockService.updateStock(stock);

        portfolio.setQuantity(portfolio.getQuantity() - quantity);
        if (portfolio.getQuantity() == 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolioRepository.save(portfolio);
        }

        return saveAndNotify(traderId, stockId, quantity, stock.getPrice(), "SELL");
    }

    public List<Transaction> getTransactionsByTrader(Long traderId) {
        traderService.getTraderById(traderId);
        return transactionRepository.findByTraderId(traderId).stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .toList();
    }

    private Transaction saveAndNotify(Long traderId, Long stockId, int quantity, double price, String type) {
        Transaction transaction = TransactionFactory.createTransaction(traderId, stockId, quantity, price, type);
        Transaction savedTransaction = transactionRepository.save(transaction);
        // DESIGN PRINCIPLE: Open/Closed Principle (OCP)
        // New observer behaviors can be added without changing this trade flow.
        tradeObservers.forEach(observer -> observer.onTrade(savedTransaction));
        return savedTransaction;
    }
}

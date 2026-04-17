package com.example.stocksimulator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.stocksimulator.model.Transaction;
import com.example.stocksimulator.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/buy")
    public Transaction buyStock(@RequestParam Long traderId, @RequestParam Long stockId, @RequestParam int quantity) {
        return transactionService.buyStock(traderId, stockId, quantity);
    }

    @PostMapping("/sell")
    public Transaction sellStock(@RequestParam Long traderId, @RequestParam Long stockId, @RequestParam int quantity) {
        return transactionService.sellStock(traderId, stockId, quantity);
    }

    @GetMapping("/trader/{traderId}")
    public List<Transaction> getTraderTransactions(@PathVariable Long traderId) {
        return transactionService.getTransactionsByTrader(traderId);
    }
}

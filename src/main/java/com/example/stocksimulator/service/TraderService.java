package com.example.stocksimulator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stocksimulator.exception.BadRequestException;
import com.example.stocksimulator.model.Trader;
import com.example.stocksimulator.repository.TraderRepository;
import com.example.stocksimulator.repository.UserRepository;

@Service
// DESIGN PRINCIPLE: Single Responsibility Principle (SRP)
// TraderService is focused only on trader domain operations.
public class TraderService {

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private UserRepository userRepository;

    public Trader saveTrader(Trader trader) {
        if (userRepository.existsByEmailIgnoreCase(trader.getEmail())) {
            throw new BadRequestException("User already exists!");
        }
        return traderRepository.save(trader);
    }

    public Trader getTraderById(Long id) {
        return traderRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Trader not found"));
    }

    public Trader updateTrader(Trader trader) {
        return traderRepository.save(trader);
    }

    public List<Trader> getAllTraders() {
        return traderRepository.findAll();
    }
}

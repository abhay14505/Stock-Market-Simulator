package com.example.stocksimulator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stocksimulator.model.Trader;
import com.example.stocksimulator.service.TraderService;

@RestController
@RequestMapping("/traders")
public class TraderController {

    @Autowired
    private TraderService traderService;

    @PostMapping
    public Trader createTrader(@RequestBody Trader trader) {
        return traderService.saveTrader(trader);
    }

    @GetMapping
    public List<Trader> getAllTraders() {
        return traderService.getAllTraders();
    }

    @GetMapping("/{id}")
    public Trader getTrader(@PathVariable Long id) {
        return traderService.getTraderById(id);
    }
}

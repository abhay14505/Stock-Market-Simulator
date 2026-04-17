package com.example.stocksimulator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stocksimulator.model.PortfolioResponse;
import com.example.stocksimulator.service.PortfolioService;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{traderId}")
    public List<PortfolioResponse> getPortfolio(@PathVariable Long traderId) {
        return portfolioService.getPortfolio(traderId);
    }
}
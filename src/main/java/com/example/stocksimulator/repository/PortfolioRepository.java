package com.example.stocksimulator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stocksimulator.model.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByTraderId(Long traderId);

    Optional<Portfolio> findByTraderIdAndStockId(Long traderId, Long stockId);
}
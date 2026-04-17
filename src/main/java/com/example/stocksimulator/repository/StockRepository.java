package com.example.stocksimulator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stocksimulator.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
	Optional<Stock> findByNameIgnoreCase(String name);
}
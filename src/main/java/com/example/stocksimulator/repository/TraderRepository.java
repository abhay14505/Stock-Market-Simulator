package com.example.stocksimulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stocksimulator.model.Trader;

public interface TraderRepository extends JpaRepository<Trader, Long> {
}
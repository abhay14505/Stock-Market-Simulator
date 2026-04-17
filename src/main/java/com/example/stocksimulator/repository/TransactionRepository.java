package com.example.stocksimulator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stocksimulator.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTraderId(Long traderId);
}
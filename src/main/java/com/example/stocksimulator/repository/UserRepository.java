package com.example.stocksimulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stocksimulator.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmailIgnoreCase(String email);
}
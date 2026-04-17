package com.example.stocksimulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stocksimulator.exception.BadRequestException;
import com.example.stocksimulator.model.User;
import com.example.stocksimulator.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new BadRequestException("User already exists!");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }
}

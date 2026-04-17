package com.example.stocksimulator.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.model.Trader;
import com.example.stocksimulator.repository.StockRepository;
import com.example.stocksimulator.repository.TraderRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final StockRepository stockRepository;
    private final TraderRepository traderRepository;

    public DataSeeder(StockRepository stockRepository, TraderRepository traderRepository) {
        this.stockRepository = stockRepository;
        this.traderRepository = traderRepository;
    }

    @Override
    public void run(String... args) {
        seedStocks();
        seedTraders();
    }

    private void seedStocks() {
        if (stockRepository.count() > 0) {
            return;
        }
        stockRepository.save(createStock("RELIANCE", 2850.00, 500));
        stockRepository.save(createStock("TCS", 3920.00, 300));
        stockRepository.save(createStock("INFY", 1780.00, 400));
        stockRepository.save(createStock("HDFC", 1650.00, 250));
        stockRepository.save(createStock("WIPRO", 480.00, 600));
    }

    private void seedTraders() {
        if (traderRepository.count() > 0) {
            return;
        }
        traderRepository.save(createTrader("Amogh Sunil", "amogh@pes.edu", "amogh123", 100000));
        traderRepository.save(createTrader("Akshay", "akshay@pes.edu", "akshay123", 150000));
        traderRepository.save(createTrader("A Santhosh Ram", "santhosh@pes.edu", "santhosh123", 200000));
        traderRepository.save(createTrader("Abhay Rajpal", "abhay@pes.edu", "abhay123", 175000));
    }

    private Stock createStock(String name, double price, int availableQuantity) {
        Stock stock = new Stock();
        stock.setName(name);
        stock.setPrice(price);
        stock.setAvailableQuantity(availableQuantity);
        return stock;
    }

    private Trader createTrader(String name, String email, String password, double balance) {
        Trader trader = new Trader();
        trader.setName(name);
        trader.setEmail(email);
        trader.setPassword(password);
        trader.setBalance(balance);
        return trader;
    }
}

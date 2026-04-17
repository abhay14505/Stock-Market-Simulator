package com.example.stocksimulator.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.repository.StockRepository;

@Service
public class MarketSimulationService {

    private static final double MIN_PRICE = 1.00;

    private final StockRepository stockRepository;

    private final ConcurrentMap<Long, Instant> nextUpdateAtByStockId = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Double> anchorPriceByStockId = new ConcurrentHashMap<>();

    @Value("${market.simulation.enabled:true}")
    private boolean simulationEnabled;

    @Value("${market.simulation.min-interval-ms:5000}")
    private long minIntervalMs;

    @Value("${market.simulation.max-interval-ms:25000}")
    private long maxIntervalMs;

    @Value("${market.simulation.base-volatility:0.0035}")
    private double baseVolatility;

    @Value("${market.simulation.mean-reversion:0.04}")
    private double meanReversion;

    @Value("${market.simulation.shock-probability:0.003}")
    private double shockProbability;

    @Value("${market.simulation.shock-min:0.03}")
    private double shockMin;

    @Value("${market.simulation.shock-max:0.09}")
    private double shockMax;

    @Value("${market.simulation.max-step:0.12}")
    private double maxStep;

    public MarketSimulationService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Scheduled(fixedDelayString = "${market.simulation.tick-ms:1000}")
    @Transactional
    public void simulateMarketTick() {
        if (!simulationEnabled) {
            return;
        }

        List<Stock> stocks = stockRepository.findAll();
        if (stocks.isEmpty()) {
            return;
        }

        Instant now = Instant.now();
        Set<Long> activeStockIds = new HashSet<>();

        for (Stock stock : stocks) {
            Long stockId = stock.getStockId();
            activeStockIds.add(stockId);

            double currentPrice = Math.max(stock.getPrice(), MIN_PRICE);
            anchorPriceByStockId.putIfAbsent(stockId, currentPrice);

            Instant nextUpdateAt = nextUpdateAtByStockId.computeIfAbsent(stockId, id -> randomFutureInstant(now));
            if (now.isBefore(nextUpdateAt)) {
                continue;
            }

            double anchorPrice = anchorPriceByStockId.getOrDefault(stockId, currentPrice);
            double stepReturn = nextStepReturn(currentPrice, anchorPrice);
            double nextPrice = roundToPaise(Math.max(MIN_PRICE, currentPrice * (1.0 + stepReturn)));

            stock.setPrice(nextPrice);
            stockRepository.save(stock);

            // Slowly move reference price toward current market to avoid directional drift.
            double updatedAnchor = (anchorPrice * 0.98) + (nextPrice * 0.02);
            anchorPriceByStockId.put(stockId, updatedAnchor);
            nextUpdateAtByStockId.put(stockId, randomFutureInstant(now));
        }

        // Cleanup cache for deleted stocks.
        nextUpdateAtByStockId.keySet().retainAll(activeStockIds);
        anchorPriceByStockId.keySet().retainAll(activeStockIds);
    }

    private Instant randomFutureInstant(Instant now) {
        long lowerBound = Math.max(1000L, minIntervalMs);
        long upperBound = Math.max(lowerBound, maxIntervalMs);
        long delayMs = ThreadLocalRandom.current().nextLong(lowerBound, upperBound + 1);
        return now.plusMillis(delayMs);
    }

    private double nextStepReturn(double currentPrice, double anchorPrice) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Pulls the simulated price gently back toward a long-run anchor level.
        double reversion = ((anchorPrice - currentPrice) / currentPrice) * meanReversion;
        double noise = random.nextGaussian() * baseVolatility;
        double step = reversion + noise;

        // Rare shock events mimic unexpected market news.
        if (random.nextDouble() < shockProbability) {
            double shockMagnitude = shockMin + random.nextDouble() * Math.max(0.0, shockMax - shockMin);
            step += random.nextBoolean() ? shockMagnitude : -shockMagnitude;
        }

        if (step > maxStep) {
            return maxStep;
        }
        if (step < -maxStep) {
            return -maxStep;
        }
        return step;
    }

    private double roundToPaise(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

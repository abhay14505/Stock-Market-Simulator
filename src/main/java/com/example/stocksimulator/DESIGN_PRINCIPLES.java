package com.example.stocksimulator;

/**
 * DESIGN PRINCIPLES APPLIED IN THIS PROJECT
 *
 * 1. Single Responsibility Principle (SRP)
 *    - Each service class (TraderService, StockService, TransactionService, PortfolioService)
 *      handles exactly one domain entity.
 *    - TransactionFactory handles ONLY transaction object creation.
 *
 * 2. Open/Closed Principle (OCP)
 *    - TradeObserver interface allows adding new post-trade behaviors (AuditLogObserver,
 *      BalanceAlertObserver) without modifying TransactionService.
 *
 * 3. Liskov Substitution Principle (LSP)
 *    - Trader extends User. Any code that works with User also works with Trader
 *      without breaking behavior (JOINED inheritance strategy).
 *
 * 4. Dependency Inversion Principle (DIP)
 *    - TransactionService depends on TradeObserver interface, not concrete observer classes.
 *    - Spring's @Autowired injects concrete implementations at runtime.
 */
public final class DESIGN_PRINCIPLES {

    private DESIGN_PRINCIPLES() {
    }
}

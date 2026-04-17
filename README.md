# Stock Market Simulator

A full-stack Spring Boot application that simulates a stock trading environment with trader onboarding, stock management, portfolio tracking, and live-like market price movement.

## What This Project Does

This simulator allows users to:

- Register traders with duplicate-email validation
- Add new stocks and update existing stock price/quantity
- Execute BUY and SELL trades with business-rule checks
- View portfolio holdings and transaction history
- Open trader dashboards with balance, portfolio value, and net worth
- Watch stock prices change automatically in the background
- See live updates on the Stocks page with periodic auto-refresh

## Key Features

### Trader Management

- Trader registration flow
- Prevents duplicate users by email

### Stock Management

- Add stock with duplicate-name protection
- Dedicated update stock flow for price and available quantity

### Trading Engine

- Validates quantity, balance, and available stock before trades
- Updates trader balance, stock inventory, and portfolio holdings
- Stores every trade in transaction history

### Dynamic Market Simulation

- Background market engine updates prices at random intervals
- Uses realistic small moves most of the time
- Rare shock events introduce occasional larger movements

### Live UI Behavior

- Stocks page auto-polls every few seconds
- Price coloring on each refresh:
	- Green when price increases
	- Red when price decreases
	- White when price is unchanged

### Modern Interface

- Responsive dark trading-terminal style UI
- Built with Thymeleaf templates + Bootstrap + custom CSS

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Thymeleaf
- H2 Database
- Maven
- HTML, CSS, JavaScript, Bootstrap

## Project Structure

- controller: Handles HTTP requests and web routes
- service: Business logic and validations
- repository: Data access layer with JPA repositories
- model: Domain entities and response models
- facade: Simplified orchestration layer for web workflows
- observer/factory: Extensible trade-event handling and object creation

## Design Principles and Patterns

### SOLID Principles Used

- Single Responsibility Principle (SRP)
- Open/Closed Principle (OCP)
- Liskov Substitution Principle (LSP)
- Dependency Inversion Principle (DIP)

### Design Patterns Used

- Facade Pattern
- Factory Pattern
- Observer Pattern

## Run Locally

### Prerequisites

- JDK 17+ (or your configured project Java version)
- Git (optional)

### Start the app (Windows PowerShell)

1. Go to project root
2. Build

```powershell
.\mvnw.cmd clean install
```

3. Run

```powershell
.\mvnw.cmd spring-boot:run
```

4. Open in browser

```text
http://localhost:8080
```

### Start the app (macOS/Linux)

```bash
./mvnw clean install
./mvnw spring-boot:run
```

## Important Routes

### Web Pages

- /
- /web/traders
- /web/traders/new
- /web/stocks
- /web/stocks/new
- /web/stocks/{stockId}/edit
- /web/trade
- /web/portfolio/{traderId}
- /web/transactions/{traderId}
- /web/dashboard/{traderId}

### REST Endpoints

- /traders
- /stocks
- /transactions
- /users

## Configuration

Main config file:

- src/main/resources/application.properties

Includes settings for:

- datasource and JPA
- H2 console
- market simulation behavior:
	- tick frequency
	- random update intervals
	- volatility and mean reversion
	- rare shock probability and bounds

## Notes

- Stock prices are simulated continuously in the backend.
- The Stocks page reflects updates automatically through polling.
- Manual add/update stock operations are still available and independent.

## License

MIT is a good default choice for this project.

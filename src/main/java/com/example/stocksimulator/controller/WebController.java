package com.example.stocksimulator.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.stocksimulator.facade.TradingFacade;
import com.example.stocksimulator.model.PortfolioResponse;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.model.Trader;
import com.example.stocksimulator.model.Transaction;
import com.example.stocksimulator.web.TradeForm;

@Controller
@RequestMapping
public class WebController {

    private final TradingFacade tradingFacade;

    public WebController(TradingFacade tradingFacade) {
        this.tradingFacade = tradingFacade;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/web/traders")
    public String traders(Model model) {
        model.addAttribute("traders", tradingFacade.getAllTraders());
        return "traders/list";
    }

    @GetMapping("/web/traders/new")
    public String newTraderForm(Model model) {
        model.addAttribute("trader", new Trader());
        return "traders/form";
    }

    @PostMapping("/web/traders/new")
    public String createTrader(@ModelAttribute Trader trader, Model model) {
        try {
            tradingFacade.createTrader(trader);
            return "redirect:/web/traders";
        } catch (RuntimeException exception) {
            model.addAttribute("trader", trader);
            model.addAttribute("errorMessage", exception.getMessage());
            return "traders/form";
        }
    }

    @GetMapping("/web/stocks")
    public String stocks(Model model) {
        model.addAttribute("stocks", tradingFacade.getAllStocks());
        return "stocks/list";
    }

    @GetMapping("/web/stocks/new")
    public String newStockForm(Model model) {
        model.addAttribute("stock", new Stock());
        return "stocks/form";
    }

    @PostMapping("/web/stocks/new")
    public String createStock(@ModelAttribute Stock stock, Model model) {
        try {
            tradingFacade.createStock(stock);
            return "redirect:/web/stocks";
        } catch (RuntimeException exception) {
            model.addAttribute("stock", stock);
            model.addAttribute("errorMessage", exception.getMessage());
            return "stocks/form";
        }
    }

    @GetMapping("/web/stocks/{stockId}/edit")
    public String editStockForm(@PathVariable Long stockId, Model model) {
        model.addAttribute("stock", tradingFacade.getStockById(stockId));
        return "stocks/edit";
    }

    @PostMapping("/web/stocks/{stockId}/edit")
    public String editStock(@PathVariable Long stockId, @ModelAttribute Stock stock, Model model) {
        try {
            Stock existingStock = tradingFacade.getStockById(stockId);
            existingStock.setPrice(stock.getPrice());
            existingStock.setAvailableQuantity(stock.getAvailableQuantity());
            tradingFacade.updateStock(existingStock);
            return "redirect:/web/stocks";
        } catch (RuntimeException exception) {
            stock.setStockId(stockId);
            model.addAttribute("stock", stock);
            model.addAttribute("errorMessage", exception.getMessage());
            return "stocks/edit";
        }
    }

    @GetMapping("/web/trade")
    public String tradeForm(Model model) {
        model.addAttribute("tradeForm", new TradeForm());
        populateTradingData(model);
        return "trade/form";
    }

    @PostMapping("/web/trade")
    public String trade(@ModelAttribute TradeForm tradeForm, Model model) {
        populateTradingData(model);
        model.addAttribute("tradeForm", tradeForm);
        try {
            Transaction transaction = "SELL".equalsIgnoreCase(tradeForm.getType())
                    ? tradingFacade.sellStock(tradeForm.getTraderId(), tradeForm.getStockId(), tradeForm.getQuantity())
                    : tradingFacade.buyStock(tradeForm.getTraderId(), tradeForm.getStockId(), tradeForm.getQuantity());
            model.addAttribute("transaction", transaction);
            model.addAttribute("stock", tradingFacade.getStockById(transaction.getStockId()));
            model.addAttribute("trader", tradingFacade.getTraderById(transaction.getTraderId()));
        } catch (RuntimeException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        return "trade/result";
    }

    @GetMapping("/web/portfolio/{traderId}")
    public String portfolio(@PathVariable Long traderId, Model model) {
        Trader trader = tradingFacade.getTraderById(traderId);
        List<PortfolioResponse> portfolio = tradingFacade.getPortfolio(traderId);
        double totalPortfolioValue = portfolio.stream().mapToDouble(PortfolioResponse::getTotalValue).sum();
        model.addAttribute("trader", trader);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("totalPortfolioValue", totalPortfolioValue);
        return "portfolio/view";
    }

    @GetMapping("/web/transactions/{traderId}")
    public String transactions(@PathVariable Long traderId, Model model) {
        model.addAttribute("trader", tradingFacade.getTraderById(traderId));
        model.addAttribute("transactions", tradingFacade.getTransactionsByTrader(traderId));
        return "transactions/list";
    }

    @GetMapping("/web/dashboard/{traderId}")
    public String dashboard(@PathVariable Long traderId, Model model) {
        Map<String, Object> dashboard = tradingFacade.getTraderDashboard(traderId);
        model.addAttribute("trader", dashboard.get("trader"));
        model.addAttribute("portfolio", dashboard.get("portfolio"));
        model.addAttribute("transactions", dashboard.get("transactions"));
        model.addAttribute("portfolioValue", dashboard.get("portfolioValue"));
        model.addAttribute("netWorth", dashboard.get("netWorth"));
        return "dashboard/view";
    }

    private void populateTradingData(Model model) {
        model.addAttribute("traders", tradingFacade.getAllTraders());
        model.addAttribute("stocks", tradingFacade.getAllStocks());
    }
}

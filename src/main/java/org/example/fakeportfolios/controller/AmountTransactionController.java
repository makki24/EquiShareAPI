package org.example.fakeportfolios.controller;

import org.example.fakeportfolios.model.PortfolioTransaction;
import org.example.fakeportfolios.model.UserTransaction;
import org.example.fakeportfolios.service.PortfolioTransactionService;
import org.example.fakeportfolios.service.UserTransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@PreAuthorize("hasRole('ADMIN')")
public class AmountTransactionController {

    private final UserTransactionService userTransactionService;
    private final PortfolioTransactionService portfolioTransactionService;

    public AmountTransactionController(UserTransactionService userTransactionService, PortfolioTransactionService portfolioTransactionService) {
        this.userTransactionService = userTransactionService;
        this.portfolioTransactionService = portfolioTransactionService;
    }

    @GetMapping("/{portfolioId}/usertransactions")
    List<UserTransaction> getAllUserTransactions(@PathVariable long portfolioId, @RequestParam long userId) {
        return userTransactionService.findUserTransactionByUserId(portfolioId, userId);
    }

    @GetMapping("/{portfolioId}/portfoliotransactions")
    List<PortfolioTransaction> getAllPortfolioTransactions(@PathVariable long portfolioId) {
        return portfolioTransactionService.findPortfolioTransactionByPortfolioId(portfolioId);
    }
}

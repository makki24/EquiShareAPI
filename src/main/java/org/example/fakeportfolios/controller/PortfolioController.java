package org.example.fakeportfolios.controller;

import org.example.fakeportfolios.dto.PortfolioDetailResponse;
import org.example.fakeportfolios.exception.DataInconsistentException;
import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.SharesTransaction;
import org.example.fakeportfolios.model.User;
import org.example.fakeportfolios.model.UserPortfolio;
import org.example.fakeportfolios.repository.PortfolioRepository;
import org.example.fakeportfolios.repository.SharesTransactionRepository;
import org.example.fakeportfolios.repository.UserRepository;
import org.example.fakeportfolios.service.PortfolioService;
import org.example.fakeportfolios.service.UserPortfolioService;
import org.example.fakeportfolios.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/portfolios")
@PreAuthorize("hasRole('ADMIN')")
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;
    private final SharesTransactionRepository sharesTransactionRepository;
    private final PortfolioService portfolioService;
    private final UserService userService;
    private final UserPortfolioService userPortfolioService;

    public PortfolioController(PortfolioRepository portfolioRepository, SharesTransactionRepository sharesTransactionRepository, PortfolioService portfolioService, UserService userService, UserPortfolioService userPortfolioService) {
        this.portfolioRepository = portfolioRepository;
        this.sharesTransactionRepository = sharesTransactionRepository;
        this.portfolioService = portfolioService;
        this.userService = userService;
        this.userPortfolioService = userPortfolioService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Portfolio>> getPortfolios() {
        return ResponseEntity.ok(portfolioRepository.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        return ResponseEntity.ok(portfolioRepository.save(portfolio));
    }

    @PostMapping("/{portfolioId}/shares/buy")
    public ResponseEntity<?> buyShares(
            @PathVariable Long portfolioId,
            @RequestParam double buyingPrice,
            @RequestParam int qty,
            @RequestParam String displayName
    ) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();

        double totalPrice = (buyingPrice * qty);
        if (portfolio.getTotalValue() < totalPrice) {
            throw new DataInconsistentException("Insufficient funds");
        }

        SharesTransaction sharesTransaction = new SharesTransaction();
        sharesTransaction.setBuyingPrice(buyingPrice);
        sharesTransaction.setCurrentPrice(buyingPrice);
        sharesTransaction.setQty(qty);
        sharesTransaction.setPortfolio(portfolio);
        sharesTransaction.setDisplayName(displayName);
        sharesTransactionRepository.save(sharesTransaction);

        portfolio.setTotalValue(portfolio.getTotalValue() - totalPrice);

        portfolioRepository.save(portfolio);

        return ResponseEntity.ok(sharesTransaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDetailResponse> getPortfolioDetail(@PathVariable Long id) {
        PortfolioDetailResponse response = portfolioService.getPortfolioDetail(id);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{portfolioId}/liquidate")
    public ResponseEntity<Void> liquidatePortfolio(@PathVariable Long portfolioId) {
        return portfolioService.deletePortfolio(portfolioId);
    }

    @PostMapping("/{portfolioId}/user/add-user")
    public ResponseEntity<?> addUserToPortfolio(@PathVariable Long portfolioId, @RequestParam Long userId, @RequestParam double amountToAdd) {
        try {
            Portfolio updatedPortfolio = portfolioService.addUserToPortfolio(portfolioId, userId, amountToAdd);
            return ResponseEntity.ok(updatedPortfolio);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{portfolioId}/user/delete-user/{userId}")
    public ResponseEntity<Void> removeUserFromPortfolio(
            @PathVariable Long portfolioId,
            @PathVariable Long userId) {

        portfolioService.removeUserFromPortfolio(portfolioId, userId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{portfolioId}/ownershipPercentage/{userId}")
    public ResponseEntity<Double> getOwnershipPercentage(@PathVariable Long portfolioId, @PathVariable Long userId) {
        Optional<UserPortfolio> userPortfolioOpt = userPortfolioService.findByUserAndPortfolio(userId, portfolioId);

        if (userPortfolioOpt.isPresent()) {
            double percentage = userPortfolioOpt.get().getOwnershipPercentage();
            return ResponseEntity.ok(percentage);
        } else {
            return ResponseEntity.badRequest().body(0.0);
        }
    }


}

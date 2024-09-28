package org.example.fakeportfolios.service;

import org.example.fakeportfolios.dto.PortfolioDetailResponse;
import org.example.fakeportfolios.model.UserPortfolio;
import org.example.fakeportfolios.repository.UserPortfolioRepository;
import org.springframework.transaction.annotation.Transactional;
import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.User;
import org.example.fakeportfolios.repository.PortfolioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserService userService;
    private final UserPortfolioService userPortfolioService;

    public PortfolioService(PortfolioRepository portfolioRepository, UserService userService, UserPortfolioService userPortfolioService) {
        this.portfolioRepository = portfolioRepository;
        this.userService = userService;
        this.userPortfolioService = userPortfolioService;
    }

    public PortfolioDetailResponse getPortfolioDetail(Long id) {
        PortfolioDetailResponse portfolioDetailResponse = new PortfolioDetailResponse();
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(NoSuchElementException::new);
        List<UserPortfolio> userPortfolio = userPortfolioService.findByPortfolio(portfolio);
        portfolioDetailResponse.setPortfolio(portfolio);
        portfolioDetailResponse.setUserPortfolios(userPortfolio);
        return portfolioDetailResponse;
    }

    public ResponseEntity<Void> deletePortfolio(Long id) {
        portfolioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public Portfolio addUserToPortfolio(Long portfolioId, Long userId, double amountToAdd) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NoSuchElementException("Portfolio not found"));

        User user = userService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Calculate new portfolioCharge per user
        int totalUsers = portfolio.getUserPortfolios().size() + 1; // Including the new user
        double userCharge = portfolio.getPortfolioCharge() / totalUsers;

        // Subtract charge from the user's contribution
        double finalContribution = amountToAdd - userCharge;
        if (finalContribution <= 0) {
            throw new IllegalArgumentException("Contribution is too low to cover the charge");
        }

        // Add user to UserPortfolio

        userPortfolioService.addOrUpdateUserPortfolio(user, portfolio, finalContribution);

        // Update portfolio total value
        portfolio.updateTotalValue(finalContribution);

        // Update ownership percentages for all users
        userPortfolioService.updateOwnershipPercentages(portfolio);

        return portfolioRepository.save(portfolio);
    }

}

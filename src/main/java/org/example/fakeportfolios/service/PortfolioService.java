package org.example.fakeportfolios.service;

import org.example.fakeportfolios.dto.PortfolioDetailResponse;
import org.example.fakeportfolios.dto.UserPortfolioResponse;
import org.example.fakeportfolios.model.SharesTransaction;
import org.example.fakeportfolios.model.UserPortfolio;
import org.example.fakeportfolios.repository.UserPortfolioRepository;
import org.springframework.transaction.annotation.Transactional;
import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.User;
import org.example.fakeportfolios.repository.PortfolioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
        double currentValueOfPortfolio = currentShareValueOfPortfolio(portfolio);
        List<UserPortfolioResponse> userPortfolioResponses = new ArrayList<>();
        for (UserPortfolio userPortfolioUser : userPortfolio) {
            UserPortfolioResponse userPortfolioResponse = new UserPortfolioResponse();
            userPortfolioResponse.setUsername(userPortfolioUser.getUser().getUsername());
            userPortfolioResponse.setId(userPortfolioUser.getUser().getId());
            userPortfolioResponse.setDisplayName(userPortfolioUser.getUser().getDisplayName());
            userPortfolioResponse.setContributionPercentage(userPortfolioUser.getOwnershipPercentage());
            userPortfolioResponse.setCurrentValue((userPortfolioUser.getOwnershipPercentage() / 100) * currentValueOfPortfolio);
            userPortfolioResponses.add(userPortfolioResponse);
        }
        portfolioDetailResponse.setTotalShareValue(shareOnlyValueOfPortfolio(portfolio));
        portfolioDetailResponse.setShares(portfolio.getSharesTransactions());
        portfolioDetailResponse.setUserPortfolioResponses(userPortfolioResponses);
        return portfolioDetailResponse;
    }

    public ResponseEntity<Void> deletePortfolio(Long id) {
        portfolioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public void removeUserFromPortfolio(Long portfolioId, Long userId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        UserPortfolio userPortfolio = userPortfolioService.findByUserAndPortfolio(userId, portfolioId)
                .orElseThrow(() -> new RuntimeException("UserPortfolio not found"));

        // Update the portfolio's total value by subtracting the contribution of the user

        double currentValueofPortfolio = currentShareValueOfPortfolio(portfolio);
        double userCurrentValue = (userPortfolio.getOwnershipPercentage() / 100) * currentValueofPortfolio;

        portfolio.setTotalValue(portfolio.getTotalValue() - userCurrentValue);

        // Remove the user from the portfolio
        userPortfolioService.deleteUserFromPortfolio(userPortfolio);

        portfolio.getUserPortfolios().remove(userPortfolio);


        // Recalculate other users' contributions with updated charge and new contribution
        for (UserPortfolio existinguserPortfolio : portfolio.getUserPortfolios()) {

            double userOldValue = (existinguserPortfolio.getOwnershipPercentage() / 100) * currentValueofPortfolio;

            double newPercentageofUser = (userOldValue/(currentValueofPortfolio - userCurrentValue)) * 100;
            existinguserPortfolio.setOwnershipPercentage(newPercentageofUser);
        }


        portfolioRepository.save(portfolio);
    }

    double shareOnlyValueOfPortfolio(Portfolio portfolio) {
        double res = 0;
        for(SharesTransaction sharesTransaction: portfolio.getSharesTransactions()) {
            res += sharesTransaction.getCurrentPrice() * sharesTransaction.getQty();
        }
        return res;
    }

    double currentShareValueOfPortfolio(Portfolio portfolio) {

        return shareOnlyValueOfPortfolio(portfolio) + portfolio.getTotalValue();
    }


    Portfolio save(Portfolio portfolio) {
        return  portfolioRepository.save(portfolio);
    }


    Optional<Portfolio> findById(Long id) {
        return portfolioRepository.findById(id);
    }

    @Transactional
    public Portfolio addUserToPortfolio(Long portfolioId, Long userId, double amountToAdd) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NoSuchElementException("Portfolio not found"));

        User user = userService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Optional<UserPortfolio> oldUserPortfolio = userPortfolioService.findByUserAndPortfolio(userId, portfolioId);

        // Calculate new portfolioCharge per user
        double userCharge, finalContribution, currentPercentage;
        if (oldUserPortfolio.isPresent()) {
            userCharge = portfolio.getPortfolioCharge();
            finalContribution = amountToAdd;
            currentPercentage = oldUserPortfolio.get().getOwnershipPercentage();
        }
        else {
            int totalUsers = portfolio.getUserPortfolios().size() + 1; // Including the new user
            if (totalUsers > 1)
                userCharge = (portfolio.getPortfolioCharge() * (totalUsers - 1)) / totalUsers;
            else
                userCharge = portfolio.getPortfolioCharge();
            finalContribution = amountToAdd - userCharge;
            currentPercentage = 0;
        }

        double currentValueofPortfolio = currentShareValueOfPortfolio(portfolio);



        // Subtract charge from the user's contribution
        if (finalContribution <= 0) {
            throw new IllegalArgumentException("Contribution is too low to cover the charge");
        }

        double extraAdditionToPortfolio = portfolio.getUserPortfolios().size() * ( portfolio.getPortfolioCharge() - userCharge );


        // Recalculate other users' contributions with updated charge and new contribution
        for (UserPortfolio userPortfolio : portfolio.getUserPortfolios()) {

            double userCurrentValue = (userPortfolio.getOwnershipPercentage() / 100) * currentValueofPortfolio;
            userCurrentValue = userCurrentValue + portfolio.getPortfolioCharge() - userCharge;

            double newPercentageofUser = (userCurrentValue/(currentValueofPortfolio + finalContribution + extraAdditionToPortfolio)) * 100;
            userPortfolio.setOwnershipPercentage(newPercentageofUser);

        }


        // Add user to UserPortfolio
        UserPortfolio addedfolio = userPortfolioService.addOrUpdateUserPortfolio(user, portfolio, finalContribution);

        double newUserOldValue = (currentPercentage / 100) * currentValueofPortfolio;


        addedfolio.setOwnershipPercentage(((finalContribution + newUserOldValue)/(currentValueofPortfolio + finalContribution + extraAdditionToPortfolio)) * 100);

        Set<UserPortfolio> userPortfolios = portfolio.getUserPortfolios();
        userPortfolios.add(addedfolio);
        portfolio.setUserPortfolios(userPortfolios);
        portfolio.setPortfolioCharge(userCharge);

        // Update portfolio total value
        portfolio.updateTotalValue(finalContribution + extraAdditionToPortfolio);

        return portfolioRepository.save(portfolio);
    }

}

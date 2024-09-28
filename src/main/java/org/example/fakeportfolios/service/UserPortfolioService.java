package org.example.fakeportfolios.service;

import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.User;
import org.example.fakeportfolios.model.UserPortfolio;
import org.example.fakeportfolios.repository.UserPortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPortfolioService {

    private final UserPortfolioRepository userPortfolioRepository;

    public UserPortfolioService(UserPortfolioRepository userPortfolioRepository) {
        this.userPortfolioRepository = userPortfolioRepository;
    }

    public List<UserPortfolio> findByPortfolio(Portfolio portfolio) {
        return userPortfolioRepository.findUserPortfolioByPortfolioId(portfolio.getId());
    }

    public void addOrUpdateUserPortfolio(User user, Portfolio portfolio, double contributionAmount) {
        Optional<UserPortfolio> userPortfolioOpt = userPortfolioRepository.findByUserIdAndPortfolioId(user.getId(), portfolio.getId());

        UserPortfolio userPortfolio = userPortfolioOpt.orElseGet(() -> new UserPortfolio());
        userPortfolio.setUser(user);
        userPortfolio.setPortfolio(portfolio);
        userPortfolio.setContributionAmount(userPortfolio.getContributionAmount() + contributionAmount);

        userPortfolioRepository.save(userPortfolio);
    }

    public void updateOwnershipPercentages(Portfolio portfolio) {
        double totalValue = portfolio.getTotalValue();

        for (UserPortfolio userPortfolio : portfolio.getUserPortfolios()) {
            double ownershipPercentage = (userPortfolio.getContributionAmount() / totalValue) * 100;
            userPortfolio.setOwnershipPercentage(ownershipPercentage);
            userPortfolioRepository.save(userPortfolio);
        }
    }

    public Optional<UserPortfolio> findByUserAndPortfolio(Long userId, Long portfolioId) {
        return userPortfolioRepository.findByUserIdAndPortfolioId(userId, portfolioId);
    }
}

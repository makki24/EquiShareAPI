package org.example.fakeportfolios.service;

import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.SharesTransaction;
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

    public UserPortfolio addOrUpdateUserPortfolio(User user, Portfolio portfolio, double contributionAmount) {
        Optional<UserPortfolio> userPortfolioOpt = userPortfolioRepository.findByUserIdAndPortfolioId(user.getId(), portfolio.getId());

        UserPortfolio userPortfolio = userPortfolioOpt.orElseGet(() -> new UserPortfolio());
        userPortfolio.setUser(user);
        userPortfolio.setPortfolio(portfolio);
        userPortfolio.setContributionAmount(userPortfolio.getContributionAmount() + contributionAmount);
        userPortfolio.setAddedAmount(userPortfolio.getAddedAmount() + contributionAmount);

        userPortfolioRepository.save(userPortfolio);

        return userPortfolio;
    }

    public UserPortfolio save(UserPortfolio userPortfolio) {
        return userPortfolioRepository.save(userPortfolio);
    }

    public void deleteUserFromPortfolio(UserPortfolio userPortfolio) {
        userPortfolioRepository.delete(userPortfolio);
    }

    public Optional<UserPortfolio> findByUserAndPortfolio(Long userId, Long portfolioId) {
        return userPortfolioRepository.findByUserIdAndPortfolioId(userId, portfolioId);
    }
}

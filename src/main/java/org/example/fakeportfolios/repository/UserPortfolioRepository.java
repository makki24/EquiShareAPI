package org.example.fakeportfolios.repository;

import org.example.fakeportfolios.model.UserPortfolio;
import org.example.fakeportfolios.model.UserPortfolioId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, UserPortfolioId> {
    Optional<UserPortfolio> findByUserIdAndPortfolioId(Long userId, Long portfolioId);

    List<UserPortfolio> findUserPortfolioByPortfolioId(Long portfolioId);
}

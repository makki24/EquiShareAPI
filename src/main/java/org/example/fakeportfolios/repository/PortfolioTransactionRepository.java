package org.example.fakeportfolios.repository;

import org.example.fakeportfolios.model.PortfolioTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioTransactionRepository extends JpaRepository<PortfolioTransaction, Long> {
    List<PortfolioTransaction> findPortfolioTransactionByPortfolioId(Long portfolioId);
}

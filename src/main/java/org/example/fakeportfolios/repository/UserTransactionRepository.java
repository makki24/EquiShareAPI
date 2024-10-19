package org.example.fakeportfolios.repository;

import org.example.fakeportfolios.model.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, Long> {

    List<UserTransaction> findUserTransactionByUserIdAndPortfolioId(Long userId, Long portfolioId);
}

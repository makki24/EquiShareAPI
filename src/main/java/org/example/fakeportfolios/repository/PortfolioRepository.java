package org.example.fakeportfolios.repository;

import org.example.fakeportfolios.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}

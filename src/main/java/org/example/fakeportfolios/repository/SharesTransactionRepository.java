package org.example.fakeportfolios.repository;

import org.example.fakeportfolios.model.SharesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharesTransactionRepository extends JpaRepository<SharesTransaction, Long> {
}

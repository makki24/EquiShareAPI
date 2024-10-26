package org.example.fakeportfolios.service;

import org.example.fakeportfolios.model.*;
import org.example.fakeportfolios.repository.UserTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserTransactionService {

    private final UserTransactionRepository userTransactionRepository;

    public UserTransactionService(UserTransactionRepository userTransactionRepository) {
        this.userTransactionRepository = userTransactionRepository;
    }

    public List<UserTransaction> findUserTransactionByUserId(long portfolidId, long userId) {
        var res = userTransactionRepository.findUserTransactionByUserIdAndPortfolioId(userId, portfolidId);
        Collections.reverse(res);
        return res;
    }

    public void createNewUserInPortfolio(Portfolio portfolio, User user, double amount, double userUpdatedAmount) {
        UserTransaction transaction = new UserTransaction();

        transaction.setAmount(amount);
        transaction.setPortfolio(portfolio);
        transaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        transaction.setUpdatedUserAmount(userUpdatedAmount);
        transaction.setAddTransaction(true);
        transaction.setDate(LocalDateTime.now());
        transaction.setUpdatedPortfolioAmount(portfolio.getTotalValue());
        transaction.setUser(user);
        transaction.setDescription("New User added");

        saveTransaction(transaction);

    }

    public void updateChargeInPortfolio(Portfolio portfolio, User user, double amount, double userUpdatedAmount, boolean newUser) {
        UserTransaction transaction = new UserTransaction();

        transaction.setAmount(Math.abs(amount));
        transaction.setPortfolio(portfolio);
        transaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        transaction.setUpdatedUserAmount(userUpdatedAmount);
        transaction.setAddTransaction(!(amount > 0));
        transaction.setDate(LocalDateTime.now());
        transaction.setUpdatedPortfolioAmount(portfolio.getTotalValue());
        transaction.setUser(user);
        transaction.setDescription("Charge updated " + (newUser ? " because of New User" :  ""));


        saveTransaction(transaction);

    }

    public void withdrawUserAmount(Portfolio portfolio, User user, double amount, double userUpdatedAmount) {
        UserTransaction transaction = new UserTransaction();

        transaction.setAmount(amount);
        transaction.setPortfolio(portfolio);
        transaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        transaction.setUpdatedUserAmount(userUpdatedAmount);
        transaction.setAddTransaction(false);
        transaction.setDate(LocalDateTime.now());
        transaction.setUpdatedPortfolioAmount(portfolio.getTotalValue() - amount);
        transaction.setUser(user);
        saveTransaction(transaction);
    }

    public void updateShare(UserPortfolio userPortfolio, double amount, String description) {
        UserTransaction transaction = new UserTransaction();

        transaction.setAmount(Math.abs(amount));
        transaction.setAddTransaction( (amount > 0));
        transaction.setDate(LocalDateTime.now());
        transaction.setPortfolio(userPortfolio.getPortfolio());
        transaction.setUser(userPortfolio.getUser());
        transaction.setUpdatedUserAmount(userPortfolio.getContributionAmount());
        transaction.setDescription(description);

        saveTransaction(transaction);
    }

    public UserTransaction saveTransaction(UserTransaction userTransaction) {
        return userTransactionRepository.save(userTransaction);
    }

}

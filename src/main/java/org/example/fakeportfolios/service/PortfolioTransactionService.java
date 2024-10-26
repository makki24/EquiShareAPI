package org.example.fakeportfolios.service;

import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.PortfolioTransaction;
import org.example.fakeportfolios.model.SharesTransaction;
import org.example.fakeportfolios.model.User;
import org.example.fakeportfolios.repository.PortfolioTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class PortfolioTransactionService {


    private final PortfolioTransactionRepository portfolioTransactionRepository;

    PortfolioTransactionService(PortfolioTransactionRepository portfolioTransactionRepository) {
        this.portfolioTransactionRepository = portfolioTransactionRepository;
    }

    public List<PortfolioTransaction> findPortfolioTransactionByPortfolioId(long portfolioId) {
        var res = portfolioTransactionRepository.findPortfolioTransactionByPortfolioId(portfolioId);
        Collections.reverse(res);
        return res;
    }

    public void updateCharge(Portfolio portfolio, double amount) {
        PortfolioTransaction portfolioTransaction = new PortfolioTransaction();

        portfolioTransaction.setAmount(amount);
        portfolioTransaction.setPortfolio(portfolio);
        portfolioTransaction.setDate(LocalDateTime.now());
        portfolioTransaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        portfolioTransaction.setUpdatedPortfolioAmount(portfolio.getTotalValue());
        portfolioTransaction.setAddTransaction(!(amount > 0));
        portfolioTransaction.setDescription("Charge updated");

        saveTransaction(portfolioTransaction);
    }

    public void createNewUserInPortfolio(Portfolio portfolio, User user, double amount) {
        PortfolioTransaction portfolioTransaction = new PortfolioTransaction();

        portfolioTransaction.setAmount(amount);
        portfolioTransaction.setPortfolio(portfolio);
        portfolioTransaction.setDate(LocalDateTime.now());
        portfolioTransaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        portfolioTransaction.setUpdatedPortfolioAmount(portfolio.getTotalValue());
        portfolioTransaction.setAddTransaction(true);
        portfolioTransaction.setUser(user);

        portfolioTransaction.setDescription("New Amount added");

        saveTransaction(portfolioTransaction);
    }

    public void withdrawAmount(Portfolio portfolio, User user, double amount) {
        PortfolioTransaction portfolioTransaction = new PortfolioTransaction();

        portfolioTransaction.setAmount(amount);
        portfolioTransaction.setPortfolio(portfolio);
        portfolioTransaction.setDate(LocalDateTime.now());
        portfolioTransaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        portfolioTransaction.setUpdatedPortfolioAmount(portfolio.getTotalValue());
        portfolioTransaction.setAddTransaction(false);
        portfolioTransaction.setUser(user);

        portfolioTransaction.setDescription("Withdrawn amount");

        saveTransaction(portfolioTransaction);
    }

    public void updateShareQtyAndBuyPrice(Portfolio portfolio, SharesTransaction sharesTransaction, long oldQty, double OldPrice) {

        PortfolioTransaction portfolioTransaction = new PortfolioTransaction();

        double amount = sharesTransaction.getQty() * sharesTransaction.getBuyingPrice() - (oldQty * OldPrice);

        portfolioTransaction.setAmount(amount);
        portfolioTransaction.setPortfolio(portfolio);
        portfolioTransaction.setDate(LocalDateTime.now());
        portfolioTransaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        portfolioTransaction.setUpdatedPortfolioAmount(portfolio.getTotalValue());
        portfolioTransaction.setAddTransaction((sharesTransaction.getQty() * sharesTransaction.getBuyingPrice()) < (oldQty * OldPrice));
        portfolioTransaction.setDescription("Share Qty and price update by " + oldQty + " and price " + OldPrice);
        saveTransaction(portfolioTransaction);
    }

    public void sellShares(Portfolio portfolio, SharesTransaction sharesTransaction, long qty, double pricePerUnit, double charges) {


        PortfolioTransaction portfolioTransaction = new PortfolioTransaction();

        portfolioTransaction.setAmount(qty * pricePerUnit - charges);
        portfolioTransaction.setPortfolio(portfolio);
        portfolioTransaction.setDate(LocalDateTime.now());
        portfolioTransaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        portfolioTransaction.setUpdatedPortfolioAmount(portfolio.getTotalValue());
        portfolioTransaction.setShareTransaction(sharesTransaction.getDisplayName());
        portfolioTransaction.setDescription("Sold share at " + pricePerUnit + " Qty " + qty + " and charge " + charges);

        portfolioTransaction.setAddTransaction(true);
        saveTransaction(portfolioTransaction);
    }

    public void buyShares(Portfolio portfolio, SharesTransaction sharesTransaction, double amount, double charges) {
        PortfolioTransaction portfolioTransaction = new PortfolioTransaction();

        portfolioTransaction.setAmount(amount);
        portfolioTransaction.setPortfolio(portfolio);
        portfolioTransaction.setDate(LocalDateTime.now());
        portfolioTransaction.setShareTransaction(sharesTransaction.getDisplayName());
        portfolioTransaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        portfolioTransaction.setUpdatedPortfolioAmount(portfolio.getTotalValue());
        portfolioTransaction.setDescription("Bought share at " + sharesTransaction.getCurrentPrice() + " Qty " + sharesTransaction.getQty() + " and charge " + charges);

        portfolioTransaction.setAddTransaction(false);
        saveTransaction(portfolioTransaction);
    }

    public void createNewPortfolio(Portfolio portfolio) {
        PortfolioTransaction portfolioTransaction = new PortfolioTransaction();

        portfolioTransaction.setPortfolio(portfolio);
        portfolioTransaction.setDate(LocalDateTime.now());
        portfolioTransaction.setUpdatedPortfolioCharge(portfolio.getPortfolioCharge());
        portfolioTransaction.setUpdatedPortfolioAmount(portfolio.getTotalValue());
        portfolioTransaction.setDescription("New Portfolio created");

        portfolioTransaction.setAddTransaction(false);
        saveTransaction(portfolioTransaction);
    }

    public PortfolioTransaction saveTransaction(PortfolioTransaction transaction) {
        return portfolioTransactionRepository.save(transaction);
    }

}

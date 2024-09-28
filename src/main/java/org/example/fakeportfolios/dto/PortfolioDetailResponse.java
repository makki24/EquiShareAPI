package org.example.fakeportfolios.dto;

import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.SharesTransaction;
import org.example.fakeportfolios.model.UserPortfolio;

import java.util.List;

public class PortfolioDetailResponse {
    private Portfolio portfolio;
    private List<SharesTransaction> shares;
    private List<UserPortfolio> userPortfolios;

    // Getters and setters

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<SharesTransaction> getShares() {
        return shares;
    }

    public void setShares(List<SharesTransaction> shares) {
        this.shares = shares;
    }

    public List<UserPortfolio> getUserPortfolios() {
        return userPortfolios;
    }

    public void setUserPortfolios(List<UserPortfolio> userPortfolios) {
        this.userPortfolios = userPortfolios;
    }
}

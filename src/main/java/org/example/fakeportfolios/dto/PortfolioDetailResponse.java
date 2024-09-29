package org.example.fakeportfolios.dto;

import org.example.fakeportfolios.model.Portfolio;
import org.example.fakeportfolios.model.SharesTransaction;

import java.util.ArrayList;
import java.util.List;

public class PortfolioDetailResponse {
    private Portfolio portfolio;
    private List<SharesTransaction> shares = new ArrayList<>();
    private List<UserPortfolioResponse> userPortfolios;

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

    public List<UserPortfolioResponse> getUserPortfolioResponses() {
        return userPortfolios;
    }

    public void setUserPortfolioResponses(List<UserPortfolioResponse> userPortfolios) {
        this.userPortfolios = userPortfolios;
    }
}

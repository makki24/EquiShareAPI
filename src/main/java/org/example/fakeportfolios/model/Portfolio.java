package org.example.fakeportfolios.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String displayName;
    private double totalValue;
    private double portfolioCharge;

    @OneToMany(mappedBy = "portfolio")
    @JsonBackReference
    private Set<UserPortfolio> userPortfolios = new HashSet<>();

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private Set<SharesTransaction> sharesTransactions = new HashSet<>();

    public void updateTotalValue(double amount) {
        this.totalValue += amount;
    }


    // Getters, Setters, Constructors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getPortfolioCharge() {
        return portfolioCharge;
    }

    public void setPortfolioCharge(double portfolioCharge) {
        this.portfolioCharge = portfolioCharge;
    }

    public Set<UserPortfolio> getUserPortfolios() {
        return userPortfolios;
    }

    public void setUserPortfolios(Set<UserPortfolio> userPortfolios) {
        this.userPortfolios = userPortfolios;
    }

    public Set<SharesTransaction> getSharesTransactions() {
        return sharesTransactions;
    }

    public void setSharesTransactions(Set<SharesTransaction> sharesTransactions) {
        this.sharesTransactions = sharesTransactions;
    }
}

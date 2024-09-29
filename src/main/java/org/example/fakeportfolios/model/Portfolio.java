package org.example.fakeportfolios.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @JsonManagedReference("portfolio-userPortfolio")
    private Set<UserPortfolio> userPortfolios = new HashSet<>();

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    @JsonManagedReference("portfolio-sharesTransaction")
    private List<SharesTransaction> sharesTransactions = new ArrayList<>();

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

    public List<SharesTransaction> getSharesTransactions() {
        return sharesTransactions;
    }

    public void setSharesTransactions(List<SharesTransaction> sharesTransactions) {
        this.sharesTransactions = sharesTransactions;
    }
}

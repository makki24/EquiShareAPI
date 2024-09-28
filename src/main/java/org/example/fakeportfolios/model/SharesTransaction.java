package org.example.fakeportfolios.model;

import jakarta.persistence.*;

@Entity
public class SharesTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String displayName;
    private double buyingPrice;
    private double currentPrice;
    private int qty;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;


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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

}

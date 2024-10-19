package org.example.fakeportfolios.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class  AmountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private LocalDateTime date;
    private String description;

    @Column(name = "updated_portfolio_amount")
    private double updatedPortfolioAmount;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "is_add_transaction", nullable = false)
    private boolean isAddTransaction;  // true for adding, false for withdrawing

    private double updatedPortfolioCharge;


    // Getters and Setters

    public boolean isAddTransaction() {
        return isAddTransaction;
    }

    public void setAddTransaction(boolean add) {
        this.isAddTransaction = add;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getUpdatedPortfolioAmount() {
        return updatedPortfolioAmount;
    }

    public void setUpdatedPortfolioAmount(double updatedPortfolioAmount) {
        this.updatedPortfolioAmount = updatedPortfolioAmount;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUpdatedPortfolioCharge() {
        return updatedPortfolioCharge;
    }

    public void setUpdatedPortfolioCharge(double updatedPortfolioCharge) {
        this.updatedPortfolioCharge = updatedPortfolioCharge;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

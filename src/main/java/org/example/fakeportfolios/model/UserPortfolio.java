package org.example.fakeportfolios.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_portfolio")
public class UserPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonManagedReference
    private Portfolio portfolio;

    private double contributionAmount; // Amount user contributed to the portfolio
    private double ownershipPercentage; // Percentage of the total portfolio owned by this user

    // Utility methods for setting ownership percentage and contributions
    public void setOwnershipPercentage(double percentage) {
        this.ownershipPercentage = percentage;
    }

    public void updateContributionAmount(double amount) {
        this.contributionAmount += amount;
    }

    // Add constructors, equals, hashCode if needed

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public double getContributionAmount() {
        return contributionAmount;
    }

    public void setContributionAmount(double contributionAmount) {
        this.contributionAmount = contributionAmount;
    }

    public double getOwnershipPercentage() {
        return ownershipPercentage;
    }
}

package org.example.fakeportfolios.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "user_portfolio")
public class UserPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-userPortfolio")
    private User user;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonBackReference("portfolio-userPortfolio")
    private Portfolio portfolio;

    private double contributionAmount; // Amount user contributed to the portfolio
    private double ownershipPercentage; // Percentage of the total portfolio owned by this user
    private double addedAmount;

    private double roundTo(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    // Utility methods for setting ownership percentage and contributions
    public void setOwnershipPercentage(double percentage) {
        percentage = roundTo(percentage, 6);
        if (percentage > 100) {
            throw new IllegalArgumentException("Ownership percentage cannot exceed 100%");
        }
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

    public double getAddedAmount() {
        return addedAmount;
    }

    public void setAddedAmount(double addedAmount) {
        this.addedAmount = addedAmount;
    }
}

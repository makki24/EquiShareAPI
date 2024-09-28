package org.example.fakeportfolios.dto;

public class UserPortfolioResponse {
    private String username;
    private double contributionPercentage;

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getContributionPercentage() {
        return contributionPercentage;
    }

    public void setContributionPercentage(double contributionPercentage) {
        this.contributionPercentage = contributionPercentage;
    }
}

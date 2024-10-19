package org.example.fakeportfolios.dto;

public class UserPortfolioResponse {
    private String username;
    private double contributionPercentage;
    private String displayName;
    private double currentValue;
    private double savedCurrentValue;
    private Long id; //user id

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getSavedCurrentValue() {
        return savedCurrentValue;
    }

    public void setSavedCurrentValue(double savedCurrentValue) {
        this.savedCurrentValue = savedCurrentValue;
    }
}

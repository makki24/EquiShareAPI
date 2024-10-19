package org.example.fakeportfolios.model;

import jakarta.persistence.Entity;

@Entity
public class PortfolioTransaction extends AmountTransaction{
    private String shareTransaction;
    private double sharePrice;
    private long quantity;
    private double shareTransactionCharge;

    public String getShareTransaction() {
        return shareTransaction;
    }

    public void setShareTransaction(String shareTransaction) {
        this.shareTransaction = shareTransaction;
    }

    public double getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getShareTransactionCharge() {
        return shareTransactionCharge;
    }

    public void setShareTransactionCharge(double shareTransactionCharge) {
        this.shareTransactionCharge = shareTransactionCharge;
    }
}

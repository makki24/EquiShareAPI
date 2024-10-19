package org.example.fakeportfolios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserTransaction extends AmountTransaction {
    private double updatedUserAmount;

    public double getUpdatedUserAmount() {
        return updatedUserAmount;
    }

    public void setUpdatedUserAmount(double updatedUserAmount) {
        this.updatedUserAmount = updatedUserAmount;
    }

}

package org.example.fakeportfolios.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserPortfolioId implements Serializable {

    private Long userId;
    private Long portfolioId;

    // Getters, setters, constructors, equals, and hashCode methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPortfolioId that = (UserPortfolioId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(portfolioId, that.portfolioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, portfolioId);
    }
}

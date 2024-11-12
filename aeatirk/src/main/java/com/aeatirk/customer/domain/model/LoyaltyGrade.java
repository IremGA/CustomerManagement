package com.aeatirk.customer.domain.model;

public enum LoyaltyGrade {
    BRONZE, SILVER, GOLD, PLATINUM;

    public static LoyaltyGrade fromLoyaltyPoints(int points) {
        if (points >= 0 && points < 100) return BRONZE;
        if (points >= 100 && points < 500) return SILVER;
        if (points >= 500 && points < 1000) return GOLD;
        if (points >= 1000) return PLATINUM;
        throw new IllegalArgumentException("Invalid points: " + points);
    }
}

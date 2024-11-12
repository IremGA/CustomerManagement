package com.aeatirk.customer.util;

public class LoyaltyGradeMapper {

    public static String calculateLoyaltyGrade(int loyaltyPoints) {
        if (loyaltyPoints < 100) {
            return "BRONZE";
        } else if (loyaltyPoints <= 499) {
            return "SILVER";
        } else if (loyaltyPoints <= 999) {
            return "GOLD";
        } else {
            return "PLATINUM";
        }
    }
}

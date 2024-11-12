package com.aeatirk.customer.constant;

public class Constants {

    public static final String BRONZE = "BRONZE";
    public static final String SILVER = "SILVER";
    public static final String GOLD = "GOLD";
    public static final String PLATINUM = "PLATINUM";

    public static final String SEARCH_QUERY =  "SELECT c FROM Customer c JOIN c.account a WHERE " +
            "CASE " +
            "WHEN a.loyaltyPoints < 100 THEN 'BRONZE' " +
            "WHEN a.loyaltyPoints BETWEEN 100 AND 499 THEN 'SILVER' " +
            "WHEN a.loyaltyPoints BETWEEN 500 AND 999 THEN 'GOLD' " +
            "WHEN a.loyaltyPoints >= 1000 THEN 'PLATINUM' " +
            "END = :grade";
}

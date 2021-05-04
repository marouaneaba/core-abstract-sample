package com.example.demo;

/**
 * Centralizes all endpoints available for REST APIs.
 * Used in the definition of APIs
 *
 */
public class ShopApiEndpoints {

    private ShopApiEndpoints() {
        throw new AssertionError("Utility class must not be instantiated.");
    }

    public static final String ROOT = "/";

    public static final String SHOES = "/shoes";
    public static final String SEARCH = "/search";
    public static final String SHOES_SEARCH = SHOES.concat(SEARCH);

    public static final String STOCK = "/stock";
    public static final String STOCK_SHOES = "/stock/shoes";
}

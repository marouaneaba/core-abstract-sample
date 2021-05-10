package com.example.demo;

import com.example.demo.error.ErrorMessage;

/**
 * Centralizes all endpoints available for REST APIs.
 * Used in the definition of APIs
 *
 */
public class ShopApiEndpoints {

    private ShopApiEndpoints() {
    throw new IllegalCallerException(
        ErrorMessage.SHOP_API_ENNDPOINTS_INSTANTIATE_ERROR.getDescription());
    }

    public static final String ROOT = "/";

    public static final String SHOES = "/shoes";
    public static final String SEARCH = "/search";
    public static final String SHOES_SEARCH = SHOES.concat(SEARCH);

    public static final String STOCK = "/stock";
    public static final String STOCK_SHOES = "/stock/shoes";
}

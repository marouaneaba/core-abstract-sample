package com.example.demo;


/**
 * <p>
 * <p>
 * Centralise l'ensemble des endpoints disponibles pour les apis RESTS.
 * Utilisée dans
 * <ul>
 * <li>la définition des apis</li>
 * <li>les tests</li>
 * </ul>
 * </p>
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
}

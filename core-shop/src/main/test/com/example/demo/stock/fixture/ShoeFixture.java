package com.example.demo.stock.fixture;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.shoe.entity.Shoe;

import java.util.List;

public class ShoeFixture {

    public static List<Shoe> createStockFull() {
        Shoe shoeBlack = Shoe.builder()
                .color(ShoeFilter.Color.BLACK)
                .size(22)
                .quantity(0)
                .build();

        Shoe shoeBlue = Shoe.builder().
                color(ShoeFilter.Color.BLUE).
                size(6)
                .quantity(10)
                .build();

        return List.of(shoeBlue, shoeBlue, shoeBlack, shoeBlack, shoeBlue);
    }

    public static List<Shoe> createStockEmpty() {
        Shoe shoeBlack = Shoe.builder()
                .color(ShoeFilter.Color.BLACK)
                .size(22)
                .quantity(0)
                .build();

        Shoe shoeBlue = Shoe.builder()
                .color(ShoeFilter.Color.BLUE)
                .size(6)
                .quantity(0)
                .build();

        return List.of(shoeBlue, shoeBlue, shoeBlack, shoeBlack, shoeBlue);
    }

    public static List<Shoe> createStockSome() {
        Shoe shoeBlack = Shoe.builder()
                .color(ShoeFilter.Color.BLACK)
                .size(22)
                .quantity(1)
                .build();

        Shoe shoeBlue = Shoe.builder()
                .color(ShoeFilter.Color.BLUE)
                .quantity(2)
                .size(6).build();

        return List.of(shoeBlue, shoeBlue, shoeBlack, shoeBlack, shoeBlue);
    }
}

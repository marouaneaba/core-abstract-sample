package com.example.demo.stock.fixture;

import com.example.demo.dto.shoe.Color;
import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.shoe.entity.Shoe;
import com.example.demo.stock.entity.StockMeasure;

import java.util.List;
import java.util.stream.Collectors;

public class StockFixture {

  public static Stock createStockItems() {
    List<StockMeasure> stockMeasures = StockFixture.createStockSome();
    List<StockItem> stockItems =
        stockMeasures.stream()
            .map(
                stockMeasure -> {
                  return StockItem.builder()
                      .name(stockMeasure.getShoe().getName())
                      .color(stockMeasure.getShoe().getColor())
                      .size(stockMeasure.getShoe().getSize())
                      .quantity(stockMeasure.getQuantity())
                      .build();
                })
            .collect(Collectors.toList());

    return Stock.builder().shoes(stockItems).build();
  }

  public static List<StockMeasure> createStockFull() {
    StockMeasure stockMeasureBlack =
        StockMeasure.builder()
            .shoe(Shoe.builder().color(Color.BLACK).size(22).build())
            .quantity(0)
            .build();

    StockMeasure stockMeasureBlue =
        StockMeasure.builder()
            .shoe(Shoe.builder().color(Color.BLUE).size(6).build())
            .quantity(10)
            .build();

    return List.of(
        stockMeasureBlue, stockMeasureBlue, stockMeasureBlack, stockMeasureBlack, stockMeasureBlue);
  }

  public static List<StockMeasure> createStockEmpty() {
    StockMeasure stockMeasureBlack =
        StockMeasure.builder()
            .shoe(Shoe.builder().color(Color.BLACK).size(22).build())
            .quantity(0)
            .build();

    StockMeasure stockMeasureBlue =
        StockMeasure.builder()
            .shoe(Shoe.builder().color(Color.BLUE).size(6).build())
            .quantity(0)
            .build();

    return List.of(
        stockMeasureBlue, stockMeasureBlue, stockMeasureBlack, stockMeasureBlack, stockMeasureBlue);
  }

  public static List<StockMeasure> createStockSome() {
    StockMeasure stockMeasureBlack =
        StockMeasure.builder()
            .shoe(Shoe.builder().color(Color.BLACK).size(22).build())
            .quantity(1)
            .build();

    StockMeasure stockMeasureBlue =
        StockMeasure.builder()
            .shoe(Shoe.builder().color(Color.BLUE).size(6).build())
            .quantity(2)
            .build();

    return List.of(
        stockMeasureBlue, stockMeasureBlue, stockMeasureBlack, stockMeasureBlack, stockMeasureBlue);
  }
}

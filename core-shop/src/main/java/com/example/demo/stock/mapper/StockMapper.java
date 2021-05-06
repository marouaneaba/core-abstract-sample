package com.example.demo.stock.mapper;

import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.shoe.entity.Shoe;
import com.example.demo.stock.entity.StockMeasure;

public class StockMapper {

  private StockMapper() {}

  public static StockItem toStockItem(StockMeasure stockMeasure) {
    Shoe shoe = stockMeasure.getShoe();
    return StockItem.builder()
        .id(stockMeasure.getId())
        .name(shoe.getName())
        .color(shoe.getColor())
        .size(shoe.getSize())
        .quantity(stockMeasure.getQuantity())
        .build();
  }

  public static StockMeasure toStockMeasure(StockItem stockItem) {
    Shoe shoe =
        Shoe.builder()
            .id(stockItem.getId())
            .name(stockItem.getName())
            .color(stockItem.getColor())
            .size(stockItem.getSize())
            .build();
    return StockMeasure.builder().shoe(shoe).quantity(stockItem.getQuantity()).build();
  }
}

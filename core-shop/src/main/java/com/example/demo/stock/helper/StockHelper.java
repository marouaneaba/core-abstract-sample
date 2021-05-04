package com.example.demo.stock.helper;

import com.example.demo.shoe.entity.Shoe;
import com.example.demo.stock.entity.StockMeasure;
import org.springframework.stereotype.Component;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StockHelper {

  public Map<Shoe, IntSummaryStatistics> computeSumShoesQuantityGroupByColorAndSize(
      List<StockMeasure> stockMeasures) {
    return stockMeasures.stream()
        .collect(
            Collectors.groupingBy(
                stockMeasure -> this.buildShoe(stockMeasure),
                Collectors.summarizingInt(StockMeasure::getQuantity)));
  }

  private Shoe buildShoe(StockMeasure stockMeasure) {
    return Shoe.builder()
        .color(stockMeasure.getShoe().getColor())
        .size(stockMeasure.getShoe().getSize())
        .build();
  }
}

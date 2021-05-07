package com.example.demo.stock.creator;

import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.stock.entity.StockMeasure;
import com.example.demo.stock.helper.StockHelper;
import com.example.demo.utils.LongUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class StockCreator {

  private StockHelper stockHelper;

    public Stock createStock(List<StockItem> stockItem, Stock.State state) {
        return Stock.builder()
                .state(state)
                .shoes(stockItem)
                .build();
    }

    public List<StockItem> createStock(List<StockMeasure> stockMeasures) {
        List<StockItem> stockOutlines = new ArrayList<>();
        this.stockHelper
                .computeSumShoesQuantityGroupByColorAndSize(stockMeasures)
                .forEach(
                        (shoe, intSummaryStatistics) ->
                            stockOutlines.add(StockItem.builder()
                                    .color(shoe.getColor())
                                    .size(shoe.getSize())
                                    .quantity(LongUtils.convertToInteger(intSummaryStatistics.getSum()))
                                    .build())
                        );
        return stockOutlines;
    }
}

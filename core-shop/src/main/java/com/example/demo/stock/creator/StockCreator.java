package com.example.demo.stock.creator;

import com.example.demo.dto.out.stock.Stock;
import com.example.demo.dto.out.stock.StockOutline;
import com.example.demo.shoe.entity.Shoe;
import com.example.demo.stock.helper.StockHelper;
import com.example.demo.utils.LongUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class StockCreator {

    private StockHelper stockHelper;

    public Stock createStock(List<StockOutline> stockOutlines, Stock.State state) {
        return Stock.builder()
                .state(state)
                .shoes(stockOutlines)
                .build();
    }

    public List<StockOutline> createStockOutline(List<Shoe> shoes) {
        List<StockOutline> stockOutlines = new ArrayList<>();
        this.stockHelper
                .computeSumShoesQuantityGroupByColorAndSize(shoes)
                .forEach(
                        (shoe, intSummaryStatistics) ->
                            stockOutlines.add(StockOutline.builder()
                                    .color(shoe.getColor())
                                    .size(shoe.getSize())
                                    .quantity(LongUtil.convertToInteger(intSummaryStatistics.getSum()))
                                    .build())
                        );
        return stockOutlines;
    }
}

package com.example.demo.stock.helper;

import com.example.demo.shoe.entity.Shoe;
import org.springframework.stereotype.Component;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class StockHelper {

    public Map<Shoe, IntSummaryStatistics> computeSumShoesQuantityGroupByColorAndSize(List<Shoe> shoes) {
        return shoes.stream()
                .collect(
                        Collectors.groupingBy(
                                shoe -> Shoe.builder().color(shoe.getColor()).size(shoe.getSize()).build(),
                                Collectors.summarizingInt(Shoe::getQuantity)));
    }
}

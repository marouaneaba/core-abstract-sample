package com.example.demo.stock.domain;


import com.example.demo.core.AbstractStockCore;
import com.example.demo.core.Implementation;
import com.example.demo.dto.out.stock.Stock;
import com.example.demo.dto.out.stock.StockOutline;
import com.example.demo.shoe.repository.ShoeRepository;
import com.example.demo.stock.creator.StockCreator;
import lombok.AllArgsConstructor;

import java.util.List;

@Implementation(version = 3)
@AllArgsConstructor
public class StockDomain extends AbstractStockCore {

  private static final int STOCK_MIN_CAPACITE = 0;
  private static final int STOCK_MAX_CAPACITE = 30;

  private StockCreator stockCreator;

  private ShoeRepository shoeRepository;


  @Override
  public Stock getStock() {
    List<StockOutline> stockOutlines = this.stockCreator.createStockOutline(this.shoeRepository.findAll());
    return this.stockCreator.createStock(stockOutlines, this.computeState(stockOutlines));
  }

  private Stock.State computeState(List<StockOutline> stock) {
    switch (this.computeSumShoesQuantity(stock)) {
      case STOCK_MIN_CAPACITE:
        return Stock.State.EMPTY;
      case STOCK_MAX_CAPACITE:
        return Stock.State.FULL;
      default:
        return Stock.State.SOME;
    }
  }

  private Integer computeSumShoesQuantity(List<StockOutline> stock){
    return stock.stream()
            .mapToInt(StockOutline::getQuantity)
            .sum();
  }
}

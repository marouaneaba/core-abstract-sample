package com.example.demo.stock.domain;

import com.example.demo.core.AbstractStockCore;
import com.example.demo.core.Implementation;
import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.stock.creator.StockCreator;
import com.example.demo.stock.entity.StockMeasure;
import com.example.demo.stock.repository.StockMeasureRepository;
import lombok.AllArgsConstructor;

import java.util.List;

import static com.example.demo.utils.ShopConstant.STOCK_MAX_CAPACITE;
import static com.example.demo.utils.ShopConstant.STOCK_MIN_CAPACITE;

@Implementation(version = 3)
@AllArgsConstructor
public class StockDomain extends AbstractStockCore {

  private StockCreator stockCreator;

  private StockMeasureRepository stockMeasureRepository;


  @Override
  public Stock getStock() {
    List<StockMeasure> stockMeasures = this.stockMeasureRepository.findAll();
    List<StockItem> stock = this.stockCreator.createStock(stockMeasures);
    return this.stockCreator.createStock(stock, this.computeState(stockMeasures));
  }

  private Stock.State computeState(List<StockMeasure> stockMeasures) {
    switch (this.computeSumQuantity(stockMeasures)) {
      case STOCK_MIN_CAPACITE:
        return Stock.State.EMPTY;
      case STOCK_MAX_CAPACITE:
        return Stock.State.FULL;
      default:
        return Stock.State.SOME;
    }
  }

  private Integer computeSumQuantity(List<StockMeasure> stockMeasures) {
    return stockMeasures.stream().mapToInt(StockMeasure::getQuantity).sum();
  }
}

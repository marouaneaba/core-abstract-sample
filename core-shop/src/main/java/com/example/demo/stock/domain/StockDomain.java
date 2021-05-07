package com.example.demo.stock.domain;

import com.example.demo.core.AbstractStockCore;
import com.example.demo.core.Implementation;
import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.exception.StockCapacityException;
import com.example.demo.exception.StockShoesDuplicationException;
import com.example.demo.stock.creator.StockCreator;
import com.example.demo.stock.entity.StockMeasure;
import com.example.demo.stock.mapper.StockMapper;
import com.example.demo.stock.repository.StockMeasureRepository;
import com.example.demo.utils.ListUtils;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Implementation(version = 3)
@AllArgsConstructor
public class StockDomain extends AbstractStockCore {

  private static final int STOCK_MIN_CAPACITE = 0;
  private static final int STOCK_MAX_CAPACITE = 30;

  private StockCreator stockCreator;

  private StockMeasureRepository stockMeasureRepository;

  @Override
  public Stock getStock() {
    List<StockMeasure> stockMeasures = this.stockMeasureRepository.findAll();
    List<StockItem> stock = this.stockCreator.createStock(stockMeasures);
    return this.stockCreator.createStock(stock, this.computeState(stockMeasures));
  }

  private Stock.State computeState(List<StockMeasure> stockMeasures) {
    switch (this.computeStockSumQuantity(stockMeasures)) {
      case STOCK_MIN_CAPACITE:
        return Stock.State.EMPTY;
      case STOCK_MAX_CAPACITE:
        return Stock.State.FULL;
      default:
        return Stock.State.SOME;
    }
  }

  private Integer computeStockSumQuantity(List<StockMeasure> stockMeasures) {
    return stockMeasures.stream().mapToInt(StockMeasure::getQuantity).sum();
  }

  @Override
  public void patch(StockItem stockItem) {
    this.checkStockAvailableSpace(stockItem);
    this.stockMeasureRepository
        .findByShoe_ColorAndShoe_NameAndShoe_Size(
            stockItem.getColor(), stockItem.getName(), stockItem.getSize())
        .ifPresentOrElse(
            stockMeasure -> {
              stockMeasure.addQuantity(stockItem.getQuantity());
              this.stockMeasureRepository.save(stockMeasure);
            },
            () -> this.stockMeasureRepository.save(StockMapper.toStockMeasure(stockItem)));
  }

  private void checkStockAvailableSpace(StockItem stockItem) {
    Integer quantityUsed = this.computeStockSumQuantity(this.stockMeasureRepository.findAll());
    this.checkStockCapacityAllowed(stockItem.getQuantity() + quantityUsed);
  }

  private void checkStockCapacityAllowed(Integer stockQuantity) throws StockCapacityException {
    if (stockQuantity > STOCK_MAX_CAPACITE) {
      throw new StockCapacityException(
          String.format("Stock capacity limited of %d shoes.", STOCK_MAX_CAPACITE));
    }
  }

  @Override
  public void patch(Stock stock) {
    List<StockMeasure> stockMeasures =
        stock.getShoes().stream()
            .map(stockItem -> StockMapper.toStockMeasure(stockItem))
            .collect(Collectors.toList());
    this.checkShoesDuplicate(stockMeasures);
    this.stockMeasureRepository.deleteAll();
    this.stockMeasureRepository.saveAll(stockMeasures);
  }

  private void checkShoesDuplicate(List<StockMeasure> stockMeasures) {
    if (ListUtils.areUnique(stockMeasures)) {
      throw new StockShoesDuplicationException("The collection contains a duplication of shoe.");
    }
  }
  
}

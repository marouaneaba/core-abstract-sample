package com.example.demo.stock.domain;

import com.example.demo.core.AbstractStockCore;
import com.example.demo.core.Implementation;
import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.error.ErrorMessage;
import com.example.demo.exception.StockCapacityException;
import com.example.demo.exception.StockShoesDuplicationException;
import com.example.demo.stock.creator.StockCreator;
import com.example.demo.stock.entity.StockMeasure;
import com.example.demo.stock.mapper.StockMapper;
import com.example.demo.stock.repository.StockMeasureRepository;
import com.example.demo.utils.ListUtils;
import com.example.demo.utils.ShopConstant;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    switch (this.computeStockSumQuantity(stockMeasures)) {
      case ShopConstant.STOCK_MIN_CAPACITE:
        return Stock.State.EMPTY;
      case ShopConstant.STOCK_MAX_CAPACITE:
        return Stock.State.FULL;
      default:
        return Stock.State.SOME;
    }
  }

  private Integer computeStockSumQuantity(List<StockMeasure> stockMeasures) {
    return stockMeasures.stream().mapToInt(StockMeasure::getQuantity).sum();
  }

  @Override
  public void patch(StockItem stockItem) throws StockCapacityException {
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

  private void checkStockAvailableSpace(StockItem stockItem) throws StockCapacityException {
    Integer quantityUsed = this.computeStockSumQuantity(this.stockMeasureRepository.findAll());
    this.checkStockCapacityAllowed(stockItem.getQuantity() + quantityUsed);
  }

  private void checkStockCapacityAllowed(Integer stockQuantity) throws StockCapacityException {
    if (stockQuantity > ShopConstant.STOCK_MAX_CAPACITE) {
      throw new StockCapacityException(ErrorMessage.STOCK_CAPACITY_ERROR.getDescription());
    }
  }

  @Override
  public void patch(Stock stock) throws StockShoesDuplicationException {
    List<StockMeasure> stockMeasures =
        stock.getShoes().stream()
            .map(StockMapper::toStockMeasure)
            .collect(Collectors.toList());
    this.checkShoesDuplicate(stockMeasures);
    this.stockMeasureRepository.deleteAll();
    this.stockMeasureRepository.saveAll(stockMeasures);
  }

  private void checkShoesDuplicate(List<StockMeasure> stockMeasures) throws StockShoesDuplicationException {
    if (ListUtils.areUnique(stockMeasures)) {
      throw new StockShoesDuplicationException(ErrorMessage.STOCK_SHOE_DUPLICATE_ERROR);
    }
  }
  
}

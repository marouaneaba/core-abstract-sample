package com.example.demo.stock.domain;

import com.example.demo.core.AbstractStockCore;
import com.example.demo.core.Implementation;
import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.exception.MalformedStockRequestException;
import com.example.demo.exception.StockCapacityException;
import com.example.demo.stock.creator.StockCreator;
import com.example.demo.stock.entity.StockMeasure;
import com.example.demo.stock.mapper.StockMapper;
import com.example.demo.stock.repository.StockMeasureRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;

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

  @Override
  public void patch(StockItem stockItem) {
    this.checkStockCapacity();
    this.stockMeasureRepository
        .findByShoe_ColorAndShoe_NameAndShoe_Size(
            stockItem.getColor(), stockItem.getName(), stockItem.getSize())
        .ifPresentOrElse(
            stockMeasure -> {
              stockMeasure.addQuantity(stockItem.getQuantity());
              this.saveStock(stockMeasure);
            },
            () -> this.saveStock(StockMapper.toStockMeasure(stockItem)));
  }

  private void saveStock(StockMeasure stockMeasure) throws StockCapacityException {
    this.checkStockData(stockMeasure);
    this.stockMeasureRepository.save(stockMeasure);
  }

  private void checkStockCapacity() throws StockCapacityException {
    Integer quantityUsed = this.computeSumQuantity(this.stockMeasureRepository.findAll());
    if (quantityUsed> STOCK_MAX_CAPACITE) {
      throw new StockCapacityException(
          String.format("Stock capacity limited of %d shoes.", STOCK_MAX_CAPACITE));
    }
  }

  public void checkStockData(StockMeasure stockMeasure) {
    if ( Strings.isBlank(stockMeasure.getShoe().getName())){
      throw new MalformedStockRequestException("Stock missing shoe name cannot be saved.");
    }
  }
}

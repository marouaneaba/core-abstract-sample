package com.example.demo.validation;

import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.error.ErrorMessage;
import com.example.demo.exception.StockCapacityException;
import com.example.demo.exception.StockShoesDuplicationException;
import com.example.demo.utils.ListUtils;
import com.example.demo.utils.ShopConstant;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class CollectionCapacityValidator implements ConstraintValidator<StockCapacity, Object> {

  @Override
  public void initialize(final StockCapacity collectionCapacity) {}

  @SneakyThrows
  @Override
  public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
      return this.checkStockValidate((List) o);
  }

  private boolean checkStockValidate(List<StockItem> shoes) throws StockShoesDuplicationException, StockCapacityException {
    this.checkShoesDuplicate(shoes);
    this.checkStockCapacity(shoes);
    return Boolean.TRUE;
  }

  private void checkStockCapacity(List<StockItem> shoes) throws StockCapacityException {
    int stockCapacity = shoes.stream().mapToInt(StockItem::getQuantity).sum();
    if ( stockCapacity > ShopConstant.STOCK_MAX_CAPACITE) {
      throw new StockCapacityException(ErrorMessage.STOCK_CAPACITY_ERROR.getDescription());
    }
  }

  private void checkShoesDuplicate(List<StockItem> shoes) throws StockShoesDuplicationException {
    if (ListUtils.areUnique(shoes)) {
      throw new StockShoesDuplicationException(ErrorMessage.STOCK_SHOE_DUPLICATE_ERROR);
    }
  }
}

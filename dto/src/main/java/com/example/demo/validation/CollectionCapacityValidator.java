package com.example.demo.validation;

import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.exception.StockShoesDuplicationException;
import com.example.demo.utils.ListUtils;
import com.example.demo.utils.ShopConstant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class CollectionCapacityValidator implements ConstraintValidator<StockCapacity, Object> {

  @Override
  public void initialize(final StockCapacity collectionCapacity) {}

  @Override
  public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
    try {
      List<StockItem> shoes = (List) o;
      return this.checkStockValidate(shoes);
    } catch (Exception e) {
      return true;
    }
  }

  private boolean checkStockValidate(List<StockItem> shoes) {
    this.checkShoesDuplicate(shoes);
    return this.checkStockCapacity(shoes);
  }

  private boolean checkStockCapacity(List<StockItem> shoes) {
    return shoes.stream().mapToInt(StockItem::getQuantity).sum() <= ShopConstant.STOCK_MAX_CAPACITE;
  }

  private void checkShoesDuplicate(List<StockItem> shoes) {
    if (ListUtils.areUnique(shoes)) {
      throw new StockShoesDuplicationException("The collection contains a duplication of shoe.");
    }
  }
}

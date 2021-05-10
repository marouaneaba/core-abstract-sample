package com.example.demo.core;

import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.exception.StockCapacityException;
import com.example.demo.exception.StockShoesDuplicationException;

public interface StockCore {
  Stock getStock();

  void patch(StockItem stockItem) throws StockCapacityException;

  void patch(Stock stock) throws StockShoesDuplicationException;
}

package com.example.demo.core;


import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;

public interface StockCore {
    Stock getStock();

    void patch(StockItem stockItem);
}

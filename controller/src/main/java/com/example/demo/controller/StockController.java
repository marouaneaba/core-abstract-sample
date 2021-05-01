package com.example.demo.controller;

import com.example.demo.ShopApiEndpoints;
import com.example.demo.dto.out.stock.Stock;
import com.example.demo.facade.StockFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ShopApiEndpoints.STOCK)
@RequiredArgsConstructor
public class StockController {

    private final StockFacade stockFacade;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Stock> get(@RequestHeader Integer version) {
        return ResponseEntity.ok(this.stockFacade.get(version).getStock());
    }
}

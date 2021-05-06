package com.example.demo.controller;

import com.example.demo.dto.stock.out.Stock;
import com.example.demo.facade.StockFacade;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-4T08:55:39.049Z")

@RestController
@RequiredArgsConstructor
public class StockController implements StockApi {

    private final StockFacade stockFacade;

    public ResponseEntity<Stock> get(@ApiParam(value = "The verion API", required = true)@RequestHeader Integer version) {
        return ResponseEntity.ok(this.stockFacade.get(version).getStock());
    }
}

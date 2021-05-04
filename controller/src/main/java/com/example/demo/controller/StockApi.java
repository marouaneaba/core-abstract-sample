package com.example.demo.controller;

import com.example.demo.ShopApiEndpoints;
import com.example.demo.dto.stock.out.Stock;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping(ShopApiEndpoints.STOCK)
@Api(value = "Stock", description = "the stock API", tags = {"stock"})
public interface StockApi {

    @ApiOperation(value = "returns the shoes and quantity of the stock", notes = "returns the shoe and quantity of the stock", response = Stock.class, tags = {"stock",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = Stock.class),
            @ApiResponse(code = 400, message = "Malformed request version param"),

            })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Stock> get(@ApiParam(value = "The verion API", required = true) @RequestHeader Integer version);
}

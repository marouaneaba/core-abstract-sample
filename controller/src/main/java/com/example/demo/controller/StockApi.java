package com.example.demo.controller;

import com.example.demo.ShopApiEndpoints;
import com.example.demo.dto.stock.out.Stock;
import com.example.demo.dto.stock.out.StockItem;
import com.example.demo.exception.StockCapacityException;
import com.example.demo.exception.StockShoesDuplicationException;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RequestMapping(ShopApiEndpoints.STOCK)
@Api(
    value = "Stock",
    description = "the stock API",
    tags = {"stock"})
public interface StockApi {

  @ApiOperation(
      value = "returns the shoes and quantity of the stock",
      notes = "returns the shoe and quantity of the stock",
      response = Stock.class,
      tags = {
        "stock",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successful operation", response = Stock.class),
        @ApiResponse(code = 400, message = "Malformed request version param"),
      })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Stock> get(
      @ApiParam(value = "The verion API", required = true) @RequestHeader Integer version);

  @ApiOperation(
      value = "Update the stock by submitting a stock item model",
      notes = "Update the stock item",
      response = Stock.class,
      tags = {
        "stock",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "No content"),
        @ApiResponse(
            code = 400,
            message = "Stock capacity limited of 30 shoes",
            response = String.class),
      })
  @PatchMapping(path = ShopApiEndpoints.SHOES, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void patch(
      @RequestHeader Integer version,
      @ApiParam(value = "Stock item object", required = true) @Valid @RequestBody
          StockItem stockItem)
      throws StockCapacityException;

  @ApiOperation(
      value =
          "Update the stock by submitting a big json objet containing all shoes and their quantities",
      notes = "Update the stock",
      response = Stock.class,
      tags = {
        "stock",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "No content"),
        @ApiResponse(
            code = 400,
            message =
                "Stock collection contains a duplication shoes or stock capacity greater than 30 shoes",
            response = String.class),
      })
  @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void patch(
      @RequestHeader Integer version,
      @ApiParam(value = "Stock object contains list of stock item", required = true)
          @Valid
          @RequestBody
          Stock stock)
      throws StockShoesDuplicationException;
}

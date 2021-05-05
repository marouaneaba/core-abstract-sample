package com.example.demo.exceptionhandler;

import com.example.demo.exception.MalformedStockRequestException;
import com.example.demo.exception.StockCapacityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(StockCapacityException.class)
  public ResponseEntity handleStockCapacity(StockCapacityException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(MalformedStockRequestException.class)
  public ResponseEntity handleStockRequestMalformed(MalformedStockRequestException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}

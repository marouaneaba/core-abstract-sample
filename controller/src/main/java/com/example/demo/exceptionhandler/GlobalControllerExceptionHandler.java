package com.example.demo.exceptionhandler;

import com.example.demo.exception.MalformedStockRequestException;
import com.example.demo.exception.StockCapacityException;
import com.example.demo.exception.StockShoesDuplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(StockCapacityException.class)
  public ResponseEntity handleStockCapacity(StockCapacityException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(StockShoesDuplicationException.class)
  public ResponseEntity handleStockShoesDuplication(StockShoesDuplicationException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(MalformedStockRequestException.class)
  public ResponseEntity handleStockRequestMalformed(MalformedStockRequestException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleBadHeaderParams(MethodArgumentNotValidException ex) {
    List<ObjectError> errors = ex.getBindingResult().getAllErrors();
    if (!errors.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(errors.stream().findFirst().map(error -> error.getDefaultMessage()).orElse(null));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}

package com.example.demo.exception;

public class MalformedStockRequestException extends BusinessException {

  public MalformedStockRequestException(String message) {
    super(message);
  }
}

package com.example.demo.exception;

import com.example.demo.error.ErrorMessage;

public class StockShoesDuplicationException extends BusinessException {

    public StockShoesDuplicationException(String message) {
        super(message);
    }

    public StockShoesDuplicationException(ErrorMessage message) {
        super(message.getDescription());
    }
}

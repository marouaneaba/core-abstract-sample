package com.example.demo.exceptionhandler;

import com.example.demo.exception.BusinessException;
import com.example.demo.json.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity handleBusinessException(BusinessException ex) {
    ApiError apiError =
        ApiError.builder().status(HttpStatus.BAD_REQUEST).message(ex.getLocalizedMessage()).build();
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity handleConstraintViolationException(ConstraintViolationException ex) {
    final List<String> errors = new ArrayList<>();
    for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
    }

    final ApiError apiError =
        ApiError.builder()
            .status(HttpStatus.BAD_REQUEST)
            .message(ex.getLocalizedMessage())
            .details(errors)
            .build();
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<ObjectError> errors = ex.getBindingResult().getAllErrors();
    if (!errors.isEmpty()) {
      ApiError apiError =
          ApiError.builder()
              .status(HttpStatus.BAD_REQUEST)
              .message(
                  errors.stream().findFirst().map(error -> error.getDefaultMessage()).orElse(null))
              .build();
      return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
  }
}

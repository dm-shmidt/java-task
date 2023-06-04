package com.example.demo.resource;

import com.example.demo.exception.ErrorMessage;
import com.example.demo.exception.InternalTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExHandler {
  @ExceptionHandler(InternalTransactionException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleInternalTransactionException(Exception exception) {
    return new ErrorMessage(exception.getMessage());
  }
}

package com.example.demo.resource;

import com.example.demo.api.TransactionResponse;
import com.example.demo.exception.ErrorMessage;
import com.example.demo.exception.InternalTransactionException;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExHandler {
  @ExceptionHandler({NotFoundException.class, IllegalArgumentException.class, NoSuchElementException.class})
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleException(Exception exception) {
    return new ErrorMessage(exception.getMessage());
  }

  @ExceptionHandler(InternalTransactionException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.ACCEPTED)
  public TransactionResponse handleInternalTransactionException(InternalTransactionException exception) {
    return exception.getTransactionResponse();
  }
}

package com.example.demo.resource;

import com.example.demo.exception.InternalTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExHandler {


  @ExceptionHandler(InternalTransactionException.class)
  void doNothing() {

  }
}

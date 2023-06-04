package com.example.demo.api;

import java.math.BigDecimal;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PostTransactionRequest {

  @Min(value = 0, message = "Negative amount is not allowed.")
  private BigDecimal amount;
}

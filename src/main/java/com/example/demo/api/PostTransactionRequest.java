package com.example.demo.api;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PostTransactionRequest {

  @NotNull(message = "Transaction can not be null.")
  private BigDecimal amount;
}

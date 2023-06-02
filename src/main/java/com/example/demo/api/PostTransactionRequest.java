package com.example.demo.api;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PostTransactionRequest {

  private BigDecimal amount;
}

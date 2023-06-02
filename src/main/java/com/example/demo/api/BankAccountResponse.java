package com.example.demo.api;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BankAccountResponse {
  private Long id;

  private String prefix;
  private String suffix;

  private BigDecimal balance;
  private Long subject;
}

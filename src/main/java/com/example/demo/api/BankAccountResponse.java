package com.example.demo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountResponse {
  private Long id;

  @JsonProperty(value = "account_number")
  private String accountNumber;
  private String prefix;
  private String suffix;

  private BigDecimal balance;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long subject;
}

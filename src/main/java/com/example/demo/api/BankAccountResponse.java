package com.example.demo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankAccountResponse {
  private Long id;

  private String accountNumber;
  private String prefix;
  private String suffix;

  private BigDecimal balance;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long subject;
}

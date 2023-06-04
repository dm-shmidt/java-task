package com.example.demo.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountRequest {
    private String prefix;
    private String suffix;

    private boolean applyForLoan;

    private BigDecimal balance;
}

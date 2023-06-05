package com.example.demo.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BankAccountRequest {

    @JsonProperty(value = "account_number")
    private String accountNumber;

    private String prefix;
    private String suffix;

    private BigDecimal balance;

    @JsonProperty("subject_id")
    @NotNull(message = "subject_id must not be empty")
    private Long subjectId;
}

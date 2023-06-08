package com.example.demo.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankAccountRequest {

    private String accountNumber;

    private String prefix;
    private String suffix;

    private BigDecimal balance;

    @NotNull(message = "subject_id must not be empty")
    private Long subjectId;
}

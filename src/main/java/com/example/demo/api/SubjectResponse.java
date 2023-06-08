package com.example.demo.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubjectResponse {

  private Long id;
  private String name;
  private String lastName;

  private int numberOfAccounts;
}

package com.example.demo.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateSubjectRequest {
  private String name;
  private String lastName;
  private List<BankAccountRequest> accounts;
}

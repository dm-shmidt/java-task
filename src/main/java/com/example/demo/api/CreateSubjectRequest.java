package com.example.demo.api;

import lombok.Data;

import java.util.List;

@Data
public class CreateSubjectRequest {
  private String name;
  private String givenName;
  private List<BankAccountRequest> accounts;
}

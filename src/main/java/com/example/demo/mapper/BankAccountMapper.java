package com.example.demo.mapper;

import com.example.demo.api.BankAccountRequest;
import com.example.demo.api.BankAccountResponse;
import com.example.demo.domain.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BankAccountMapper {

  @Mapping(target = "subject", source = "subject.id")
  BankAccountResponse map(BankAccount bankAccount);

  BankAccount map(BankAccountRequest bankAccountRequest);
}

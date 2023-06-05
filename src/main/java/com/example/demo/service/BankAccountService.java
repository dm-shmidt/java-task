package com.example.demo.service;

import com.example.demo.api.BankAccountRequest;
import com.example.demo.api.BankAccountResponse;
import com.example.demo.domain.BankAccount;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BankAccountService {

  Page<BankAccountResponse> findAll(Pageable pageable);
  void applyForLoan(Long subjectId);

  BankAccount findById(Long id);

  BankAccountResponse addAccount(BankAccountRequest bankAccount) throws NotFoundException;

  BankAccountRequest enrichAccountBeforeSave(BankAccountRequest account);

  int countBySubjectId(Long id);
}

package com.example.demo.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.example.demo.api.BankAccountResponse;
import com.example.demo.domain.BankAccount;
import com.example.demo.mapper.BankAccountMapper;
import com.example.demo.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

  private final BankAccountRepository bankAccountRepository;
  private final BankAccountMapper bankAccountMapper;

  @Override
  @Transactional(readOnly = true)
  public Page<BankAccountResponse> findAll(Pageable pageable) {
    return bankAccountRepository.findAll(pageable).map(bankAccountMapper::map);
  }

  @Transactional
  public void applyForLoan(Long subjectId) {
    Optional<BankAccount> bankAccount = bankAccountRepository.findBySubjectId(subjectId);
    if (bankAccount.isPresent()) {
        bankAccount.get().setApplyForLoan(true);
        if (bankAccount.get().getBalance().compareTo(BigDecimal.TEN) <= 0) {
            throw new IllegalStateException("Your request will be probably rejected due to low balance");
        }
    }
  }

    @Override
    @Transactional(readOnly = true)
    public BankAccount findById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bank account with " + id + " not found."));
    }

    @Override
    @Transactional
    public BankAccountResponse addAccount(BankAccount bankAccount) {
        return bankAccountMapper.map(bankAccountRepository.save(bankAccount));
    }
}

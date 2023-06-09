package com.example.demo.service;

import com.example.demo.api.BankAccountRequest;
import com.example.demo.api.BankAccountResponse;
import com.example.demo.client.PrefixClient;
import com.example.demo.domain.BankAccount;
import com.example.demo.domain.Subject;
import com.example.demo.mapper.BankAccountMapper;
import com.example.demo.repository.BankAccountRepository;
import com.example.demo.repository.SequenceProvider;
import com.example.demo.repository.SubjectRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final SubjectRepository subjectRepository;
    private final SequenceProvider sequenceProvider;
    private final PrefixClient prefixClient;

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
    public BankAccountResponse addAccount(BankAccountRequest bankAccountRequest) throws NotFoundException {

        Subject subject = subjectRepository.findById(bankAccountRequest.getSubjectId()).orElseThrow(
                () -> new NotFoundException("No subject with id " + bankAccountRequest.getSubjectId())
        );

        BankAccount account = bankAccountMapper.map(enrichAccountBeforeSave(bankAccountRequest));
        account.setSubject(subject);
        return bankAccountMapper.map(bankAccountRepository.save(account));
    }

    @Override
    public BankAccountRequest enrichAccountBeforeSave(BankAccountRequest account) {

        String prefix = "0300-4";

        // call prefix-client for test purpose only
        try {
            prefix = Objects.requireNonNull(prefixClient.getPrefix().getBody()).getPrefix();
        } catch (Exception ignored) {}

        account.setPrefix(prefix);
        account.setSuffix(sequenceProvider.next());
        account.setAccountNumber(UUID.randomUUID().toString());
        return account;
    }

    @Override
    public int countBySubjectId(Long id) {
        return bankAccountRepository.countBySubject_Id(id);
    }

    @Override
    @Transactional
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.saveAndFlush(bankAccount);
    }
}

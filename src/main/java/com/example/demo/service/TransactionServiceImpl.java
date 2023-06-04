package com.example.demo.service;

import com.example.demo.api.PostTransactionRequest;
import com.example.demo.api.TransactionResponse;
import com.example.demo.domain.BankAccount;
import com.example.demo.domain.Transaction;
import com.example.demo.exception.InternalTransactionException;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;
    @Override
    @Transactional
    public TransactionResponse createTransaction(Long account, PostTransactionRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InternalTransactionException("Negative amount in transaction request is not allowed.");
        }
        BankAccount bankAccount = bankAccountService.findById(account);
        if (bankAccount.getBalance().subtract(request.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InternalTransactionException("Transaction amount exceeds the balance.");
        }
        BigDecimal balance = bankAccount.getBalance().subtract(request.getAmount());
        bankAccount.setBalance(balance);
        Transaction transaction = new Transaction(account, request.getAmount(), bankAccount);
        transaction = transactionRepository.save(transaction);
        return new TransactionResponse(transaction.getId());
    }
}

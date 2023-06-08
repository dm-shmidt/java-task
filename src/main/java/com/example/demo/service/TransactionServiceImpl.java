package com.example.demo.service;

import com.example.demo.api.PostTransactionRequest;
import com.example.demo.api.TransactionResponse;
import com.example.demo.configuration.AppProps;
import com.example.demo.domain.BankAccount;
import com.example.demo.domain.Transaction;
import com.example.demo.exception.InternalTransactionException;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;
    private final AppProps appProps;

    @Override
    @Transactional(rollbackFor = Throwable.class, noRollbackFor = InternalTransactionException.class)
    public TransactionResponse createTransaction(Long account, PostTransactionRequest request) throws InternalTransactionException {

        BankAccount bankAccount = bankAccountService.findById(account);
        BigDecimal balance = bankAccount.getBalance().add(request.getAmount());

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount exceeds the balance.");
        }

        bankAccount.setBalance(balance);
        Transaction transaction = new Transaction(null, request.getAmount(), bankAccount);
        transaction = transactionRepository.saveAndFlush(transaction);

        TransactionResponse transactionResponse = new TransactionResponse(transaction.getId(), bankAccount.getId(), bankAccount.getBalance(), "");

        if (balance.compareTo(appProps.balanceThreshold()) < 0) {
            transactionResponse.setError("Balance is under threshold of " + appProps.balanceThreshold());
            throw new InternalTransactionException(transactionResponse);
        }

        return transactionResponse;
    }
}

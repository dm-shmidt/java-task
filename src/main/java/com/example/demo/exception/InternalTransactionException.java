package com.example.demo.exception;

import com.example.demo.api.TransactionResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
@Getter
public class InternalTransactionException extends Exception {

    private final TransactionResponse transactionResponse;
    public InternalTransactionException(TransactionResponse transactionResponse) {
        super(transactionResponse.getError());
        this.transactionResponse = transactionResponse;
    }
}

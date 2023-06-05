package com.example.demo.resource;

import com.example.demo.api.BankAccountRequest;
import com.example.demo.api.BankAccountResponse;
import com.example.demo.api.PostTransactionRequest;
import com.example.demo.service.BankAccountService;
import com.example.demo.service.TransactionService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Transactional
public class BankResource {

  private final BankAccountService bankAccountService;
  private final TransactionService transactionService;

  @PostMapping("/new")
  public ResponseEntity<?> addAccount(@RequestBody @Valid BankAccountRequest bankAccount, BindingResult result) throws NotFoundException {
    ResponseEntity<?> badRequestResponse = validateRequest(result);
    return badRequestResponse != null ? badRequestResponse : ResponseEntity.ok(bankAccountService.addAccount(bankAccount));
  }

  @GetMapping
  public ResponseEntity<Page<BankAccountResponse>> getAllAccounts(Pageable pageable) {
    return ResponseEntity.ok(bankAccountService.findAll(pageable));
  }

  @PostMapping("/{id}/transaction")
  public ResponseEntity<?> addTransaction(@PathVariable Long id, @Valid @RequestBody PostTransactionRequest request, BindingResult result) {
    ResponseEntity<?> badRequestResponse = validateRequest(result);
    return badRequestResponse != null ? badRequestResponse : ResponseEntity.accepted().body(transactionService.createTransaction(id, request));
  }

  @PostMapping("/{id}/applyforloan")
  public ResponseEntity<Void> applyForLoan(@PathVariable Long id) {
    bankAccountService.applyForLoan(id);
    return ResponseEntity.accepted().build();
  }

  private ResponseEntity<?> validateRequest(BindingResult result) {
    if (result != null && result.hasErrors()) {
      List<String> errorMsgs = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
      return ResponseEntity.badRequest().body(errorMsgs);
    }
    return null;
  }
}

package com.tellerapp.transactionservice.controller;

import com.tellerapp.transactionservice.entity.Transaction;
import com.tellerapp.transactionservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('Maker', 'Checker', 'Authorizer')")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    @PostMapping("/highvalue")
    @PreAuthorize("hasRole('Authorizer')")
    public ResponseEntity<Transaction> approveHighValueTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.approveHighValueTransaction(transaction));
    }

    @GetMapping("/account/{accountNumber}")
    @PreAuthorize("hasAnyRole('Checker', 'Authorizer')")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('Checker', 'Authorizer')")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCustomerId(customerId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Checker', 'Authorizer')")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}

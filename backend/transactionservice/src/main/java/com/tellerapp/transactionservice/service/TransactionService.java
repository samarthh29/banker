package com.tellerapp.transactionservice.service;

import com.tellerapp.transactionservice.entity.Transaction;
import com.tellerapp.transactionservice.repository.TransactionRepository;
import tellerapp.accountservice.entity.Account;
import tellerapp.accountservice.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountService accountService,
                              TransactionRepository transactionRepository) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        // Get account using account number instead of account ID
        Account account = accountService.getAccountByAccountNumber(transaction.getAccountNumber());

        // If account is not found, throw an exception
        if (account == null) {
            throw new IllegalArgumentException("Account not found for the given account number.");
        }

        // Set accountId in the transaction (important for database consistency)
        transaction.setAccountId(account.getId());

        // Handle "Withdrawal"
        if ("WITHDRAWAL".equalsIgnoreCase(transaction.getTransactionType())) {
            // Authorization check for withdrawals over $1000
            if (transaction.getAmount() > 1000 && transaction.getApprovedBy() == null) {
                throw new IllegalArgumentException("Withdrawals over $1000 require authorization.");
            }

            // Insufficient balance check
            if (account.getBalance() < transaction.getAmount()) {
                throw new IllegalArgumentException("Insufficient balance.");
            }

            // Deduct balance and update account
            double newBalance = account.getBalance() - transaction.getAmount();
            accountService.updateBalance(account.getAccountNumber(), newBalance);
        }

        // Handle "Deposit"
        else if ("DEPOSIT".equalsIgnoreCase(transaction.getTransactionType())) {
            // Add balance and update account
            double newBalance = account.getBalance() + transaction.getAmount();
            accountService.updateBalance(account.getAccountNumber(), newBalance);
        }

        // Set transaction date (if not already set)
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }

        // Save the transaction to the database
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}

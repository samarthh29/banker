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
        Account account = accountService.getAccountById(transaction.getAccountId());

        // Set the account number for the transaction from the Account entity
        transaction.setAccountNumber(account.getAccountNumber());

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
            account.setBalance(account.getBalance() - transaction.getAmount());
            accountService.updateAccount(account.getId(), account);
        }

        // Handle "Deposit"
        else if ("DEPOSIT".equalsIgnoreCase(transaction.getTransactionType())) {
            // Add balance and update account
            account.setBalance(account.getBalance() + transaction.getAmount());
            accountService.updateAccount(account.getId(), account);
        }

        // Set transaction date (if not already set) and save the transaction
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }

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

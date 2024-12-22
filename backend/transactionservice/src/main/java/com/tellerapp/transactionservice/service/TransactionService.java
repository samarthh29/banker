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
        // Handle "Withdrawal"
        if ("Withdrawal".equalsIgnoreCase(transaction.getTransactionType())) {
            Account account = accountService.getAccountById(transaction.getAccountId());

            // Authorization check
            if (transaction.getAmount() > 1000 && transaction.getApprovedBy() == null) {
                throw new RuntimeException("Withdrawals over $1000 require authorization.");
            }

            // Insufficient balance check
            if (account.getBalance() < transaction.getAmount()) {
                throw new RuntimeException("Insufficient balance.");
            }

            // Deduct balance and update account
            account.setBalance(account.getBalance() - transaction.getAmount());
            accountService.updateAccount(account.getId(), account);
        }

        // Handle "Deposit"
        else if ("Deposit".equalsIgnoreCase(transaction.getTransactionType())) {
            Account account = accountService.getAccountById(transaction.getAccountId());

            // Add balance and update account
            account.setBalance(account.getBalance() + transaction.getAmount());
            accountService.updateAccount(account.getId(), account);
        }

        // Set transaction date and save
        transaction.setTransactionDate(LocalDateTime.now());
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

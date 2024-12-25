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
        // Fetch the account by account number
        Account account = accountService.getAccountByAccountNumber(transaction.getAccountNumber());

        // If account is not found, throw an exception
        if (account == null) {
            throw new IllegalArgumentException("Account not found for the given account number.");
        }

        // Set accountId in the transaction to maintain database consistency
        transaction.setAccountId(account.getId());

        // Handle Withdrawal
        if ("WITHDRAWAL".equalsIgnoreCase(transaction.getTransactionType())) {
            // Withdrawals > $1000 must go through the high-value approval endpoint
            if (transaction.getAmount() > 1000) {
                throw new IllegalArgumentException("Withdrawals over $1000 require Authorizer approval.");
            }

            // Insufficient balance check
            if (account.getBalance() < transaction.getAmount()) {
                throw new IllegalArgumentException("Insufficient balance.");
            }

            // Deduct balance and update account
            double newBalance = account.getBalance() - transaction.getAmount();
            accountService.updateBalance(account.getAccountNumber(), newBalance);
        }

        // Handle Deposit
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

    @Transactional
    public Transaction approveHighValueTransaction(Transaction transaction) {
        // Fetch the account by account number
        Account account = accountService.getAccountByAccountNumber(transaction.getAccountNumber());

        // If account is not found, throw an exception
        if (account == null) {
            throw new IllegalArgumentException("Account not found for the given account number.");
        }

        // Validate transaction type
        if (!"WITHDRAWAL".equalsIgnoreCase(transaction.getTransactionType())) {
            throw new IllegalArgumentException("Only withdrawals can be approved as high-value transactions.");
        }

        // Validate transaction amount
        if (transaction.getAmount() <= 1000) {
            throw new IllegalArgumentException("This transaction does not qualify as a high-value transaction.");
        }

        // Check for sufficient balance
        if (account.getBalance() < transaction.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        // Set accountId in the transaction
        transaction.setAccountId(account.getId());

        // Deduct balance and update account
        double newBalance = account.getBalance() - transaction.getAmount();
        accountService.updateBalance(account.getAccountNumber(), newBalance);

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

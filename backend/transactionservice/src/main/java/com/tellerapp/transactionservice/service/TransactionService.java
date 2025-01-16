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

        Account account = accountService.getAccountByAccountNumber(transaction.getAccountNumber());


        if (account == null) {
            throw new IllegalArgumentException("Account not found for the given account number.");
        }

        transaction.setAccountId(account.getId());


        if ("WITHDRAWAL".equalsIgnoreCase(transaction.getTransactionType())) {
            if (transaction.getAmount() > 1000) {
                throw new IllegalArgumentException("Withdrawals over $1000 require Authorizer approval.");
            }


            if (account.getBalance() < transaction.getAmount()) {
                throw new IllegalArgumentException("Insufficient balance.");
            }

            double newBalance = account.getBalance() - transaction.getAmount();
            accountService.updateBalance(account.getAccountNumber(), newBalance);
        }


        else if ("DEPOSIT".equalsIgnoreCase(transaction.getTransactionType())) {
            double newBalance = account.getBalance() + transaction.getAmount();
            accountService.updateBalance(account.getAccountNumber(), newBalance);
        }

        // Set transaction date (if not already set)
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }


        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction approveHighValueTransaction(Transaction transaction) {

        Account account = accountService.getAccountByAccountNumber(transaction.getAccountNumber());


        if (account == null) {
            throw new IllegalArgumentException("Account not found for the given account number.");
        }


        if (!"WITHDRAWAL".equalsIgnoreCase(transaction.getTransactionType())) {
            throw new IllegalArgumentException("Only withdrawals can be approved as high-value transactions.");
        }

        if (transaction.getAmount() <= 1000) {
            throw new IllegalArgumentException("This transaction does not qualify as a high-value transaction.");
        }

        if (account.getBalance() < transaction.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        transaction.setAccountId(account.getId());

        // Deduct balance and update account
        double newBalance = account.getBalance() - transaction.getAmount();
        accountService.updateBalance(account.getAccountNumber(), newBalance);


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

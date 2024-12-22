package com.tellerapp.transactionservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId; // Reference to the Account entity

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType; // e.g., "DEPOSIT", "WITHDRAWAL", etc.

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "approved_by")
    private String approvedBy; // Optional: Name or ID of the approver for specific transactions

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    // Lifecycle callback
    @PrePersist
    public void prePersist() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }
}

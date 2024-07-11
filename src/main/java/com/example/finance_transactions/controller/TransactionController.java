package com.example.finance_transactions.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_transactions.dto.Balance;
import com.example.finance_transactions.model.Transaction;
import com.example.finance_transactions.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "API for managing financial transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public Transaction createTransaction(
            @RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping
    @Operation(summary = "Get all transactions")
    public List<Transaction> getAllTransactions(
            @RequestParam(required = false) 
            @Parameter(description = "Filter by description") String description,
            @RequestParam(required = false) 
            @Parameter(description = "Filter by payment method") String paymentMethod,
            @RequestParam(required = false) 
            @Parameter(description = "Filter by card holder name") String cardHolderName) {
        return transactionService.getAllTransactions(description, paymentMethod, cardHolderName);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public Optional<Transaction> getTransactionById(
            @PathVariable 
            @Parameter(description = "ID of the transaction to retrieve") String id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/balance")
    @Operation(summary = "Get balance by card holder name")
    public Balance getBalance(
            @RequestParam 
            @Parameter(description = "Card holder name to retrieve the balance for") String cardHolderName) {
        return transactionService.getBalance(cardHolderName);
    }
}
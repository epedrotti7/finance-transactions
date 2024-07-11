package com.example.finance_transactions.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.finance_transactions.model.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
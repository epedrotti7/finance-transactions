package com.example.finance_transactions.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.finance_transactions.dto.Balance;
import com.example.finance_transactions.model.Statement;
import com.example.finance_transactions.model.Transaction;
import com.example.finance_transactions.repository.StatementRepository;
import com.example.finance_transactions.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String DEBIT = "debit";
    private static final String CREDIT = "credit";
    private static final String PAID = "paid";
    private static final String WAITING_FUNDS = "waiting_funds";
    private static final double DEBIT_FEE = 0.97;
    private static final double CREDIT_FEE = 0.95;

    public Transaction createTransaction(Transaction transaction) {
        setMerchantCodeIfNull(transaction);
        maskCardNumber(transaction);
        transactionRepository.save(transaction);
        createAndSaveStatement(transaction);
        return transaction;
    }

    @SuppressWarnings("null")
    public List<Transaction> getAllTransactions(String description, String paymentMethod, String cardHolderName) {
        Query query = new Query();

        Boolean isPassedDescription = description != null && !description.isEmpty();
        Boolean isPassedPaymentMethod = paymentMethod != null && !paymentMethod.isEmpty();
        Boolean isPassedCardHolderName = cardHolderName != null && !cardHolderName.isEmpty();

        if (isPassedDescription) {
            query.addCriteria(Criteria.where("description").regex(description, "i"));
        }
        if (isPassedPaymentMethod) {
            query.addCriteria(Criteria.where("paymentMethod").is(paymentMethod));
        }
        if (isPassedCardHolderName) {
            query.addCriteria(Criteria.where("cardHolderName").regex(cardHolderName, "i"));
        }

        return mongoTemplate.find(query, Transaction.class);
    }

    public Optional<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    public Balance getBalance(String cardHolderName) {

        List<Statement> statements = statementRepository.findAll();
        double availableBalance = 0;
        double waitingFunds = 0;

        for (Statement statement : statements) {

            Optional<Transaction> transaction = transactionRepository.findById(statement.getTransactionId());

            Boolean transactionExists = transaction.isPresent();
            Boolean isSameCardHolderName = transaction.get().getCardHolderName().equalsIgnoreCase(cardHolderName);

            if (transactionExists && isSameCardHolderName) {

                Boolean isPaidStatus = PAID.equals(statement.getStatus());
                Boolean isWaitingFundStatus = WAITING_FUNDS.equals(statement.getStatus());

                if (isPaidStatus) {
                    availableBalance += statement.getFinalAmount();
                } else if (isWaitingFundStatus) {
                    waitingFunds += statement.getFinalAmount();
                }
            }
        }

        return new Balance(availableBalance, waitingFunds);
    }

    private void setMerchantCodeIfNull(Transaction transaction) {

        Boolean transactionMerchatCodeNotExists = transaction.getMerchantCode() == null;

        if (transactionMerchatCodeNotExists) {
            transaction.setMerchantCode(UUID.randomUUID());
        }
    }

    private void maskCardNumber(Transaction transaction) {
        String cardNumber = transaction.getCardNumber();
        transaction.setCardNumber(cardNumber.substring(cardNumber.length() - 4));
    }

    private void createAndSaveStatement(Transaction transaction) {
        Statement statement = new Statement();
        statement.setTransactionId(transaction.getId());

        Boolean transactionPaymentMethodIsDebit = DEBIT.equals(transaction.getPaymentMethod());
        Boolean transactionPaymentMethodIsCredit = CREDIT.equals(transaction.getPaymentMethod());

        if (transactionPaymentMethodIsDebit) {
            statement.setFinalAmount(transaction.getAmount() * DEBIT_FEE);
            statement.setStatus(PAID);
            statement.setPaymentDate(transaction.getCreatedAt());
        } else if (transactionPaymentMethodIsCredit) {
            statement.setFinalAmount(transaction.getAmount() * CREDIT_FEE);
            statement.setStatus(WAITING_FUNDS);
            statement.setPaymentDate(transaction.getCreatedAt().plusDays(30));
        }
        statementRepository.save(statement);
    }
}
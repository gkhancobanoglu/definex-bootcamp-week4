package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import java.util.List;
import java.util.UUID;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Transaction;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.TransactionNotFoundException;

public interface TransactionService {
	List<Transaction> findTransactions();
    Transaction findById(UUID id) throws TransactionNotFoundException;
    Transaction create(Transaction transaction);
    Transaction update(Transaction transaction) throws TransactionNotFoundException;
    void delete(UUID id) throws TransactionNotFoundException;
}

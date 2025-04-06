package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Transaction;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.TransactionRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.TransactionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;



    @Override
    public List<Transaction> findTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction findById(UUID id) throws TransactionNotFoundException {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException());
    }

    @Override 
    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) throws TransactionNotFoundException {
        if(!transactionRepository.existsById(transaction.getId())){
            throw new TransactionNotFoundException();
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public void delete(UUID id) throws TransactionNotFoundException {
        if(!transactionRepository.existsById(id)){
            throw new TransactionNotFoundException();
        }
        transactionRepository.deleteById(id);
    }
}

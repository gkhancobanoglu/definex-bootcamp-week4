package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Transaction;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    
    public TransactionController(TransactionService transactionService) {
    	this.transactionService = transactionService;
    }

    // Tüm işlemleri listeleme
    @GetMapping("/v1")
    public ResponseEntity<List<Transaction>> getTransactions() {
        List<Transaction> transactions = transactionService.findTransactions();
        if (transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(transactions);
    }

    // Id'ye göre belirli işlemi getirme
    @GetMapping("/v1/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable UUID id) {
        try{
            Transaction transaction = transactionService.findById(id);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 döndür, işlem bulunamadı
        }
    }

    // Yeni işlem oluştur
    @PostMapping("/v1")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        if(transaction.getAmount().compareTo(BigDecimal.ZERO) < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Transaction createdTransaction = transactionService.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    //İşlem Güncelle
    @PutMapping("/v1")
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction) {
       try{
           Transaction updatedTransaction = transactionService.update(transaction);
           return ResponseEntity.ok(updatedTransaction);
       }catch (TransactionNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       }
    }

    // İşlem Silme
    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        try {
            transactionService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 döndür, başarılı silme
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 döndür, işlem bulunamadı
        }
    }
}

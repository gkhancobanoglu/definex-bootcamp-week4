package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Account;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Transaction;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.TransactionType;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.TransactionRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.TransactionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private UUID transactionId;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        // Dummy Category and Account objects
        Category category = new Category(categoryId, "Food", "Groceries and meals");
        Account account = new Account(accountId, "Main Account", new BigDecimal("5000.00"), null, "123456", "Bank A");

        // Create a Transaction object
        transaction = new Transaction(
                transactionId,
                new BigDecimal("100.00"),
                "Test transaction",
                LocalDateTime.now(),
                TransactionType.DEPOSIT,
                category,
                account
        );
    }

    /**
     * ✅ Tüm işlemleri bulma testi (findTransactions)
     * - Boş liste durumu
     */
    @Test
    void shouldFindAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.findTransactions();

        assertNotNull(result);
        assertEquals(1, result.size(), "Expected 1 transaction.");
        verify(transactionRepository, times(1)).findAll();
    }

    /**
     * ✅ Transaction'ı id'ye göre bulma testi (findById)
     * - Transaction bulunduğunda
     */
    @Test
    void shouldFindTransactionById() throws TransactionNotFoundException {
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.findById(transactionId);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        verify(transactionRepository, times(1)).findById(transactionId);
    }

    /**
     * ✅ Transaction'ı id'ye göre bulma testi (findById)
     * - Transaction bulunamadığında (exception)
     */
    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.findById(transactionId));
        verify(transactionRepository, times(1)).findById(transactionId);
    }

    /**
     * ✅ Yeni transaction oluşturma testi (create)
     */
    @Test
    void shouldCreateTransaction() {
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.create(transaction);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        verify(transactionRepository, times(1)).save(transaction);
    }

    /**
     * ✅ Transaction güncelleme testi (update)
     * - Transaction var ise başarılı güncelleme
     */
    @Test
    void shouldUpdateTransaction() throws TransactionNotFoundException {
        when(transactionRepository.existsById(transactionId)).thenReturn(true);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.update(transaction);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        verify(transactionRepository, times(1)).existsById(transactionId);
        verify(transactionRepository, times(1)).save(transaction);
    }

    /**
     * ✅ Transaction güncelleme testi (update)
     * - Transaction bulunamadığında hata fırlatma
     */
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTransaction() {
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.update(transaction));
        verify(transactionRepository, times(1)).existsById(transactionId);
    }

    /**
     * ✅ Transaction silme testi (delete)
     * - Transaction var ise başarılı silme
     */
    @Test
    void shouldDeleteTransaction() throws TransactionNotFoundException {
        when(transactionRepository.existsById(transactionId)).thenReturn(true);

        transactionService.delete(transactionId);

        verify(transactionRepository, times(1)).existsById(transactionId);
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    /**
     * ✅ Transaction silme testi (delete)
     * - Transaction bulunamadığında hata fırlatma
     */
    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTransaction() {
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.delete(transactionId));
        verify(transactionRepository, times(1)).existsById(transactionId);
    }
}

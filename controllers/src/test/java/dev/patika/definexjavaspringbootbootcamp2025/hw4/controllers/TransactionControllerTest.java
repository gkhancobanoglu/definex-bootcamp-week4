package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.*;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.TransactionService;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.TransactionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private UUID transactionId;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();

        // Category nesnesini @Builder ile oluştur
        Category category = Category.builder()
                .id(UUID.randomUUID())
                .name("Test Category")
                .description("Test Description")
                .build();

        // Account nesnesini @Builder ile oluştur
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .name("Test Account")
                .balance(new BigDecimal("1000.00"))
                .type(AccountType.SAVINGS)  // Enum sınıfındaki bir değeri kullanmalısın
                .accountNumber("123456789")
                .bankName("Test Bank")
                .build();

        // Transaction nesnesini oluştur
        transaction = Transaction.builder()
                .id(transactionId)
                .amount(new BigDecimal("100.00"))
                .description("Test Transaction")
                .date(LocalDateTime.now())
                .type(TransactionType.DEPOSIT)
                .category(category)
                .account(account)
                .build();
    }


    /**
     * Test: Tüm işlemleri listeleme - Boş liste durumu
     */
    @Test
    void shouldReturnNoContentWhenNoTransactionsAvailable() {
        when(transactionService.findTransactions()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Transaction>> response = transactionController.getTransactions();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(transactionService, times(1)).findTransactions();
    }

    /**
     * Test: Tüm işlemleri listeleme - Mevcut işlemler var
     */
    @Test
    void shouldReturnTransactionsWhenAvailable() {
        when(transactionService.findTransactions()).thenReturn(List.of(transaction));

        ResponseEntity<List<Transaction>> response = transactionController.getTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(transactionService, times(1)).findTransactions();
    }

    /**
     * Test: ID'ye göre işlem getirme - Geçerli işlem var
     */
    @Test
    void shouldReturnTransactionByIdWhenExists() {
        when(transactionService.findById(transactionId)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransaction(transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionId, response.getBody().getId());
        verify(transactionService, times(1)).findById(transactionId);
    }

    /**
     * Test: ID'ye göre işlem getirme - İşlem bulunamazsa
     */
    @Test
    void shouldReturnNotFoundWhenTransactionDoesNotExist() {
        when(transactionService.findById(transactionId)).thenThrow(new TransactionNotFoundException());

        ResponseEntity<Transaction> response = transactionController.getTransaction(transactionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(transactionService, times(1)).findById(transactionId);
    }

    /**
     * Test: Geçerli işlem oluşturma
     */
    @Test
    void shouldCreateTransaction() {
        when(transactionService.create(transaction)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transactionId, response.getBody().getId());
        verify(transactionService, times(1)).create(transaction);
    }

    /**
     * Test: Geçersiz işlem oluşturma (negatif miktar)
     */
    @Test
    void shouldReturnBadRequestWhenTransactionAmountIsNegative() {
        // Negatif miktar içeren geçersiz işlem nesnesi oluştur
        Transaction invalidTransaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("-50.00"))
                .description("Invalid Transaction")
                .date(LocalDateTime.now())
                .type(TransactionType.DEPOSIT)
                .category(Category.builder()
                        .id(UUID.randomUUID())
                        .name("Test Category")
                        .description("Test Description")
                        .build()) // Builder ile kategori oluşturuldu
                .account(Account.builder()
                        .id(UUID.randomUUID())
                        .name("Test Account")
                        .balance(new BigDecimal("1000.00"))
                        .type(AccountType.SAVINGS)
                        .accountNumber("123456789")
                        .bankName("Test Bank")
                        .build()) // Builder ile hesap oluşturuldu
                .build();

        ResponseEntity<Transaction> response = transactionController.createTransaction(invalidTransaction);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(transactionService, times(0)).create(any());
    }


    /**
     * Test: İşlem güncelleme - Başarılı
     */
    @Test
    void shouldUpdateTransactionSuccessfully() {
        when(transactionService.update(transaction)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.updateTransaction(transaction);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionId, response.getBody().getId());
        verify(transactionService, times(1)).update(transaction);
    }

    /**
     * Test: İşlem güncelleme - İşlem bulunamazsa
     */
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentTransaction() {
        when(transactionService.update(transaction)).thenThrow(new TransactionNotFoundException());

        ResponseEntity<Transaction> response = transactionController.updateTransaction(transaction);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(transactionService, times(1)).update(transaction);
    }

    /**
     * Test: İşlem silme - Başarılı
     */
    @Test
    void shouldDeleteTransactionSuccessfully() {
        doNothing().when(transactionService).delete(transactionId);

        ResponseEntity<Void> response = transactionController.deleteTransaction(transactionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(transactionService, times(1)).delete(transactionId);
    }

    /**
     * Test: İşlem silme - İşlem bulunamazsa
     */
    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentTransaction() {
        doThrow(new TransactionNotFoundException()).when(transactionService).delete(transactionId);

        ResponseEntity<Void> response = transactionController.deleteTransaction(transactionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(transactionService, times(1)).delete(transactionId);
    }
}

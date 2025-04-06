package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Account;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.AccountService;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private UUID accountId;
    private Account account;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        account = new Account(accountId, "Test Account", new BigDecimal("1000.00"), null, "123456", "Bank A");
    }

    // Test: Tüm hesapları listeleme
    @Test
    void shouldGetAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);

        when(accountService.findAccounts()).thenReturn(accounts);

        ResponseEntity<List<Account>> response = accountController.getAccounts();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(accountService, times(1)).findAccounts();
    }

    // Test: Hesap bulunamadığında
    @Test
    void shouldReturnNoContentWhenNoAccounts() {
        when(accountService.findAccounts()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Account>> response = accountController.getAccounts();

        assertEquals(204, response.getStatusCodeValue());  // 204 No Content
        verify(accountService, times(1)).findAccounts();
    }

    // Test: Hesap ID'ye göre getirilmesi
    @Test
    void shouldGetAccountById() throws AccountNotFoundException {
        when(accountService.findById(accountId)).thenReturn(account);

        ResponseEntity<Account> response = accountController.getAccount(accountId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(accountId, response.getBody().getId());
        verify(accountService, times(1)).findById(accountId);
    }

    // Test: Hesap ID'ye göre bulunamaması
    @Test
    void shouldReturnNotFoundWhenAccountDoesNotExist() throws AccountNotFoundException {
        when(accountService.findById(accountId)).thenThrow(new AccountNotFoundException());

        ResponseEntity<Account> response = accountController.getAccount(accountId);

        assertEquals(404, response.getStatusCodeValue());
        verify(accountService, times(1)).findById(accountId);
    }

    // Test: Yeni hesap oluşturulması
    @Test
    void shouldCreateAccount() {
        when(accountService.create(account)).thenReturn(account);

        ResponseEntity<Account> response = accountController.createAccount(account);

        assertEquals(201, response.getStatusCodeValue()); // 201 Created
        verify(accountService, times(1)).create(account);
    }

    // Test: Geçersiz hesap bakiyesi
    @Test
    void shouldReturnBadRequestWhenBalanceIsNegative() {
        Account invalidAccount = new Account(accountId, "Invalid Account", new BigDecimal("-100.00"), null, "123457", "Bank B");

        ResponseEntity<Account> response = accountController.createAccount(invalidAccount);

        assertEquals(400, response.getStatusCodeValue());  // 400 Bad Request
        verify(accountService, times(0)).create(invalidAccount);  // create metoduna çağrı yapılmamalı
    }

    // Test: Hesap güncellenmesi
    @Test
    void shouldUpdateAccount() throws AccountNotFoundException {
        when(accountService.update(account)).thenReturn(account);

        ResponseEntity<Account> response = accountController.updateAccount(account);

        assertEquals(200, response.getStatusCodeValue());  // 200 OK
        verify(accountService, times(1)).update(account);
    }

    // Test: Hesap güncellenemediğinde (Hesap bulunamaması)
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingAccount() throws AccountNotFoundException {
        when(accountService.update(account)).thenThrow(new AccountNotFoundException());

        ResponseEntity<Account> response = accountController.updateAccount(account);

        assertEquals(404, response.getStatusCodeValue());  // 404 Not Found
        verify(accountService, times(1)).update(account);
    }

    // Test: Hesap bakiyesi alınması
    @Test
    void shouldGetAccountBalance() throws AccountNotFoundException {
        when(accountService.findById(accountId)).thenReturn(account);

        ResponseEntity<BigDecimal> response = accountController.getAccountBalance(accountId);

        assertEquals(200, response.getStatusCodeValue());  // 200 OK
        assertEquals(account.getBalance(), response.getBody());
        verify(accountService, times(1)).findById(accountId);
    }

    // Test: Hesap bakiyesi alınırken hesap bulunamadığında
    @Test
    void shouldReturnNotFoundWhenGettingBalanceOfNonExistingAccount() throws AccountNotFoundException {
        when(accountService.findById(accountId)).thenThrow(new AccountNotFoundException());

        ResponseEntity<BigDecimal> response = accountController.getAccountBalance(accountId);

        assertEquals(404, response.getStatusCodeValue());  // 404 Not Found
        verify(accountService, times(1)).findById(accountId);
    }
}

package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Account;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.AccountRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private UUID accountId;
    private Account account;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        account = Account.builder()
                .id(accountId)
                .name("Test Account")
                .balance(new BigDecimal("1000.00"))
                .accountNumber("123456")
                .bankName("Test Bank")
                .build();
    }

    /**
     *  Tüm hesapları listeleme testi (findAccounts)
     */
    @Test
    void shouldReturnAllAccounts() {
        List<Account> accounts = List.of(account);
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.findAccounts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Account", result.get(0).getName());
        verify(accountRepository, times(1)).findAll();
    }

    /**
     *  ID ile hesap bulma testi (findById)
     */
    @Test
    void shouldFindAccountById() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Account result = accountService.findById(accountId);

        assertNotNull(result);
        assertEquals(accountId, result.getId());
        verify(accountRepository, times(1)).findById(accountId);
    }

    /**
     *  ID ile hesap bulma testi (findById) - Hesap bulunamadığında hata fırlatma
     */
    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.findById(accountId));
        verify(accountRepository, times(1)).findById(accountId);
    }

    /**
     *  Yeni hesap oluşturma testi (create)
     */
    @Test
    void shouldCreateAccount() {
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.create(account);

        assertNotNull(result);
        assertEquals(accountId, result.getId());
        verify(accountRepository, times(1)).save(account);
    }

    /**
     *  Var olan hesabı güncelleme testi (update)
     */
    @Test
    void shouldUpdateAccount() {
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.update(account);

        assertNotNull(result);
        assertEquals(accountId, result.getId());
        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, times(1)).save(account);
    }

    /**
     *  Güncellenecek hesap yoksa hata fırlatma testi (update)
     */
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingAccount() {
        when(accountRepository.existsById(accountId)).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> accountService.update(account));
        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, never()).save(account);
    }

    /**
     *  Hesap bakiyesi alma testi (getBalance) - Başarılı
     */
    @Test
    void shouldGetBalanceSuccessfully() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        BigDecimal balance = accountService.getBalance(accountId);

        assertNotNull(balance);
        assertEquals(new BigDecimal("1000.00"), balance);
        verify(accountRepository, times(1)).findById(accountId);
    }

    /**
     *  Hesap bakiyesi null olduğunda hata fırlatma testi (getBalance)
     */
    @Test
    void shouldThrowExceptionWhenBalanceIsNull() {
        account.setBalance(null);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(AccountNotFoundException.class, () -> accountService.getBalance(accountId));
        verify(accountRepository, times(1)).findById(accountId);
    }
}

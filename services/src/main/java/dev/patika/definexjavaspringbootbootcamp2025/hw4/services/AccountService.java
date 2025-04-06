package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Account;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.AccountNotFoundException;

public interface AccountService {
    List<Account> findAccounts();
    Account findById(UUID id) throws AccountNotFoundException;
    Account create(Account account);
    Account update(Account account) throws AccountNotFoundException;
    BigDecimal getBalance(UUID id) throws AccountNotFoundException;
}

package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Account;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.AccountRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;


    @Override
    public List<Account> findAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(UUID id) throws AccountNotFoundException {
        return accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) throws AccountNotFoundException {
        if (!accountRepository.existsById(account.getId())) {
            throw new AccountNotFoundException();
        }
        return accountRepository.save(account);
    }

    @Override
    public BigDecimal getBalance(UUID id) throws AccountNotFoundException {
        Account account = findById(id);
        if (account.getBalance() == null) {
            throw new AccountNotFoundException();
        }
        return account.getBalance();
    }
}

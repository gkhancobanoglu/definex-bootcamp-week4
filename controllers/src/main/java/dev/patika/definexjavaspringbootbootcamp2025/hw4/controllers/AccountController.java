package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Account;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

    //Tüm hesapları getirme
	@GetMapping("/v1")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = accountService.findAccounts();
        if (accounts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(accounts);
    }

    //Id'ye göre hesap getirme
    @GetMapping("/v1/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID id) {
        try{
            Account account =  accountService.findById(id);
            return ResponseEntity.ok(account);
        } catch(AccountNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //Hesap oluşturma
    @PostMapping("/v1")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        if(account.getBalance().compareTo(BigDecimal.ZERO) < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Account createdAccount = accountService.create(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    //Hesap Güncelleme
    @PutMapping("/v1")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        try {
            Account updatedAccount = accountService.update(account);
            return ResponseEntity.ok(updatedAccount);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //Hesap bakiyesini getirme
    @GetMapping("/v1/{id}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable UUID id) {
        try {
            BigDecimal balance = accountService.findById(id).getBalance();
            return ResponseEntity.ok(balance);
        }catch (AccountNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

package dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception;

public class AccountNotFoundException extends RuntimeException {
	public AccountNotFoundException() {
		super("AccountNotFoundException");
	}	
}
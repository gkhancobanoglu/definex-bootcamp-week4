package dev.patika.definexjavaspringbootbootcamp2025.hw4.entities;

public enum AccountType {
	
	BANK("BANK"), BROKER("BROKER"), SAVINGS("SAVINGS");
	
	private final String accountType;
	
	AccountType(String type) {
		accountType = type;
	}
	
	String getType() {
		return accountType;
	}
}

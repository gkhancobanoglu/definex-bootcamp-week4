package dev.patika.definexjavaspringbootbootcamp2025.hw4.entities;

public enum TransactionType {
	DEPOSIT("DEPOSIT"), WITHDRAW("WITHDRAW");
	
	private final String transactionType;
	
	TransactionType(String type) {
		transactionType = type;
	}
	
	String getType() {
		return transactionType;
	}
}

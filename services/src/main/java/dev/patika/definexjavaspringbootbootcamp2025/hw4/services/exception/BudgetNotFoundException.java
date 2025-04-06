package dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception;

public class BudgetNotFoundException extends RuntimeException {
	public BudgetNotFoundException() {
		super("BudgetNotFoundException");
	}	
}
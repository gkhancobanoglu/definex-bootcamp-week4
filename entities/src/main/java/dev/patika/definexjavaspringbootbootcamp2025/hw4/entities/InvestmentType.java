package dev.patika.definexjavaspringbootbootcamp2025.hw4.entities;

public enum InvestmentType {
	STOCK("STOCK"), 
	FUND("FUND"), 
	REAL_ESTATE("REAL_ESTATE"), 
	COMMODITY("COMMODITY");
	
	private final String invesmentType;
	
	private InvestmentType(String type) {
		invesmentType = type;
	}
	
	String getType() {
		return invesmentType;
	}
}

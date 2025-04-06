package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import java.util.List;
import java.util.Map;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Investment;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.InvestmentNotFoundException;

public interface InvestmentService {
    List<Investment> findInvestments();
    Investment create(Investment investment);
    Investment update(Investment investment) throws InvestmentNotFoundException;
    Map<String, Object> getPerformanceMetrics();
    Map<String, Object> getPortfolioSummary();
}

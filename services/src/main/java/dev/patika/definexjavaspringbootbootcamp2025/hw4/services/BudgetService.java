package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Budget;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.BudgetNotFoundException;

public interface BudgetService {
    List<Budget> findBudgets();
    Budget create(Budget budget);
    Budget update(Budget budget) throws BudgetNotFoundException;
    Map<String, Object> analyzeBudgets();
    Map<String, Object> getProgress(UUID id) throws BudgetNotFoundException;
}
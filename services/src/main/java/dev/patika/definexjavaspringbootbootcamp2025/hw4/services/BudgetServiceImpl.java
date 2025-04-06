package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Budget;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Transaction;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.BudgetRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.TransactionRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.BudgetNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;


    @Override
    public List<Budget> findBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    public Budget create(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public Budget update(Budget budget) throws BudgetNotFoundException {
        if(!budgetRepository.existsById(budget.getId())){
            throw new BudgetNotFoundException();
        }
        return budgetRepository.save(budget);
    }

    /**
     * Tüm bütçeleri analiz eder, her bütçeye ait harcama oranını ve durumunu döndürür.
     */
    @Override
    public Map<String, Object> analyzeBudgets() {
        List<Budget> budgets = budgetRepository.findAll();
        Map<String, Object> budgetAnalysis = new HashMap<>();

        for (Budget budget : budgets) {
            // O bütçeye ait harcamaları çek
            BigDecimal totalSpent = transactionRepository
                    .findByCategory(budget.getCategory())
                    .stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Kalan bütçeyi hesapla
            BigDecimal remainingBudget = budget.getLimit().subtract(totalSpent);
            double spendingPercentage = budget.getLimit().compareTo(BigDecimal.ZERO) > 0
                    ? totalSpent.divide(budget.getLimit(), 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100
                    : 0.0;


            // Durumu belirle
            String status;
            if (spendingPercentage > 100) {
                status = "Budget exceeded!";
            } else if (spendingPercentage > 80) {
                status = "Close to limit!";
            } else {
                status = "Within budget";
            }

            // Sonucu ekle
            Map<String, Object> budgetData = new HashMap<>();
            budgetData.put("totalSpent", totalSpent);
            budgetData.put("remainingBudget", remainingBudget);
            budgetData.put("spendingPercentage", spendingPercentage);
            budgetData.put("status", status);

            budgetAnalysis.put(budget.getName(), budgetData);
        }

        return budgetAnalysis;
    }

    /**
     * Belirli bir bütçenin ilerleyişini hesaplar.
     */
    @Override
    public Map<String, Object> getProgress(UUID id) throws BudgetNotFoundException {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException());

        // O bütçeye ait tüm harcamaları getir
        BigDecimal totalSpent = transactionRepository
                .findByCategory(budget.getCategory())
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Kalan bütçeyi hesapla
        BigDecimal remainingBudget = budget.getLimit().subtract(totalSpent);
        double spendingPercentage = totalSpent.divide(budget.getLimit(), 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;

        // Sonucu döndür
        return Map.of(
                "budgetName", budget.getName(),
                "totalSpent", totalSpent,
                "remainingBudget", remainingBudget,
                "spendingPercentage", spendingPercentage
        );
    }
}

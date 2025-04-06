package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Budget;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Transaction;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.BudgetRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.CategoryRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.TransactionRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;


    @Override
    public List<Category> findCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) throws CategoryNotFoundException {
        if(!categoryRepository.existsById(category.getId())){
            throw new CategoryNotFoundException();
        }
        return categoryRepository.save(category);
    }

    /**
     * Her kategoriye ait harcamaları analiz eder.
     */
    @Override
    public Map<String, Object> getSpendingAnalysis() {
        List<Category> categories = categoryRepository.findAll(); // Tüm kategorileri getir
        Map<String, Object> spendingAnalysis = new HashMap<>();

        for (Category category : categories) {
            // Bu kategoriye ait harcamaları getir ve toplam harcamayı hesapla
            BigDecimal totalSpent = transactionRepository.findByCategory(category)
                    .stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Bu kategoriye ait bir bütçe var mı?
            Optional<Budget> budgetOpt = budgetRepository.findByCategory(category);

            BigDecimal budgetLimit = budgetOpt.map(Budget::getLimit).orElse(BigDecimal.ZERO);
            BigDecimal remainingBudget = budgetLimit.compareTo(BigDecimal.ZERO) > 0
                    ? budgetLimit.subtract(totalSpent)
                    : BigDecimal.ZERO; // Eğer bütçe yoksa, kalan bütçe sıfır olacak


            // Harcama yüzdesini hesapla
            double spendingPercentage = budgetLimit.compareTo(BigDecimal.ZERO) > 0
                    ? totalSpent.divide(budgetLimit, 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100
                    : 0.0;

            // Durum belirleme (Fazla harcama, limit yakın, güvenli)
            String status;
            if (spendingPercentage > 100) {
                status = "Budget exceeded!";
            } else if (spendingPercentage > 80) {
                status = "Close to limit!";
            } else {
                status = "Within budget";
            }

            // Sonuçları bir Map olarak kaydet
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("totalSpent", totalSpent);
            categoryData.put("budgetLimit", budgetLimit);
            categoryData.put("remainingBudget", remainingBudget);
            categoryData.put("spendingPercentage", spendingPercentage);
            categoryData.put("status", status);

            spendingAnalysis.put(category.getName(), categoryData);
        }

        return spendingAnalysis;
    }
}

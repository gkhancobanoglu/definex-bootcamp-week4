package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Budget;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Transaction;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.BudgetRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.CategoryRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.TransactionRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private UUID categoryId;
    private Category category;
    private List<Transaction> transactions;
    private Budget budget;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        category = new Category(categoryId, "Food", "Groceries and restaurants");
        budget = new Budget(UUID.randomUUID(), "Food Budget", new BigDecimal("1000.00"), category);

        transactions = List.of(
                new Transaction(UUID.randomUUID(), new BigDecimal("200.00"), "Dinner", LocalDateTime.now(), null, category, null),
                new Transaction(UUID.randomUUID(), new BigDecimal("150.00"), "Groceries", LocalDateTime.now(), null, category, null)
        );
    }

    /**
     * ✅ Tüm kategorileri listeleme testi (findCategories)
     */
    @Test
    void shouldReturnAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.findCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    /**
     * ✅ Yeni kategori oluşturma testi (create)
     */
    @Test
    void shouldCreateCategory() {
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.create(category);

        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        verify(categoryRepository, times(1)).save(category);
    }

    /**
     * ✅ Var olan kategoriyi güncelleme testi (update)
     */
    @Test
    void shouldUpdateCategory() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.update(category);

        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        verify(categoryRepository, times(1)).existsById(categoryId);
        verify(categoryRepository, times(1)).save(category);
    }

    /**
     *  Olmayan kategoriyi güncellemeye çalışırken hata fırlatma testi (update)
     */
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingCategory() {
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.update(category));
        verify(categoryRepository, times(1)).existsById(categoryId);
        verify(categoryRepository, never()).save(category);
    }

    /**
     * ✅ Kategorilerin harcama analizini yapma testi (getSpendingAnalysis)
     */
    @Test
    void shouldGetSpendingAnalysis() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(transactionRepository.findByCategory(category)).thenReturn(transactions);
        when(budgetRepository.findByCategory(category)).thenReturn(Optional.of(budget));

        Map<String, Object> result = categoryService.getSpendingAnalysis();

        assertNotNull(result);
        assertTrue(result.containsKey("Food"));
        Map<String, Object> categoryData = (Map<String, Object>) result.get("Food");
        assertEquals(new BigDecimal("350.00"), categoryData.get("totalSpent"));
        assertEquals(new BigDecimal("650.00"), categoryData.get("remainingBudget"));
        assertEquals(35.0, categoryData.get("spendingPercentage"));
        assertEquals("Within budget", categoryData.get("status"));
        verify(categoryRepository, times(1)).findAll();
        verify(transactionRepository, times(1)).findByCategory(category);
    }

    /**
     * ❌ Kategorilerde bütçe olmayan durum testini ekle
     */
    @Test
    void shouldHandleNoBudgetForCategory() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(transactionRepository.findByCategory(category)).thenReturn(transactions);
        when(budgetRepository.findByCategory(category)).thenReturn(Optional.empty());

        // Get spending analysis
        Map<String, Object> result = categoryService.getSpendingAnalysis();

        // Verify the result is not null
        assertNotNull(result);

        // Verify that "Food" category is in the result map
        assertTrue(result.containsKey("Food"));

        // Get the spending data for the "Food" category
        Map<String, Object> categoryData = (Map<String, Object>) result.get("Food");

        // Verify that the total spent is correct
        assertEquals(new BigDecimal("350.00"), categoryData.get("totalSpent"));

        // Verify that the remaining budget is correctly handled when no budget is found
        assertEquals(BigDecimal.ZERO, categoryData.get("remainingBudget"));

        // Verify that the spending percentage is correctly set to 0.0 when no budget is available
        assertEquals(0.0, categoryData.get("spendingPercentage"));

        // Verify the status when no budget is found
        assertEquals("Within budget", categoryData.get("status"));

        // Verify interactions with repositories
        verify(categoryRepository, times(1)).findAll();
        verify(transactionRepository, times(1)).findByCategory(category);
        verify(budgetRepository, times(1)).findByCategory(category);
    }



}

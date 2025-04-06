package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Budget;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Transaction;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.BudgetRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories.TransactionRepository;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.BudgetNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private UUID budgetId;
    private Budget budget;
    private Category category;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        budgetId = UUID.randomUUID();
        category = new Category(budgetId, "Food", "Groceries and restaurants");

        budget = Budget.builder()
                .id(budgetId)
                .name("Monthly Food Budget")
                .limit(new BigDecimal("1000.00"))
                .category(category)
                .build();

        transactions = List.of(
                new Transaction(UUID.randomUUID(), new BigDecimal("900.00"), "Dinner", null, null, category, null)
        );
    }

    /**
     * ✅ Tüm bütçeleri listeleme testi (findBudgets)
     */
    @Test
    void shouldReturnAllBudgets() {
        when(budgetRepository.findAll()).thenReturn(List.of(budget));

        List<Budget> result = budgetService.findBudgets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Monthly Food Budget", result.get(0).getName());
        verify(budgetRepository, times(1)).findAll();
    }

    /**
     * ✅ Yeni bütçe oluşturma testi (create)
     */
    @Test
    void shouldCreateBudget() {
        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget result = budgetService.create(budget);

        assertNotNull(result);
        assertEquals(budgetId, result.getId());
        verify(budgetRepository, times(1)).save(budget);
    }

    /**
     * ✅ Var olan bütçeyi güncelleme testi (update)
     */
    @Test
    void shouldUpdateBudget() {
        when(budgetRepository.existsById(budgetId)).thenReturn(true);
        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget result = budgetService.update(budget);

        assertNotNull(result);
        assertEquals(budgetId, result.getId());
        verify(budgetRepository, times(1)).existsById(budgetId);
        verify(budgetRepository, times(1)).save(budget);
    }

    /**
     *  Olmayan bütçeyi güncellemeye çalışırken hata fırlatma testi (update)
     */
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingBudget() {
        when(budgetRepository.existsById(budgetId)).thenReturn(false);

        assertThrows(BudgetNotFoundException.class, () -> budgetService.update(budget));
        verify(budgetRepository, times(1)).existsById(budgetId);
        verify(budgetRepository, never()).save(budget);
    }

    /**
     * ✅ Bütçe harcama analizi testi - "Within budget" senaryosu
     */
    @Test
    void shouldAnalyzeBudgetsWithinBudget() {
        when(budgetRepository.findAll()).thenReturn(List.of(budget));
        when(transactionRepository.findByCategory(category)).thenReturn(Collections.emptyList()); // Hiç harcama yok

        Map<String, Object> result = budgetService.analyzeBudgets();

        assertNotNull(result);
        assertEquals("Within budget", ((Map<String, Object>) result.get("Monthly Food Budget")).get("status"));
    }

    /**
     * ✅ Bütçe harcama analizi testi - "Close to limit" senaryosu
     */
    @Test
    void shouldAnalyzeBudgetsCloseToLimit() {
        when(budgetRepository.findAll()).thenReturn(List.of(budget));
        when(transactionRepository.findByCategory(category)).thenReturn(transactions); // 900 harcanmış

        Map<String, Object> result = budgetService.analyzeBudgets();

        assertNotNull(result);
        assertEquals("Close to limit!", ((Map<String, Object>) result.get("Monthly Food Budget")).get("status"));
    }

    /**
     * ✅ Belirli bir bütçenin ilerleme durumu testi (getProgress)
     */
    @Test
    void shouldGetBudgetProgress() {
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
        when(transactionRepository.findByCategory(category)).thenReturn(transactions);

        Map<String, Object> result = budgetService.getProgress(budgetId);

        assertNotNull(result);
        assertEquals("Monthly Food Budget", result.get("budgetName"));
        verify(budgetRepository, times(1)).findById(budgetId);
        verify(transactionRepository, times(1)).findByCategory(category);
    }

    /**
     *  Olmayan bütçenin ilerleme durumu alınırken hata fırlatma testi (getProgress)
     */
    @Test
    void shouldThrowExceptionWhenBudgetNotFoundInProgress() {
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(BudgetNotFoundException.class, () -> budgetService.getProgress(budgetId));
        verify(budgetRepository, times(1)).findById(budgetId);
        verify(transactionRepository, never()).findByCategory(any());
    }

    /**
     *  Bütçenin limiti 0 ise bölme hatasını önleme testi
     */
    @Test
    void shouldHandleDivisionByZero() {
        budget.setLimit(BigDecimal.ZERO); // Bütçe limiti sıfır
        when(budgetRepository.findAll()).thenReturn(List.of(budget));
        when(transactionRepository.findByCategory(category)).thenReturn(transactions);

        Map<String, Object> result = budgetService.analyzeBudgets();

        assertNotNull(result);
        assertEquals(0.0, ((Map<String, Object>) result.get("Monthly Food Budget")).get("spendingPercentage"));
    }


}

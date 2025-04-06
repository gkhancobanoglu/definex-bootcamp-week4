package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Budget;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.BudgetService;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.BudgetNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private UUID budgetId;
    private Budget budget;

    @BeforeEach
    void setUp() {
        budgetId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();  // Category için UUID
        Category category = new Category(categoryId, "Food", "Groceries");  // Category nesnesini oluştur

        // Budget nesnesini oluştur
        budget = new Budget(budgetId, "Food Budget", new BigDecimal("1000.00"), category);
    }


    // Test: Tüm bütçeleri listeleme
    @Test
    void shouldGetAllBudgets() {
        List<Budget> budgets = new ArrayList<>();
        budgets.add(budget);

        when(budgetService.findBudgets()).thenReturn(budgets);

        ResponseEntity<List<Budget>> response = budgetController.getBudgets();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(budgetService, times(1)).findBudgets();
    }

    // Test: Bütçe bulunamadığında (No Content)
    @Test
    void shouldReturnNoContentWhenNoBudgets() {
        when(budgetService.findBudgets()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Budget>> response = budgetController.getBudgets();

        assertEquals(204, response.getStatusCodeValue()); // 204 No Content
        verify(budgetService, times(1)).findBudgets();
    }

    // Test: Yeni bütçe oluşturulması
    @Test
    void shouldCreateBudget() {
        when(budgetService.create(budget)).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.createBudget(budget);

        assertEquals(201, response.getStatusCodeValue()); // 201 Created
        verify(budgetService, times(1)).create(budget);
    }

    // Test: Geçersiz bütçe limiti
    @Test
    void shouldReturnBadRequestWhenBudgetLimitIsNegative() {
        // Hatalı kısmı düzelttim, Category nesnesini oluşturuyorum
        UUID categoryId = UUID.randomUUID();  // Category için UUID
        Category category = new Category(categoryId, "Food", "Groceries");  // Category nesnesi oluştur

        // Budget nesnesini oluştur
        Budget invalidBudget = new Budget(UUID.randomUUID(), "Invalid Budget", new BigDecimal("-100.00"), category);

        ResponseEntity<Budget> response = budgetController.createBudget(invalidBudget);

        assertEquals(400, response.getStatusCodeValue()); // 400 Bad Request
        verify(budgetService, times(0)).create(invalidBudget); // create metodu çağrılmamalı
    }


    // Test: Bütçe analizini getirme
    @Test
    void shouldGetBudgetAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("Total Budget", 5000);

        when(budgetService.analyzeBudgets()).thenReturn(analysis);

        ResponseEntity<Map<String, Object>> response = budgetController.getBudgetAnalysis();

        assertEquals(200, response.getStatusCodeValue()); // 200 OK
        assertTrue(response.getBody().containsKey("Total Budget"));
        verify(budgetService, times(1)).analyzeBudgets();
    }

    // Test: Belirli bir bütçenin ilerleyişini getirme (Başarı durumu)
    @Test
    void shouldGetBudgetProgress() throws BudgetNotFoundException {
        Map<String, Object> progress = new HashMap<>();
        progress.put("Spent", 500);

        when(budgetService.getProgress(budgetId)).thenReturn(progress);

        ResponseEntity<Map<String, Object>> response = budgetController.getBudgetProgress(budgetId);

        assertEquals(200, response.getStatusCodeValue()); // 200 OK
        assertTrue(response.getBody().containsKey("Spent"));
        verify(budgetService, times(1)).getProgress(budgetId);
    }

    // Test: Belirli bir bütçenin ilerleyişini getirme (Hata durumu)
    @Test
    void shouldReturnNotFoundWhenBudgetDoesNotExist() throws BudgetNotFoundException {
        when(budgetService.getProgress(budgetId)).thenThrow(new BudgetNotFoundException());

        ResponseEntity<Map<String, Object>> response = budgetController.getBudgetProgress(budgetId);

        assertEquals(404, response.getStatusCodeValue()); // 404 Not Found
        verify(budgetService, times(1)).getProgress(budgetId);
    }
}

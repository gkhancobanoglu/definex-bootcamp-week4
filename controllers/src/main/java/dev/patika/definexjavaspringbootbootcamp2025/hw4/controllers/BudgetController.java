package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.BudgetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Budget;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.BudgetService;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    
	private final BudgetService budgetService;
	
	public BudgetController(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
    
    // Tüm bütçeleri listeleme
    @GetMapping("/v1")
    public ResponseEntity<List<Budget>> getBudgets() {
        List<Budget> budgets =  budgetService.findBudgets();
        if(budgets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(budgets);
    }

    // Yeni bütçe oluştur
    @PostMapping("/v1")
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        if (budget.getLimit().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // 400 döndür, geçersiz limit
        }
        Budget createdBudget = budgetService.create(budget);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBudget);
    }

    // Bütçe analizini getirme
    @GetMapping("/v1/analysis")
    public ResponseEntity<Map<String, Object>> getBudgetAnalysis() {
        Map<String, Object> analysis = budgetService.analyzeBudgets();
        return ResponseEntity.ok(analysis);
    }

    // Belirli bir bütçenin ilerleyişini getirme
    @GetMapping("/{id}/progress")
    public ResponseEntity<Map<String, Object>> getBudgetProgress(@PathVariable UUID id) {
        try {
            Map<String, Object> progress = budgetService.getProgress(id);
            return ResponseEntity.ok(progress); // 200 döndür
        } catch (BudgetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // 404 döndür, bütçe bulunamadı
        }
    }
}

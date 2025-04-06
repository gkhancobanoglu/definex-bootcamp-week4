package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import java.util.List;
import java.util.Map;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.CategoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Tüm kategorileri listeleme
    @GetMapping("/v1")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.findCategories();
        if(categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    //Kategori oluşturma
    @PostMapping("/v1")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        if (category.getName() == null || category.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // 400 döndür, geçersiz kategori adı
        }
        Category createdCategory = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    //Kategori güncelleme
    @PutMapping("/v1")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.update(category);
            return ResponseEntity.ok(updatedCategory); // 200 döndür, başarılı güncelleme
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // 404 döndür, kategori bulunamadı
        }
    }

    //harcama analizi getirme
    @GetMapping("/v1/spending-analysis")
    public ResponseEntity<Map<String, Object>> getSpendingAnalysis() {
        Map<String, Object> analysis = categoryService.getSpendingAnalysis();
        return ResponseEntity.ok(analysis); // 200 döndür
    }
}

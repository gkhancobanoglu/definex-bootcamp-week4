package dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.CategoryService;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Category category;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        category = new Category(categoryId, "Food", "Groceries and meals");
    }

    /**
     * ✅ Tüm kategorileri listeleme testi (getCategories)
     * - Kategoriler listeleniyor
     */
    @Test
    void shouldReturnCategories() {
        when(categoryService.findCategories()).thenReturn(List.of(category));

        ResponseEntity<List<Category>> response = categoryController.getCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(categoryService, times(1)).findCategories();
    }


    /**
     * ✅ Tüm kategorileri listeleme testi (getCategories)
     * - Kategori yok
     */
    @Test
    void shouldReturnNoContentWhenCategoriesAreEmpty() {
        when(categoryService.findCategories()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Category>> response = categoryController.getCategories();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        // Eğer response.getBody() null dönerse bunu kontrol edebiliriz,
        // NO_CONTENT olduğu için zaten body null olabilir
        assertNull(response.getBody());
        verify(categoryService, times(1)).findCategories();
    }



    /**
     * ✅ Kategori oluşturma testi (createCategory)
     * - Kategori adı geçerli
     */
    @Test
    void shouldCreateCategory() {
        when(categoryService.create(category)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.createCategory(category);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Food", response.getBody().getName());
        verify(categoryService, times(1)).create(category);
    }

    /**
     * ✅ Kategori oluşturma testi (createCategory)
     * - Kategori adı geçersiz
     */
    @Test
    void shouldReturnBadRequestWhenCategoryNameIsEmpty() {
        category.setName("");  // Boş isim

        ResponseEntity<Category> response = categoryController.createCategory(category);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(categoryService, times(0)).create(category);
    }

    /**
     * ✅ Kategori güncelleme testi (updateCategory)
     * - Kategori başarıyla güncelleniyor
     */
    @Test
    void shouldUpdateCategory() throws CategoryNotFoundException {
        when(categoryService.update(category)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.updateCategory(category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).update(category);
    }

    /**
     * ✅ Kategori güncelleme testi (updateCategory)
     * - Kategori bulunamıyor
     */
    @Test
    void shouldReturnNotFoundWhenCategoryNotFoundForUpdate() throws CategoryNotFoundException {
        when(categoryService.update(category)).thenThrow(new CategoryNotFoundException());

        ResponseEntity<Category> response = categoryController.updateCategory(category);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(categoryService, times(1)).update(category);
    }

    /**
     * ✅ Harcama analizi testi (getSpendingAnalysis)
     */
    @Test
    void shouldGetSpendingAnalysis() {
        Map<String, Object> analysis = Map.of("Food", 1000);
        when(categoryService.getSpendingAnalysis()).thenReturn(analysis);

        ResponseEntity<Map<String, Object>> response = categoryController.getSpendingAnalysis();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(analysis, response.getBody());
        verify(categoryService, times(1)).getSpendingAnalysis();
    }
}

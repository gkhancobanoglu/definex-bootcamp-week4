package dev.patika.definexjavaspringbootbootcamp2025.hw4.services;

import java.util.List;
import java.util.Map;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.services.exception.CategoryNotFoundException;

public interface CategoryService {
	List<Category> findCategories();
    Category create(Category category);
    Category update(Category category) throws CategoryNotFoundException;
    Map<String, Object> getSpendingAnalysis();
}

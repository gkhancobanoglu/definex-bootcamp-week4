package dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}

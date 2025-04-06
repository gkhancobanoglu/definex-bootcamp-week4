package dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Category;
import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByCategory(Category category);
}

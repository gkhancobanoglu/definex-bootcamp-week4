package dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface InvestmentRepository extends JpaRepository<Investment, UUID> {
}

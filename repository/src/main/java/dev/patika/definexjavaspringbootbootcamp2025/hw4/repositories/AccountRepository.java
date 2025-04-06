package dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories;

import dev.patika.definexjavaspringbootbootcamp2025.hw4.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
}

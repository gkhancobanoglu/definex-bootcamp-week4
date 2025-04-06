package dev.patika.definexjavaspringbootbootcamp2025.hw4.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories")
@EntityScan(basePackages = "dev.patika.definexjavaspringbootbootcamp2025.hw4.entities")
public class PersonalFinanceApplication {
 public static void main(String[] args) {
     SpringApplication.run(PersonalFinanceApplication.class, args);
 }
}



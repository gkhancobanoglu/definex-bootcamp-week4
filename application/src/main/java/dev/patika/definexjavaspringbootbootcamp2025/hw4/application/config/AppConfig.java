package dev.patika.definexjavaspringbootbootcamp2025.hw4.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {
		"dev.patika.definexjavaspringbootbootcamp2025.hw4.controllers",
		"dev.patika.definexjavaspringbootbootcamp2025.hw4.application",
		"dev.patika.definexjavaspringbootbootcamp2025.hw4.services",
		"dev.patika.definexjavaspringbootbootcamp2025.hw4.repositories",
		"dev.patika.definexjavaspringbootbootcamp2025.hw4.entities"
})
public class AppConfig {

	// H2 Veritabanı Yapılandırması
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		// H2 in-memory veritabanı bağlantı ayarları
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
		dataSource.setUsername("sa");
		dataSource.setPassword("password");

		return dataSource;
	}
}

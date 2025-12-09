package com.bookcorner.app;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {

    @Bean
    @ServiceConnection
    @SuppressWarnings("resource")
    PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("bookcorner_db")
            .withUsername("admin")
            .withPassword("admin123");
    }
}

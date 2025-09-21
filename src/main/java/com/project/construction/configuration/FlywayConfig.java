package com.project.construction.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // In a production environment, you might want to validate or baseline on migrate.
            // For development, repair can be useful if migrations get into a bad state.
            flyway.repair();
            flyway.migrate();
        };
    }
}

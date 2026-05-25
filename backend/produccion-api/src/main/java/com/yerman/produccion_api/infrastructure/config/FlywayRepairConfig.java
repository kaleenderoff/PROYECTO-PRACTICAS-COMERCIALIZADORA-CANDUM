package com.yerman.produccion_api.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayRepairConfig {
    @Bean
    public FlywayMigrationStrategy repairStrategy() {
        return flyway -> {
            System.out.println("--- RUNNING FLYWAY REPAIR ---");
            flyway.repair();
            flyway.migrate();
        };
    }
}

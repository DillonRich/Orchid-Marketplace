package com.orchid.orchid_marketplace.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cosmos")
@EnableCosmosRepositories(basePackages = "com.orchid.orchid_marketplace.repository.cosmos")
public class CosmosDbConfiguration extends AbstractCosmosConfiguration {

    @Value("${azure.cosmos.uri}")
    private String uri;

    @Value("${azure.cosmos.key}")
    private String key;

    @Value("${azure.cosmos.database}")
    private String database;

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        return new CosmosClientBuilder()
                .endpoint(uri)
                .key(key)
                .contentResponseOnWriteEnabled(true)
                .directMode();
    }

    @Bean
    public CosmosConfig cosmosDbConfig() {
        return CosmosConfig.builder()
                .enableQueryMetrics(true)
                .responseDiagnosticsProcessor(diagnostics -> {
                    // Log diagnostics for monitoring
                    System.out.println("Cosmos DB Diagnostics: " + diagnostics);
                })
                .build();
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }
}

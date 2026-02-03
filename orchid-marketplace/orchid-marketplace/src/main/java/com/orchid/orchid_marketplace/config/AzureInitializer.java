package com.orchid.orchid_marketplace.config;

import com.orchid.orchid_marketplace.service.azure.AzureBlobStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Initializes Azure services on application startup.
 * Only runs when azure.storage.enabled=true.
 */
@Configuration
public class AzureInitializer {

    private static final Logger logger = LoggerFactory.getLogger(AzureInitializer.class);

    @Bean
    @ConditionalOnProperty(prefix = "azure.storage", name = "enabled", havingValue = "true")
    public CommandLineRunner initializeAzureStorage(AzureBlobStorageService blobStorageService) {
        return args -> {
            logger.info("Initializing Azure Blob Storage containers...");
            try {
                blobStorageService.initializeContainers();
                logger.info("Azure Blob Storage initialization complete");
            } catch (Exception e) {
                logger.error("Failed to initialize Azure Blob Storage", e);
                // Don't fail application startup, but log prominently
            }
        };
    }
}

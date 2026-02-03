package com.orchid.orchid_marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.CacheConfiguration;
import com.orchid.orchid_marketplace.repository.CacheConfigurationRepository;

@Service
@Profile("!cosmos")
public class CacheConfigurationService {
    
    @Autowired
    private CacheConfigurationRepository cacheConfigurationRepository;
    
    // Get all cache configurations
    public List<CacheConfiguration> getAllCacheConfigurations() {
        return cacheConfigurationRepository.findAll();
    }
    
    // Get cache configuration by ID
    public Optional<CacheConfiguration> getCacheConfigurationById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return cacheConfigurationRepository.findById(id);
    }
    
    // Get cache configurations by entity type
    public List<CacheConfiguration> getCacheConfigurationsByEntityType(String entityType) {
        Objects.requireNonNull(entityType, "entityType must not be null");
        return cacheConfigurationRepository.findByEntityTypeAndIsEnabledTrue(entityType);
    }
    
    // Create a new cache configuration
    public CacheConfiguration createCacheConfiguration(CacheConfiguration cacheConfiguration) {
        Objects.requireNonNull(cacheConfiguration, "cacheConfiguration must not be null");
        @SuppressWarnings("null")
        CacheConfiguration saved = cacheConfigurationRepository.save(cacheConfiguration);
        return saved;
    }
    
    // Update a cache configuration
    public CacheConfiguration updateCacheConfiguration(UUID id, CacheConfiguration cacheConfigurationDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(cacheConfigurationDetails, "cacheConfigurationDetails must not be null");
        return cacheConfigurationRepository.findById(id)
            .map(existingConfig -> {
                if (cacheConfigurationDetails.getEntityType() != null) {
                    existingConfig.setEntityType(cacheConfigurationDetails.getEntityType());
                }
                
                if (cacheConfigurationDetails.getCacheKeyPattern() != null) {
                    existingConfig.setCacheKeyPattern(cacheConfigurationDetails.getCacheKeyPattern());
                }
                
                if (cacheConfigurationDetails.getCacheTtlSeconds() != null) {
                    existingConfig.setCacheTtlSeconds(cacheConfigurationDetails.getCacheTtlSeconds());
                }
                
                if (cacheConfigurationDetails.getCacheStrategy() != null) {
                    existingConfig.setCacheStrategy(cacheConfigurationDetails.getCacheStrategy());
                }
                
                if (cacheConfigurationDetails.getIsEnabled() != null) {
                    existingConfig.setIsEnabled(cacheConfigurationDetails.getIsEnabled());
                }

                @SuppressWarnings("null")
                CacheConfiguration saved = cacheConfigurationRepository.save(existingConfig);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Cache configuration not found with ID: " + id));
    }
    
    // Soft delete a cache configuration
    public void deleteCacheConfiguration(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        cacheConfigurationRepository.findById(id)
            .ifPresentOrElse(
                config -> {
                    config.softDelete();
                    cacheConfigurationRepository.save(config);
                },
                () -> { throw new RuntimeException("Cache configuration not found with ID: " + id); }
            );
    }
    
    // Toggle cache configuration enabled status
    public CacheConfiguration toggleCacheConfigurationStatus(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return cacheConfigurationRepository.findById(id)
            .map(config -> {
                boolean current = Boolean.TRUE.equals(config.getIsEnabled());
                config.setIsEnabled(!current);
                @SuppressWarnings("null")
                CacheConfiguration saved = cacheConfigurationRepository.save(config);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Cache configuration not found with ID: " + id));
    }
    
    // Get enabled cache configurations
    public List<CacheConfiguration> getEnabledCacheConfigurations() {
        return cacheConfigurationRepository.findByIsEnabledTrue();
    }
    
    // Get cache configurations by TTL threshold
    public List<CacheConfiguration> getCacheConfigurationsByMinTtl(Integer minTtl) {
        Objects.requireNonNull(minTtl, "minTtl must not be null");
        return cacheConfigurationRepository.findEnabledByMinTtl(minTtl);
    }
    
    // Get cache configurations by strategy
    public List<CacheConfiguration> getCacheConfigurationsByStrategy(String strategy) {
        return cacheConfigurationRepository.findByStrategy(strategy);
    }
}
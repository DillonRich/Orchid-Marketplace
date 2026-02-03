package com.orchid.orchid_marketplace.model;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cache_configurations")
public class CacheConfiguration extends BaseEntity {
    
    @Column(name = "cache_key_pattern", nullable = false)
    private String cacheKeyPattern;
    
    @Column(name = "cache_ttl_seconds")
    private Integer cacheTtlSeconds;
    
    @Column(name = "cache_strategy")
    private String cacheStrategy; // "WRITE_THROUGH", "WRITE_BEHIND", "CACHE_ASIDE"
    
    @Column(name = "invalidation_pattern")
    private String invalidationPattern;
    
    @Column(name = "entity_type")
    private String entityType; // "PRODUCT", "CATEGORY", "USER", "STORE"
    
    @Column(name = "is_enabled")
    private Boolean isEnabled = true;
    
    @Column(name = "max_entries")
    private Integer maxEntries;
    
    // ========== Constructors ==========
    
    public CacheConfiguration() {}
    
    public CacheConfiguration(String cacheKeyPattern, Integer cacheTtlSeconds, 
                             String cacheStrategy, String entityType) {
        this.cacheKeyPattern = cacheKeyPattern;
        this.cacheTtlSeconds = cacheTtlSeconds;
        this.cacheStrategy = cacheStrategy;
        this.entityType = entityType;
        this.isEnabled = true;
    }
    
    // ========== Getters and Setters ==========
    
    public String getCacheKeyPattern() {
        return cacheKeyPattern;
    }
    
    public void setCacheKeyPattern(String cacheKeyPattern) {
        this.cacheKeyPattern = cacheKeyPattern;
    }
    
    public Integer getCacheTtlSeconds() {
        return cacheTtlSeconds;
    }
    
    public void setCacheTtlSeconds(Integer cacheTtlSeconds) {
        this.cacheTtlSeconds = cacheTtlSeconds;
    }
    
    public String getCacheStrategy() {
        return cacheStrategy;
    }
    
    public void setCacheStrategy(String cacheStrategy) {
        this.cacheStrategy = cacheStrategy;
    }
    
    public String getInvalidationPattern() {
        return invalidationPattern;
    }
    
    public void setInvalidationPattern(String invalidationPattern) {
        this.invalidationPattern = invalidationPattern;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public Boolean getIsEnabled() {
        return isEnabled;
    }
    
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public Integer getMaxEntries() {
        return maxEntries;
    }
    
    public void setMaxEntries(Integer maxEntries) {
        this.maxEntries = maxEntries;
    }
    
    // ========== Helper Methods ==========
    
    public boolean isValidPattern() {
        return cacheKeyPattern != null && !cacheKeyPattern.trim().isEmpty() &&
               cacheKeyPattern.contains("{") && cacheKeyPattern.contains("}");
    }
    
    public String generateCacheKey(Object... params) {
        if (!isValidPattern()) {
            return null;
        }
        
        String key = cacheKeyPattern;
        int paramIndex = 0;
        
        // Replace placeholders with actual values
        while (key.contains("{") && key.contains("}") && paramIndex < params.length) {
            int start = key.indexOf("{");
            int end = key.indexOf("}");
            if (end > start) {
                String placeholder = key.substring(start, end + 1);
                String replacement = params[paramIndex] != null ? params[paramIndex].toString() : "";
                key = key.replace(placeholder, replacement);
                paramIndex++;
            }
        }
        
        return key;
    }
    
    public boolean shouldInvalidateOnUpdate() {
        if (cacheStrategy == null) {
            return false;
        }
        return cacheStrategy.equals("WRITE_THROUGH") || cacheStrategy.equals("CACHE_ASIDE");
    }
    
    public boolean isWriteBehind() {
        return cacheStrategy != null && cacheStrategy.equals("WRITE_BEHIND");
    }
    
    // FIXED: Using enhanced switch expression for cleaner code
    public String getCacheStrategyDescription() {
        if (cacheStrategy == null) {
            return "Unknown strategy";
        }
        
        return switch (cacheStrategy) {
            case "WRITE_THROUGH" -> "Write data through cache to database";
            case "WRITE_BEHIND" -> "Write to cache first, sync to database later";
            case "CACHE_ASIDE" -> "Application manages cache manually";
            default -> "Custom: " + cacheStrategy;
        };
    }
    
    // Helper to check if cache is expired
    public boolean isCacheExpired(Long cacheTimestamp) {
        if (cacheTtlSeconds == null || cacheTimestamp == null) {
            return true;
        }
        long currentTime = System.currentTimeMillis() / 1000;
        return (currentTime - cacheTimestamp) > cacheTtlSeconds;
    }
    
    // Helper to get cache duration in readable format
    public String getCacheDurationFormatted() {
        if (cacheTtlSeconds == null) {
            return "Not set";
        }
        
        if (cacheTtlSeconds < 60) {
            return cacheTtlSeconds + " seconds";
        } else if (cacheTtlSeconds < 3600) {
            return (cacheTtlSeconds / 60) + " minutes";
        } else if (cacheTtlSeconds < 86400) {
            return (cacheTtlSeconds / 3600) + " hours";
        } else {
            return (cacheTtlSeconds / 86400) + " days";
        }
    }
    
    // Helper to validate configuration
    public boolean isValidConfiguration() {
        return cacheKeyPattern != null && !cacheKeyPattern.trim().isEmpty() &&
               cacheStrategy != null && !cacheStrategy.trim().isEmpty() &&
               entityType != null && !entityType.trim().isEmpty() &&
               (cacheTtlSeconds == null || cacheTtlSeconds > 0);
    }
}
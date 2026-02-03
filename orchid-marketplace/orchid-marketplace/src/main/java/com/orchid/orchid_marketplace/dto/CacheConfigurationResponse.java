package com.orchid.orchid_marketplace.dto;

public class CacheConfigurationResponse {
    private String entityType;
    private String cacheKeyPattern;
    private Integer cacheTtlSeconds;
    private String cacheStrategy;
    private String invalidationPattern;
    private Boolean isEnabled;
    private Integer maxEntries;

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getCacheKeyPattern() { return cacheKeyPattern; }
    public void setCacheKeyPattern(String cacheKeyPattern) { this.cacheKeyPattern = cacheKeyPattern; }

    public Integer getCacheTtlSeconds() { return cacheTtlSeconds; }
    public void setCacheTtlSeconds(Integer cacheTtlSeconds) { this.cacheTtlSeconds = cacheTtlSeconds; }

    public String getCacheStrategy() { return cacheStrategy; }
    public void setCacheStrategy(String cacheStrategy) { this.cacheStrategy = cacheStrategy; }

    public String getInvalidationPattern() { return invalidationPattern; }
    public void setInvalidationPattern(String invalidationPattern) { this.invalidationPattern = invalidationPattern; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public Integer getMaxEntries() { return maxEntries; }
    public void setMaxEntries(Integer maxEntries) { this.maxEntries = maxEntries; }
}

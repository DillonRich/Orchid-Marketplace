package com.orchid.orchid_marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CacheConfigurationRequest {
    @NotBlank
    private String entityType;
    @NotBlank
    private String cacheKeyPattern;
    @NotNull
    private Integer cacheTtlSeconds;

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getCacheKeyPattern() { return cacheKeyPattern; }
    public void setCacheKeyPattern(String cacheKeyPattern) { this.cacheKeyPattern = cacheKeyPattern; }

    public Integer getCacheTtlSeconds() { return cacheTtlSeconds; }
    public void setCacheTtlSeconds(Integer cacheTtlSeconds) { this.cacheTtlSeconds = cacheTtlSeconds; }
}

package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.CacheConfigurationRequest;
import com.orchid.orchid_marketplace.model.CacheConfiguration;

public final class CacheConfigurationMapper {
    private CacheConfigurationMapper() {}

    public static CacheConfiguration toEntity(CacheConfigurationRequest req) {
        if (req == null) return null;
        CacheConfiguration c = new CacheConfiguration();
        c.setEntityType(req.getEntityType());
        c.setCacheKeyPattern(req.getCacheKeyPattern());
        c.setCacheTtlSeconds(req.getCacheTtlSeconds());
        return c;
    }

    public static com.orchid.orchid_marketplace.dto.CacheConfigurationResponse toResponse(CacheConfiguration c) {
        if (c == null) return null;
        com.orchid.orchid_marketplace.dto.CacheConfigurationResponse r = new com.orchid.orchid_marketplace.dto.CacheConfigurationResponse();
        r.setEntityType(c.getEntityType());
        r.setCacheKeyPattern(c.getCacheKeyPattern());
        r.setCacheTtlSeconds(c.getCacheTtlSeconds());
        r.setCacheStrategy(c.getCacheStrategy());
        r.setInvalidationPattern(c.getInvalidationPattern());
        r.setIsEnabled(c.getIsEnabled());
        r.setMaxEntries(c.getMaxEntries());
        return r;
    }
}

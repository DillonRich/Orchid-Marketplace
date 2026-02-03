package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.CacheConfigurationRequest;
import com.orchid.orchid_marketplace.dto.CacheConfigurationResponse;
import com.orchid.orchid_marketplace.mapper.CacheConfigurationMapper;
import com.orchid.orchid_marketplace.model.CacheConfiguration;
import com.orchid.orchid_marketplace.service.CacheConfigurationService;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/cache-configurations")
public class CacheConfigurationController {

    @Autowired
    private CacheConfigurationService cacheService;

    @GetMapping
    public List<CacheConfigurationResponse> listAll() {
        return cacheService.getAllCacheConfigurations()
            .stream()
            .map(CacheConfigurationMapper::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CacheConfigurationResponse> getById(@PathVariable UUID id) {
        return cacheService.getCacheConfigurationById(id).map(CacheConfigurationMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CacheConfigurationResponse create(@jakarta.validation.Valid @RequestBody CacheConfigurationRequest req) {
        CacheConfiguration cfg = CacheConfigurationMapper.toEntity(req);
        CacheConfiguration saved = cacheService.createCacheConfiguration(cfg);
        return CacheConfigurationMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public CacheConfigurationResponse update(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody CacheConfigurationRequest req) {
        CacheConfiguration cfg = CacheConfigurationMapper.toEntity(req);
        CacheConfiguration updated = cacheService.updateCacheConfiguration(id, cfg);
        return CacheConfigurationMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cacheService.deleteCacheConfiguration(id);
        return ResponseEntity.noContent().build();
    }
}

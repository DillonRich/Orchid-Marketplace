package com.orchid.orchid_marketplace.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.StoreRequest;
import com.orchid.orchid_marketplace.dto.StoreResponse;
import com.orchid.orchid_marketplace.mapper.StoreMapper;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.service.StoreService;

@RestController
@RequestMapping("/api/stores")
@Profile("!cosmos")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping
    public List<StoreResponse> listAll() {
        return storeService.getAllStores()
            .stream()
            .map(StoreMapper::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getById(@PathVariable UUID id) {
        return storeService.getStoreById(id).map(StoreMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public StoreResponse create(@jakarta.validation.Valid @RequestBody StoreRequest req) {
        Store s = StoreMapper.toEntity(req);
        Store saved = storeService.createStore(s);
        return StoreMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public StoreResponse update(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody StoreRequest req) {
        Store s = StoreMapper.toEntity(req);
        Store updated = storeService.updateStore(id, s);
        return StoreMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}

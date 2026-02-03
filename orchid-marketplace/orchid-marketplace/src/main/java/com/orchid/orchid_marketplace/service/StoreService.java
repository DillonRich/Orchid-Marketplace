package com.orchid.orchid_marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.repository.StoreRepository;

@Service
@Profile("!cosmos")
public class StoreService {
    
    @Autowired
    private StoreRepository storeRepository;
    
    // Get all stores
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }
    
    // Get store by ID
    public Optional<Store> getStoreById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return storeRepository.findById(id);
    }
    
    // Get store by slug
    public Optional<Store> getStoreBySlug(String slug) {
        return storeRepository.findBySlug(slug);
    }
    
    // Get store by store name
    public Optional<Store> getStoreByName(String storeName) {
        return storeRepository.findByStoreName(storeName);
    }
    
    // Get store by seller ID
    public Optional<Store> getStoreBySellerId(UUID sellerId) {
        Objects.requireNonNull(sellerId, "sellerId must not be null");
        return storeRepository.findBySellerId(sellerId);
    }
    
    // Get active stores
    public List<Store> getActiveStores() {
        return storeRepository.findByIsActiveTrue();
    }
    
    // Get public and active stores
    public List<Store> getPublicStores() {
        return storeRepository.findByIsPublicTrueAndIsActiveTrue();
    }
    
    // Search stores
    public List<Store> searchStores(String keyword) {
        return storeRepository.searchStores(keyword);
    }
    
    // Create a new store
    public Store createStore(Store store) {
        Objects.requireNonNull(store, "store must not be null");
        // Check if store slug already exists
        if (storeRepository.existsBySlug(store.getSlug())) {
            throw new RuntimeException("Store with slug '" + store.getSlug() + "' already exists");
        }
        
        // Check if store name already exists
        if (storeRepository.existsByStoreName(store.getStoreName())) {
            throw new RuntimeException("Store with name '" + store.getStoreName() + "' already exists");
        }
        
        @SuppressWarnings("null")
        Store saved = storeRepository.save(store);
        return saved;
    }
    
    // Update a store
    public Store updateStore(UUID id, Store storeDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(storeDetails, "storeDetails must not be null");
        return storeRepository.findById(id)
            .map(existingStore -> {
                if (storeDetails.getStoreName() != null) {
                    // Check if new name already exists (excluding current store)
                    Optional<Store> existingWithName = storeRepository.findByStoreName(storeDetails.getStoreName());
                    if (existingWithName.map(s -> !s.getId().equals(id)).orElse(false)) {
                        throw new RuntimeException("Store with name '" + storeDetails.getStoreName() + "' already exists");
                    }
                    existingStore.setStoreName(storeDetails.getStoreName());
                }
                
                if (storeDetails.getSlug() != null) {
                    existingStore.setSlug(storeDetails.getSlug());
                }
                
                if (storeDetails.getAboutText() != null) {
                    existingStore.setAboutText(storeDetails.getAboutText());
                }
                
                if (storeDetails.getReturnPolicyText() != null) {
                    existingStore.setReturnPolicyText(storeDetails.getReturnPolicyText());
                }
                
                if (storeDetails.getProfileImageUrl() != null) {
                    existingStore.setProfileImageUrl(storeDetails.getProfileImageUrl());
                }
                
                if (storeDetails.getBannerImageUrl() != null) {
                    existingStore.setBannerImageUrl(storeDetails.getBannerImageUrl());
                }
                
                if (storeDetails.getIsActive() != null) {
                    existingStore.setIsActive(storeDetails.getIsActive());
                }
                
                if (storeDetails.getIsPublic() != null) {
                    existingStore.setIsPublic(storeDetails.getIsPublic());
                }

                @SuppressWarnings("null")
                Store saved = storeRepository.save(existingStore);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Store not found with ID: " + id));
    }
    
    // Soft delete a store
    public void deleteStore(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        storeRepository.findById(id)
            .ifPresentOrElse(
                store -> {
                    store.softDelete();
                    storeRepository.save(store);
                },
                () -> { throw new RuntimeException("Store not found with ID: " + id); }
            );
    }
    
    // Toggle store active status
    public Store toggleStoreStatus(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return storeRepository.findById(id)
            .map(store -> {
                store.setIsActive(!Boolean.TRUE.equals(store.getIsActive()));
                @SuppressWarnings("null")
                Store saved = storeRepository.save(store);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Store not found with ID: " + id));
    }
    
    // Count active products in store
    public long countActiveProducts(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return storeRepository.countActiveProducts(storeId);
    }
    
    // Get store average rating
    public Double getStoreAverageRating(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return storeRepository.calculateStoreAverageRating(storeId);
    }
    
    // Get top selling stores
    public List<Store> getTopSellingStores(int limit) {
        return storeRepository.findTopSellingStores(
            org.springframework.data.domain.PageRequest.of(0, limit)
        ).stream().toList();
    }
    
    // Get total number of stores
    public long countStores() {
        return storeRepository.count();
    }
    
    // Get total number of active stores
    public long countActiveStores() {
        List<Store> activeStores = storeRepository.findByIsActiveTrue();
        return activeStores.size();
    }
}
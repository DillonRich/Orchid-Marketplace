package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    
    Optional<Store> findBySlug(String slug);
    Optional<Store> findByStoreName(String storeName);
    boolean existsBySlug(String slug);
    boolean existsByStoreName(String storeName);
    
    @Query("SELECT s FROM Store s WHERE s.seller.id = :sellerId")
    Optional<Store> findBySellerId(@Param("sellerId") UUID sellerId);
    
    List<Store> findByIsPublicTrue();
    List<Store> findByIsPublicTrueAndIsActiveTrue();
    List<Store> findByIsActiveTrue();
    
    // Use SQL LOWER via function() to avoid dialect issues with CLOB/text columns
    // Caller should provide a lower-cased keyword wrapped with '%' wildcards (e.g. "%term%").
    // For now, restrict search to `storeName` to avoid DB CLOB/text LIKE limitations on `aboutText`.
    @Query("SELECT s FROM Store s WHERE s.isActive = true AND LOWER(s.storeName) LIKE :keyword")
    List<Store> searchStores(@Param("keyword") String keyword);
    
    @Query("SELECT s FROM Store s WHERE s.isActive = true ORDER BY s.totalSales DESC")
    List<Store> findTopSellingStores(org.springframework.data.domain.Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Store s JOIN s.products p WHERE s.id = :storeId AND p.isActive = true")
    long countActiveProducts(@Param("storeId") UUID storeId);
    
    // Calculate average rating by averaging review ratings across active products in the store
    @Query("SELECT AVG(r.rating) FROM Store s JOIN s.products p JOIN p.reviews r WHERE s.id = :storeId AND p.isActive = true")
    Double calculateStoreAverageRating(@Param("storeId") UUID storeId);
}
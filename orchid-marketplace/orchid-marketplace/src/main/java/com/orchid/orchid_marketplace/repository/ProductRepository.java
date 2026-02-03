package com.orchid.orchid_marketplace.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    List<Product> findByStoreId(UUID storeId);
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByStoreIdAndIsActiveTrue(UUID storeId);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stockQuantity > 0")
    List<Product> findInStockProducts();
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stockQuantity > 0 AND p.stockQuantity <= :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
    
    @Query("SELECT DISTINCT p FROM Product p JOIN p.tags t WHERE p.isActive = true AND t = :tag")
    List<Product> findByTag(@Param("tag") String tag);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.condition = :condition")
    List<Product> findByCondition(@Param("condition") Product.ProductCondition condition);
    
    @Query("SELECT p FROM Product p WHERE p.store.id = :storeId AND p.isActive = true ORDER BY p.soldCount DESC")
    List<Product> findTopSellingByStore(@Param("storeId") UUID storeId, org.springframework.data.domain.Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.category.id = :categoryId ORDER BY p.createdAt DESC")
    List<Product> findNewestByCategory(@Param("categoryId") UUID categoryId, org.springframework.data.domain.Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.weightLbs <= :maxWeight")
    List<Product> findByMaxWeight(@Param("maxWeight") BigDecimal maxWeight);
    
    long countByStoreIdAndIsActiveTrue(UUID storeId);
}
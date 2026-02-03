package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    
    List<ProductImage> findByProductId(UUID productId);
    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(UUID productId);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId AND pi.isActive = true ORDER BY pi.displayOrder ASC")
    List<ProductImage> findActiveByProduct(@Param("productId") UUID productId);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId AND pi.isPrimary = true AND pi.isActive = true")
    Optional<ProductImage> findPrimaryImageByProduct(@Param("productId") UUID productId);
    
    @Query("SELECT COUNT(pi) FROM ProductImage pi WHERE pi.product.id = :productId AND pi.isActive = true")
    long countActiveImagesByProduct(@Param("productId") UUID productId);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.azureBlobUrl IS NOT NULL AND pi.isActive = true")
    List<ProductImage> findImagesWithAzureUrls();
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.blobContainer = :container AND pi.isActive = true")
    List<ProductImage> findByBlobContainer(@Param("container") String container);
}
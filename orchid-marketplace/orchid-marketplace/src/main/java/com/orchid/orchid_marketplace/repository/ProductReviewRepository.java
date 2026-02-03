package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, UUID> {
    
    List<ProductReview> findByProductId(UUID productId);
    List<ProductReview> findByUserId(UUID userId);
    List<ProductReview> findByProductIdAndIsActiveTrue(UUID productId);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.product.id = :productId AND pr.isActive = true ORDER BY pr.createdAt DESC")
    List<ProductReview> findActiveReviewsByProduct(@Param("productId") UUID productId);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.product.id = :productId AND pr.isVerifiedPurchase = true AND pr.isActive = true")
    List<ProductReview> findVerifiedReviewsByProduct(@Param("productId") UUID productId);
    
    @Query("SELECT AVG(pr.rating) FROM ProductReview pr WHERE pr.product.id = :productId AND pr.isActive = true")
    Double calculateAverageRatingByProduct(@Param("productId") UUID productId);
    
    @Query("SELECT COUNT(pr) FROM ProductReview pr WHERE pr.product.id = :productId AND pr.isActive = true")
    long countActiveReviewsByProduct(@Param("productId") UUID productId);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.product.store.id = :storeId AND pr.isActive = true ORDER BY pr.createdAt DESC")
    List<ProductReview> findReviewsByStore(@Param("storeId") UUID storeId);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.sentimentScore IS NOT NULL AND pr.isActive = true ORDER BY pr.sentimentScore DESC")
    List<ProductReview> findReviewsWithSentimentAnalysis();
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.rating = :rating AND pr.isActive = true")
    List<ProductReview> findByRating(@Param("rating") Integer rating);
}
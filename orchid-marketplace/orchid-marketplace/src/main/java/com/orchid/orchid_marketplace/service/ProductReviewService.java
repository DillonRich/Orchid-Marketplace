package com.orchid.orchid_marketplace.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.model.ProductReview;
import com.orchid.orchid_marketplace.repository.ProductReviewRepository;

@Service
@Profile("!cosmos")
public class ProductReviewService {
    
    @Autowired
    private ProductReviewRepository productReviewRepository;
    
    // Get all product reviews
    public List<ProductReview> getAllProductReviews() {
        return productReviewRepository.findAll();
    }
    
    // Get product review by ID
    public Optional<ProductReview> getProductReviewById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return productReviewRepository.findById(id);
    }
    
    // Get active reviews by product ID
    public List<ProductReview> getReviewsByProductId(UUID productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return productReviewRepository.findActiveReviewsByProduct(productId);
    }
    
    // Get reviews by user ID
    public List<ProductReview> getReviewsByUserId(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return productReviewRepository.findByUserId(userId);
    }
    
    // Get reviews by rating
    public List<ProductReview> getReviewsByRating(Integer rating) {
        Objects.requireNonNull(rating, "rating must not be null");
        return productReviewRepository.findByRating(rating);
    }
    
    // Get verified purchase reviews for product
    public List<ProductReview> getVerifiedReviewsByProductId(UUID productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return productReviewRepository.findVerifiedReviewsByProduct(productId);
    }
    
    // Create a new product review
    public ProductReview createProductReview(ProductReview productReview) {
        Objects.requireNonNull(productReview, "productReview must not be null");
        @SuppressWarnings("null")
        ProductReview saved = productReviewRepository.save(productReview);
        return saved;
    }
    
    // Update a product review
    public ProductReview updateProductReview(UUID id, ProductReview productReviewDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(productReviewDetails, "productReviewDetails must not be null");
        return productReviewRepository.findById(id)
            .map(existingReview -> {
                if (productReviewDetails.getRating() != null) {
                    existingReview.setRating(productReviewDetails.getRating());
                }
                
                if (productReviewDetails.getTitle() != null) {
                    existingReview.setTitle(productReviewDetails.getTitle());
                }
                
                if (productReviewDetails.getComment() != null) {
                    existingReview.setComment(productReviewDetails.getComment());
                }
                
                if (productReviewDetails.getSentimentScore() != null) {
                    existingReview.setSentimentScore(productReviewDetails.getSentimentScore());
                }

                @SuppressWarnings("null")
                ProductReview saved = productReviewRepository.save(existingReview);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Product review not found with ID: " + id));
    }
    
    // Soft delete a product review
    public void deleteProductReview(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        productReviewRepository.findById(id)
            .ifPresentOrElse(
                review -> {
                    review.softDelete();
                    productReviewRepository.save(review);
                },
                () -> { throw new RuntimeException("Product review not found with ID: " + id); }
            );
    }
    
    // Calculate average rating for product
    public Double calculateAverageRatingForProduct(UUID productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        Double averageRating = productReviewRepository.calculateAverageRatingByProduct(productId);
        return averageRating != null ? averageRating : 0.0;
    }
    
    // Count active reviews for product
    public long countReviewsForProduct(UUID productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return productReviewRepository.countActiveReviewsByProduct(productId);
    }
    
    // Get reviews by store
    public List<ProductReview> getReviewsByStore(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return productReviewRepository.findReviewsByStore(storeId);
    }

    @Transactional
    @SuppressWarnings("null")
    public ProductReview addSellerResponse(UUID reviewId, String response) {
        ProductReview review = productReviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setSellerResponse(response);
        review.setSellerResponseDate(LocalDateTime.now());

        return productReviewRepository.save(review);
    }
}
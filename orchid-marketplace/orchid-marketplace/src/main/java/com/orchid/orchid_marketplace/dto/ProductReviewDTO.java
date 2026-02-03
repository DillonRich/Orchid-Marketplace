package com.orchid.orchid_marketplace.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductReviewDTO {

    private UUID id;
    private UUID productId;
    private UUID reviewerId;
    private String reviewerName;
    private Integer rating;
    private String title;
    private String comment;
    private Boolean verifiedPurchase;
    private Integer helpfulCount;
    private LocalDateTime createdAt;
    private String sellerResponse;
    private LocalDateTime sellerResponseDate;

    public ProductReviewDTO() {}

    public ProductReviewDTO(UUID id, UUID productId, UUID reviewerId, String reviewerName,
                           Integer rating, String title, String comment, Boolean verifiedPurchase,
                           Integer helpfulCount, LocalDateTime createdAt,
                           String sellerResponse, LocalDateTime sellerResponseDate) {
        this.id = id;
        this.productId = productId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.verifiedPurchase = verifiedPurchase;
        this.helpfulCount = helpfulCount;
        this.createdAt = createdAt;
        this.sellerResponse = sellerResponse;
        this.sellerResponseDate = sellerResponseDate;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public UUID getReviewerId() { return reviewerId; }
    public void setReviewerId(UUID reviewerId) { this.reviewerId = reviewerId; }

    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Boolean getVerifiedPurchase() { return verifiedPurchase; }
    public void setVerifiedPurchase(Boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; }

    public Integer getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(Integer helpfulCount) { this.helpfulCount = helpfulCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getSellerResponse() { return sellerResponse; }
    public void setSellerResponse(String sellerResponse) { this.sellerResponse = sellerResponse; }

    public LocalDateTime getSellerResponseDate() { return sellerResponseDate; }
    public void setSellerResponseDate(LocalDateTime sellerResponseDate) { this.sellerResponseDate = sellerResponseDate; }
}

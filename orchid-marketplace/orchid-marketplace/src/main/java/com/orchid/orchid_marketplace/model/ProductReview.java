package com.orchid.orchid_marketplace.model;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_reviews")
public class ProductReview extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
    
    private Integer rating;
    private String title;
    
    @Column(length = 2000)
    private String comment;
    
    @Column(name = "is_verified_purchase")
    private Boolean isVerifiedPurchase = false;
    
    // Azure AI Search for Sentiment Analysis
    @Column(name = "sentiment_score")
    private Float sentimentScore;
    
    @Column(name = "key_phrases")
    private String keyPhrases;

    @Column(name = "seller_response", length = 2000)
    private String sellerResponse;

    @Column(name = "seller_response_date")
    private LocalDateTime sellerResponseDate;
    
    // ========== Constructors ==========
    
    public ProductReview() {}
    
    public ProductReview(Product product, User user, Integer rating, String comment) {
        this.product = product;
        this.user = user;
        this.rating = rating;
        this.comment = comment;
    }
    
    // ========== Getters and Setters ==========
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public OrderItem getOrderItem() {
        return orderItem;
    }
    
    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Boolean getIsVerifiedPurchase() {
        return isVerifiedPurchase;
    }
    
    public void setIsVerifiedPurchase(Boolean isVerifiedPurchase) {
        this.isVerifiedPurchase = isVerifiedPurchase;
    }
    
    public Float getSentimentScore() {
        return sentimentScore;
    }
    
    public void setSentimentScore(Float sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
    
    public String getKeyPhrases() {
        return keyPhrases;
    }
    
    public void setKeyPhrases(String keyPhrases) {
        this.keyPhrases = keyPhrases;
    }

    public String getSellerResponse() {
        return sellerResponse;
    }

    public void setSellerResponse(String sellerResponse) {
        this.sellerResponse = sellerResponse;
    }

    public LocalDateTime getSellerResponseDate() {
        return sellerResponseDate;
    }

    public void setSellerResponseDate(LocalDateTime sellerResponseDate) {
        this.sellerResponseDate = sellerResponseDate;
    }
    
    // Helper methods
    public boolean isValidRating() {
        return rating != null && rating >= 1 && rating <= 5;
    }
    
    public boolean hasComment() {
        return comment != null && !comment.trim().isEmpty();
    }
    
    public String getRatingStars() {
        if (rating == null) return "No rating";
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }
    
    public String getSentimentLabel() {
        if (sentimentScore == null) return "Unknown";
        if (sentimentScore >= 0.7) return "Very Positive";
        if (sentimentScore >= 0.3) return "Positive";
        if (sentimentScore >= -0.3) return "Neutral";
        if (sentimentScore >= -0.7) return "Negative";
        return "Very Negative";
    }
}
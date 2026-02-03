package com.orchid.orchid_marketplace.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSearchDTO {

    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private Double averageRating;
    private Long reviewCount;
    private Integer stock;
    private String storeName;

    public ProductSearchDTO() {}

    public ProductSearchDTO(UUID id, String title, String description, BigDecimal price, 
                          String category, Double averageRating, Long reviewCount, 
                          Integer stock, String storeName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.stock = stock;
        this.storeName = storeName;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Long getReviewCount() { return reviewCount; }
    public void setReviewCount(Long reviewCount) { this.reviewCount = reviewCount; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
}

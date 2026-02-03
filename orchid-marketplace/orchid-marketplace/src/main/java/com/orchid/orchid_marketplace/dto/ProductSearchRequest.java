package com.orchid.orchid_marketplace.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSearchRequest {
    private String keyword;
    private UUID categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Double minRating;
    private String sortBy; // "price_asc", "price_desc", "rating", "newest", "popular"
    private Integer page;
    private Integer size;

    // Constructors
    public ProductSearchRequest() {}

    public ProductSearchRequest(String keyword, UUID categoryId, BigDecimal minPrice, 
                                BigDecimal maxPrice, Double minRating, String sortBy,
                                Integer page, Integer size) {
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minRating = minRating;
        this.sortBy = sortBy;
        this.page = page;
        this.size = size;
    }

    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getPage() {
        return page != null ? page : 0;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size != null ? size : 20;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}

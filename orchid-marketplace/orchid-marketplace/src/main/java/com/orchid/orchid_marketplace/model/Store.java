package com.orchid.orchid_marketplace.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set; // Added import

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "stores")
public class Store extends BaseEntity {
    
    // CRITICAL RELATIONSHIP: One Store to One User (Seller)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", referencedColumnName = "id", unique = true, nullable = false)
    private User seller;
    
    // Store Profile Information
    @Column(name = "store_name", nullable = false, unique = true, length = 100)
    private String storeName;
    
    @Column(name = "slug", nullable = false, unique = true, length = 110)
    private String slug;
    
    // Azure Blob Storage URLs
    @Column(name = "profile_image_url", length = 512)
    private String profileImageUrl;
    
    @Column(name = "banner_image_url", length = 512)
    private String bannerImageUrl;
    
    // Text Fields
    @Lob
    @Column(name = "about_text", columnDefinition = "TEXT")
    private String aboutText;
    
    @Lob
    @Column(name = "return_policy_text", columnDefinition = "TEXT")
    private String returnPolicyText;
    
    // Stats & Status
    @Column(name = "total_sales")
    private Integer totalSales = 0;
    
    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;
    
    @Column(name = "is_public")
    private Boolean isPublic = true;
    
    // Azure AI Search Integration
    @Column(name = "search_vector")
    private String searchVector;
    
    // Cache Control
    @Column(name = "cache_key")
    private String cacheKey;
    
    @Column(name = "last_updated_for_cache")
    private Long lastUpdatedForCache;
    
    // Relationships
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
    
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> soldItems = new HashSet<>();
    
    // ========== Constructors ==========
    
    public Store() {}
    
    public Store(User seller, String storeName, String slug) {
        this.seller = seller;
        this.storeName = storeName;
        this.slug = slug;
        this.isPublic = true;
        this.totalSales = 0;
    }
    
    // ========== Getters and Setters ==========
    
    public User getSeller() {
        return seller;
    }
    
    public void setSeller(User seller) {
        this.seller = seller;
    }
    
    public String getStoreName() {
        return storeName;
    }
    
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    
    public String getBannerImageUrl() {
        return bannerImageUrl;
    }
    
    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }
    
    public String getAboutText() {
        return aboutText;
    }
    
    public void setAboutText(String aboutText) {
        this.aboutText = aboutText;
    }
    
    public String getReturnPolicyText() {
        return returnPolicyText;
    }
    
    public void setReturnPolicyText(String returnPolicyText) {
        this.returnPolicyText = returnPolicyText;
    }
    
    public Integer getTotalSales() {
        return totalSales;
    }
    
    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }
    
    public BigDecimal getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }
    
    public Boolean getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public String getSearchVector() {
        return searchVector;
    }
    
    public void setSearchVector(String searchVector) {
        this.searchVector = searchVector;
    }
    
    public String getCacheKey() {
        return cacheKey;
    }
    
    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
    
    public Long getLastUpdatedForCache() {
        return lastUpdatedForCache;
    }
    
    public void setLastUpdatedForCache(Long lastUpdatedForCache) {
        this.lastUpdatedForCache = lastUpdatedForCache;
    }
    
    public Set<Product> getProducts() {
        return products;
    }
    
    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    
    public Set<OrderItem> getSoldItems() {
        return soldItems;
    }
    
    public void setSoldItems(Set<OrderItem> soldItems) {
        this.soldItems = soldItems;
    }
    
    // Helper method to add product
    public void addProduct(Product product) {
        products.add(product);
        product.setStore(this);
    }
    
    // Helper method to add sold item
    public void addSoldItem(OrderItem orderItem) {
        soldItems.add(orderItem);
        orderItem.setStore(this);
    }
    
    // FIXED: Business logic method to calculate average rating
    public BigDecimal calculateAverageRating() {
        if (products == null || products.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        double sum = 0.0;
        int reviewCount = 0;
        
        for (Product product : products) {
            if (product.getReviews() != null) {
                for (ProductReview review : product.getReviews()) {
                    sum += review.getRating();
                    reviewCount++;
                }
            }
        }
        
        if (reviewCount == 0) {
            return BigDecimal.ZERO;
        }
        
        double average = sum / reviewCount;
        
        // Using RoundingMode.HALF_UP instead of BigDecimal.ROUND_HALF_UP
        return BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP);
    }
    
    // Helper method to increment total sales
    public void incrementTotalSales(int quantity) {
        if (totalSales == null) {
            totalSales = quantity;
        } else {
            totalSales += quantity;
        }
    }
    
    // Helper method to check if store is empty
    public boolean isEmpty() {
        return products == null || products.isEmpty();
    }
    
    // Helper method to get active product count
    public int getActiveProductCount() {
        if (products == null) {
            return 0;
        }
        return (int) products.stream()
            .filter(product -> product.getIsActive() != null && product.getIsActive())
            .count();
    }
    
    // Helper method to get store URL
    public String getStoreUrl() {
        return "/stores/" + slug;
    }
}
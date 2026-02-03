package com.orchid.orchid_marketplace.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 5000)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity;
    
    @Column(name = "sold_count")
    private Integer soldCount = 0;
    
    @Enumerated(EnumType.STRING)
    private ProductCondition condition;
    
    public enum ProductCondition {
        NEW, LIKE_NEW, GOOD, FAIR
    }
    
    // New shipping-related fields
    @Column(name = "weight_lbs")
    private BigDecimal weightLbs;
    
    @Column(name = "package_length_in")
    private BigDecimal packageLengthIn;
    
    @Column(name = "package_width_in")
    private BigDecimal packageWidthIn;
    
    @Column(name = "package_height_in")
    private BigDecimal packageHeightIn;
    
    @Column(name = "requires_special_handling")
    private Boolean requiresSpecialHandling = false;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductImage> images = new HashSet<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductReview> reviews = new HashSet<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShippingOption> shippingOptions = new HashSet<>();
    
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems = new HashSet<>();
    
    // Azure AI Search Fields
    @Column(name = "search_index_id")
    private String searchIndexId;
    
    @Column(name = "search_score")
    private Float searchScore;
    
    // Azure Blob Storage
    @Column(name = "azure_container_name")
    private String azureContainerName = "product-images";
    
    @Column(name = "azure_storage_account")
    private String azureStorageAccount;
    
    // Cache Control for Redis
    @Column(name = "cache_ttl")
    private Integer cacheTtl = 3600; // 1 hour in seconds
    
    @Column(name = "cache_tags")
    private String cacheTags; // "product:store:{storeId},category:{categoryId}"
    
    // Tags for Search
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();
    
    // ========== Constructors ==========
    
    public Product() {}
    
    public Product(String title, String description, BigDecimal price, Integer stockQuantity, 
                   Store store, ProductCondition condition) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.store = store;
        this.condition = condition;
        this.soldCount = 0;
    }
    
    // ========== Getters and Setters ==========
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public Integer getSoldCount() { return soldCount; }
    public void setSoldCount(Integer soldCount) { this.soldCount = soldCount; }
    
    public ProductCondition getCondition() { return condition; }
    public void setCondition(ProductCondition condition) { this.condition = condition; }
    
    // Shipping-related getters and setters
    public BigDecimal getWeightLbs() { return weightLbs; }
    public void setWeightLbs(BigDecimal weightLbs) { this.weightLbs = weightLbs; }
    
    public BigDecimal getPackageLengthIn() { return packageLengthIn; }
    public void setPackageLengthIn(BigDecimal packageLengthIn) { this.packageLengthIn = packageLengthIn; }
    
    public BigDecimal getPackageWidthIn() { return packageWidthIn; }
    public void setPackageWidthIn(BigDecimal packageWidthIn) { this.packageWidthIn = packageWidthIn; }
    
    public BigDecimal getPackageHeightIn() { return packageHeightIn; }
    public void setPackageHeightIn(BigDecimal packageHeightIn) { this.packageHeightIn = packageHeightIn; }
    
    public Boolean getRequiresSpecialHandling() { return requiresSpecialHandling; }
    public void setRequiresSpecialHandling(Boolean requiresSpecialHandling) { this.requiresSpecialHandling = requiresSpecialHandling; }
    
    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public Set<ProductImage> getImages() { return images; }
    public void setImages(Set<ProductImage> images) { this.images = images; }
    
    public Set<ProductReview> getReviews() { return reviews; }
    public void setReviews(Set<ProductReview> reviews) { this.reviews = reviews; }
    
    public Set<ShippingOption> getShippingOptions() { return shippingOptions; }
    public void setShippingOptions(Set<ShippingOption> shippingOptions) { this.shippingOptions = shippingOptions; }
    
    public Set<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(Set<OrderItem> orderItems) { this.orderItems = orderItems; }
    
    public String getSearchIndexId() { return searchIndexId; }
    public void setSearchIndexId(String searchIndexId) { this.searchIndexId = searchIndexId; }
    
    public Float getSearchScore() { return searchScore; }
    public void setSearchScore(Float searchScore) { this.searchScore = searchScore; }
    
    public String getAzureContainerName() { return azureContainerName; }
    public void setAzureContainerName(String azureContainerName) { this.azureContainerName = azureContainerName; }
    
    public String getAzureStorageAccount() { return azureStorageAccount; }
    public void setAzureStorageAccount(String azureStorageAccount) { this.azureStorageAccount = azureStorageAccount; }
    
    public Integer getCacheTtl() { return cacheTtl; }
    public void setCacheTtl(Integer cacheTtl) { this.cacheTtl = cacheTtl; }
    
    public String getCacheTags() { return cacheTags; }
    public void setCacheTags(String cacheTags) { this.cacheTags = cacheTags; }
    
    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }
    
    // ========== Helper Methods ==========
    
    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }
    
    public void addReview(ProductReview review) {
        reviews.add(review);
        review.setProduct(this);
    }
    
    public void addShippingOption(ShippingOption shippingOption) {
        shippingOptions.add(shippingOption);
        shippingOption.setProduct(this);
    }
    
    public void addTag(String tag) {
        tags.add(tag);
    }
    
    public void removeTag(String tag) {
        tags.remove(tag);
    }
    
    public boolean hasStock() {
        return stockQuantity != null && stockQuantity > 0;
    }
    
    public boolean reduceStock(Integer quantity) {
        if (stockQuantity == null || stockQuantity < quantity) {
            return false;
        }
        stockQuantity -= quantity;
        soldCount += quantity;
        return true;
    }
    
    public void increaseStock(Integer quantity) {
        if (stockQuantity == null) {
            stockQuantity = quantity;
        } else {
            stockQuantity += quantity;
        }
    }
    
    public BigDecimal calculateAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        double average = reviews.stream()
            .mapToInt(ProductReview::getRating)
            .average()
            .orElse(0.0);
        
        return BigDecimal.valueOf(average).setScale(1, RoundingMode.HALF_UP);
    }
    
    public String getFormattedPrice() {
        if (price == null) {
            return "$0.00";
        }
        return "$" + price.setScale(2, RoundingMode.HALF_UP);
    }
    
    public boolean isLowStock() {
        return stockQuantity != null && stockQuantity <= 10;
    }
    
    public ProductImage getPrimaryImage() {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream()
            .filter(ProductImage::getIsPrimary)
            .findFirst()
            .orElse(images.iterator().next());
    }
    
    // Shipping helper methods
    public BigDecimal calculatePackageVolume() {
        if (packageLengthIn == null || packageWidthIn == null || packageHeightIn == null) {
            return null;
        }
        return packageLengthIn.multiply(packageWidthIn).multiply(packageHeightIn);
    }
    
    public boolean hasShippingOptions() {
        return shippingOptions != null && !shippingOptions.isEmpty();
    }
    
    public ShippingOption getDefaultShippingOption() {
        if (shippingOptions == null || shippingOptions.isEmpty()) {
            return null;
        }
        // First try to find free shipping
        return shippingOptions.stream()
            .filter(ShippingOption::getIsFreeShipping)
            .findFirst()
            .orElse(shippingOptions.iterator().next());
    }
}
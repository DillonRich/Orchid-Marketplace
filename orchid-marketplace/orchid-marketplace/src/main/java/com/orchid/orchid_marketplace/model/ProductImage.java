package com.orchid.orchid_marketplace.model;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_images")
public class ProductImage extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    @Column(name = "azure_blob_url")
    private String azureBlobUrl;
    
    @Column(name = "blob_container")
    private String blobContainer = "product-images";
    
    @Column(name = "blob_name")
    private String blobName;
    
    @Column(name = "sas_token_expiry")
    private Long sasTokenExpiry;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    @Column(name = "is_primary")
    private Boolean isPrimary = false;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "content_type")
    private String contentType;
    
    // ========== Constructors ==========
    
    public ProductImage() {}
    
    public ProductImage(Product product, String imageUrl, String azureBlobUrl, String blobName) {
        this.product = product;
        this.imageUrl = imageUrl;
        this.azureBlobUrl = azureBlobUrl;
        this.blobName = blobName;
    }
    
    // ========== Getters and Setters ==========
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getAzureBlobUrl() {
        return azureBlobUrl;
    }
    
    public void setAzureBlobUrl(String azureBlobUrl) {
        this.azureBlobUrl = azureBlobUrl;
    }
    
    public String getBlobContainer() {
        return blobContainer;
    }
    
    public void setBlobContainer(String blobContainer) {
        this.blobContainer = blobContainer;
    }
    
    public String getBlobName() {
        return blobName;
    }
    
    public void setBlobName(String blobName) {
        this.blobName = blobName;
    }
    
    public Long getSasTokenExpiry() {
        return sasTokenExpiry;
    }
    
    public void setSasTokenExpiry(Long sasTokenExpiry) {
        this.sasTokenExpiry = sasTokenExpiry;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public Boolean getIsPrimary() {
        return isPrimary;
    }
    
    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    // Helper methods
    public String getThumbnailUrl() {
        if (azureBlobUrl != null && azureBlobUrl.contains("?")) {
            return azureBlobUrl + "&resize=width:200";
        } else if (azureBlobUrl != null) {
            return azureBlobUrl + "?resize=width:200";
        }
        return imageUrl;
    }
    
    public String getMediumUrl() {
        if (azureBlobUrl != null && azureBlobUrl.contains("?")) {
            return azureBlobUrl + "&resize=width:500";
        } else if (azureBlobUrl != null) {
            return azureBlobUrl + "?resize=width:500";
        }
        return imageUrl;
    }
    
    public boolean isSasTokenValid() {
        if (sasTokenExpiry == null) {
            return false;
        }
        long currentTime = System.currentTimeMillis() / 1000;
        return currentTime < sasTokenExpiry;
    }
}
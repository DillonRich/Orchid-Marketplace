package com.orchid.orchid_marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.ProductImage;
import com.orchid.orchid_marketplace.repository.ProductImageRepository;

@Service
@Profile("!cosmos")
public class ProductImageService {
    
    @Autowired
    private ProductImageRepository productImageRepository;
    
    // Get all product images
    public List<ProductImage> getAllProductImages() {
        return productImageRepository.findAll();
    }
    
    // Get product image by ID
    public Optional<ProductImage> getProductImageById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return productImageRepository.findById(id);
    }
    
    // Get product images by product ID
    public List<ProductImage> getProductImagesByProductId(UUID productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return productImageRepository.findActiveByProduct(productId);
    }
    
    // Get primary image for product
    public Optional<ProductImage> getPrimaryImageForProduct(UUID productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return productImageRepository.findPrimaryImageByProduct(productId);
    }
    
    // Create a new product image
    public ProductImage createProductImage(ProductImage productImage) {
        Objects.requireNonNull(productImage, "productImage must not be null");
        @SuppressWarnings("null")
        ProductImage saved = productImageRepository.save(productImage);
        return saved;
    }
    
    // Update a product image
    public ProductImage updateProductImage(UUID id, ProductImage productImageDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(productImageDetails, "productImageDetails must not be null");
        return productImageRepository.findById(id)
            .map(existingImage -> {
                if (productImageDetails.getImageUrl() != null) {
                    existingImage.setImageUrl(productImageDetails.getImageUrl());
                }
                
                if (productImageDetails.getAzureBlobUrl() != null) {
                    existingImage.setAzureBlobUrl(productImageDetails.getAzureBlobUrl());
                }
                
                if (productImageDetails.getContentType() != null) {
                    existingImage.setContentType(productImageDetails.getContentType());
                }
                
                if (productImageDetails.getDisplayOrder() != null) {
                    existingImage.setDisplayOrder(productImageDetails.getDisplayOrder());
                }
                
                if (productImageDetails.getIsPrimary() != null) {
                    existingImage.setIsPrimary(productImageDetails.getIsPrimary());
                }

                @SuppressWarnings("null")
                ProductImage saved = productImageRepository.save(existingImage);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Product image not found with ID: " + id));
    }
    
    // Soft delete a product image
    public void deleteProductImage(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        productImageRepository.findById(id)
            .ifPresentOrElse(
                image -> {
                    image.softDelete();
                    productImageRepository.save(image);
                },
                () -> { throw new RuntimeException("Product image not found with ID: " + id); }
            );
    }
    
    // Set an image as primary for product
    public ProductImage setPrimaryImage(UUID productId, UUID imageId) {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(imageId, "imageId must not be null");
        return productImageRepository.findById(imageId)
            .map(image -> {
                image.setIsPrimary(true);
                @SuppressWarnings("null")
                ProductImage saved = productImageRepository.save(image);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Product image not found with ID: " + imageId));
    }
    
    // Count active images for product
    public long countImagesForProduct(UUID productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return productImageRepository.countActiveImagesByProduct(productId);
    }
    
    // Get images with Azure URLs
    public List<ProductImage> getImagesWithAzureUrls() {
        return productImageRepository.findImagesWithAzureUrls();
    }
}
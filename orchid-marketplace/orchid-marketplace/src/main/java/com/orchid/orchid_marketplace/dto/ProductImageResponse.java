package com.orchid.orchid_marketplace.dto;

import java.util.UUID;

public class ProductImageResponse {
    private UUID id;
    private String imageUrl;
    private UUID productId;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
}

package com.orchid.orchid_marketplace.dto;

import java.util.UUID;

public class OrderItemResponse {
    private UUID id;
    private UUID productId;
    private Integer quantity;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}

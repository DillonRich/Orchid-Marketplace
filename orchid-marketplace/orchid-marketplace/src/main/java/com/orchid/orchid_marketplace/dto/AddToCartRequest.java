package com.orchid.orchid_marketplace.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for adding an item to the shopping cart.
 */
public class AddToCartRequest {
    
    @NotNull(message = "Product ID is required")
    private UUID productId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    private UUID shippingOptionId; // Optional - can be selected later
    
    public AddToCartRequest() {
    }
    
    public UUID getProductId() {
        return productId;
    }
    
    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public UUID getShippingOptionId() {
        return shippingOptionId;
    }
    
    public void setShippingOptionId(UUID shippingOptionId) {
        this.shippingOptionId = shippingOptionId;
    }
}

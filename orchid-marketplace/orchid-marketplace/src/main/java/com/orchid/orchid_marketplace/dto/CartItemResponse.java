package com.orchid.orchid_marketplace.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response DTO for a single cart item.
 */
public class CartItemResponse {
    
    private UUID cartItemId;
    private UUID productId;
    private String productName;
    private String productDescription;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private UUID shippingOptionId;
    private String shippingMethod;
    private BigDecimal shippingCost;
    private Integer stockAvailable;
    
    public CartItemResponse() {
    }
    
    public UUID getCartItemId() {
        return cartItemId;
    }
    
    public void setCartItemId(UUID cartItemId) {
        this.cartItemId = cartItemId;
    }
    
    public UUID getProductId() {
        return productId;
    }
    
    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public BigDecimal getLineTotal() {
        return lineTotal;
    }
    
    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
    
    public UUID getShippingOptionId() {
        return shippingOptionId;
    }
    
    public void setShippingOptionId(UUID shippingOptionId) {
        this.shippingOptionId = shippingOptionId;
    }
    
    public String getShippingMethod() {
        return shippingMethod;
    }
    
    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
    
    public BigDecimal getShippingCost() {
        return shippingCost;
    }
    
    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }
    
    public Integer getStockAvailable() {
        return stockAvailable;
    }
    
    public void setStockAvailable(Integer stockAvailable) {
        this.stockAvailable = stockAvailable;
    }
}

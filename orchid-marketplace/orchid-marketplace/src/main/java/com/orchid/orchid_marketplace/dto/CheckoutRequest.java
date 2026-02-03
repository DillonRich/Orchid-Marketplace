package com.orchid.orchid_marketplace.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for initiating checkout.
 * Requires shipping and billing addresses, and optionally a payment method.
 */
public class CheckoutRequest {
    
    @NotNull(message = "Shipping address ID is required")
    private UUID shippingAddressId;
    
    @NotNull(message = "Billing address ID is required")
    private UUID billingAddressId;
    
    private UUID paymentMethodId; // For saved cards
    
    private Boolean createPaymentMethodForFuture = false; // Create token for future use
    
    // ========== Constructors ==========
    
    public CheckoutRequest() {}
    
    public CheckoutRequest(UUID shippingAddressId, UUID billingAddressId) {
        this.shippingAddressId = shippingAddressId;
        this.billingAddressId = billingAddressId;
    }
    
    // ========== Getters and Setters ==========
    
    public UUID getShippingAddressId() {
        return shippingAddressId;
    }
    
    public void setShippingAddressId(UUID shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }
    
    public UUID getBillingAddressId() {
        return billingAddressId;
    }
    
    public void setBillingAddressId(UUID billingAddressId) {
        this.billingAddressId = billingAddressId;
    }
    
    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }
    
    public void setPaymentMethodId(UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
    
    public Boolean getCreatePaymentMethodForFuture() {
        return createPaymentMethodForFuture;
    }
    
    public void setCreatePaymentMethodForFuture(Boolean createPaymentMethodForFuture) {
        this.createPaymentMethodForFuture = createPaymentMethodForFuture;
    }
}

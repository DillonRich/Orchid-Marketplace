package com.orchid.orchid_marketplace.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class OrderRequest {
    @NotNull
    private UUID buyerId;

    private UUID shippingAddressId;
    private UUID billingAddressId;

    public UUID getBuyerId() { return buyerId; }
    public void setBuyerId(UUID buyerId) { this.buyerId = buyerId; }

    public UUID getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(UUID shippingAddressId) { this.shippingAddressId = shippingAddressId; }

    public UUID getBillingAddressId() { return billingAddressId; }
    public void setBillingAddressId(UUID billingAddressId) { this.billingAddressId = billingAddressId; }
}

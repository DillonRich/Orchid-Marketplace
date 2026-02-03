package com.orchid.orchid_marketplace.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class StripeConnectAuthorizeRequest {

    @NotNull
    private UUID storeId;

    // Optional: where to send the user after callback (for future UI).
    private String returnUrl;

    public UUID getStoreId() { return storeId; }
    public void setStoreId(UUID storeId) { this.storeId = storeId; }

    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }
}

package com.orchid.orchid_marketplace.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class StartConversationRequest {

    @NotNull(message = "Seller ID is required")
    private UUID sellerId;

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    private String initialMessage;

    // Constructors
    public StartConversationRequest() {
    }

    public StartConversationRequest(UUID sellerId, UUID orderId, String initialMessage) {
        this.sellerId = sellerId;
        this.orderId = orderId;
        this.initialMessage = initialMessage;
    }

    // Getters and Setters
    public UUID getSellerId() { return sellerId; }
    public void setSellerId(UUID sellerId) { this.sellerId = sellerId; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public String getInitialMessage() { return initialMessage; }
    public void setInitialMessage(String initialMessage) { this.initialMessage = initialMessage; }
}


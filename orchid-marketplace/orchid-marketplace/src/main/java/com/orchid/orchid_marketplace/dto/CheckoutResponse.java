package com.orchid.orchid_marketplace.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for checkout response.
 * Returns order ID, Stripe session details, and status.
 */
public class CheckoutResponse {
    
    private UUID orderId;
    
    private String stripeSessionId; // Stripe Checkout Session ID
    
    private String checkoutUrl; // URL to redirect customer to Stripe
    
    private String status; // PENDING, PAYMENT_REQUIRED, CONFIRMED
    
    private BigDecimal totalAmount;
    
    private String message;
    
    // ========== Constructors ==========
    
    public CheckoutResponse() {}
    
    public CheckoutResponse(UUID orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
    
    public CheckoutResponse(UUID orderId, String stripeSessionId, String checkoutUrl, String status) {
        this.orderId = orderId;
        this.stripeSessionId = stripeSessionId;
        this.checkoutUrl = checkoutUrl;
        this.status = status;
    }
    
    // ========== Getters and Setters ==========
    
    public UUID getOrderId() {
        return orderId;
    }
    
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
    
    public String getStripeSessionId() {
        return stripeSessionId;
    }
    
    public void setStripeSessionId(String stripeSessionId) {
        this.stripeSessionId = stripeSessionId;
    }
    
    public String getCheckoutUrl() {
        return checkoutUrl;
    }
    
    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}

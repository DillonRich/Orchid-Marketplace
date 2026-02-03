package com.orchid.orchid_marketplace.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

/**
 * REST controller for payment intent creation.
 * Handles payment processing with Stripe Payment Intents.
 */
@RestController
@Profile("!cosmos")
@RequestMapping("/api/payment")
public class PaymentController {
    
    @Value("${stripe.secret-key}")
    private String stripeApiKey;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }
    
    /**
     * Create a Stripe Payment Intent for processing payment.
     * Works for both authenticated and guest users.
     */
    @PostMapping("/create-intent")
    public ResponseEntity<?> createPaymentIntent(@Valid @RequestBody PaymentIntentRequest request) {
        try {
            // Calculate amount in cents
            long amountInCents = Math.round(request.getAmount() * 100);
            
            // Build metadata
            Map<String, String> metadata = new HashMap<>();
            if (request.getOrderId() != null) {
                metadata.put("order_id", request.getOrderId().toString());
            }
            if (request.getCustomerId() != null) {
                metadata.put("customer_id", request.getCustomerId());
            }
            if (request.getGuestEmail() != null) {
                metadata.put("guest_email", request.getGuestEmail());
            }
            
            // Create payment intent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(request.getCurrency() != null ? request.getCurrency() : "usd")
                .putAllMetadata(metadata)
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();
            
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            
            // Build response
            PaymentIntentResponse response = new PaymentIntentResponse();
            response.setClientSecret(paymentIntent.getClientSecret());
            response.setPaymentIntentId(paymentIntent.getId());
            
            return ResponseEntity.ok(response);
            
        } catch (StripeException e) {
            return ResponseEntity
                .status(HttpStatus.PAYMENT_REQUIRED)
                .body(Map.of("error", "Stripe error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Payment intent creation failed: " + e.getMessage()));
        }
    }
    
    /**
     * Request DTO for payment intent creation
     */
    public static class PaymentIntentRequest {
        private Double amount;
        private String currency;
        private UUID orderId;
        private String customerId;
        private String guestEmail;
        
        public Double getAmount() {
            return amount;
        }
        
        public void setAmount(Double amount) {
            this.amount = amount;
        }
        
        public String getCurrency() {
            return currency;
        }
        
        public void setCurrency(String currency) {
            this.currency = currency;
        }
        
        public UUID getOrderId() {
            return orderId;
        }
        
        public void setOrderId(UUID orderId) {
            this.orderId = orderId;
        }
        
        public String getCustomerId() {
            return customerId;
        }
        
        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }
        
        public String getGuestEmail() {
            return guestEmail;
        }
        
        public void setGuestEmail(String guestEmail) {
            this.guestEmail = guestEmail;
        }
    }
    
    /**
     * Response DTO for payment intent creation
     */
    public static class PaymentIntentResponse {
        private String clientSecret;
        private String paymentIntentId;
        
        public String getClientSecret() {
            return clientSecret;
        }
        
        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }
        
        public String getPaymentIntentId() {
            return paymentIntentId;
        }
        
        public void setPaymentIntentId(String paymentIntentId) {
            this.paymentIntentId = paymentIntentId;
        }
    }
}

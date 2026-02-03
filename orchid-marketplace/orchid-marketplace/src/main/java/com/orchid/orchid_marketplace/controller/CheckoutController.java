package com.orchid.orchid_marketplace.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.CheckoutRequest;
import com.orchid.orchid_marketplace.dto.CheckoutResponse;
import com.orchid.orchid_marketplace.dto.GuestCheckoutRequest;
import com.orchid.orchid_marketplace.dto.StripeCheckoutSessionResponse;
import com.orchid.orchid_marketplace.mapper.OrderMapper;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.service.CheckoutService;
import com.orchid.orchid_marketplace.service.stripe.StripeCheckoutService;
import com.stripe.exception.StripeException;

import jakarta.validation.Valid;

/**
 * REST controller for checkout operations.
 * Handles order creation, payment confirmation, and cancellation.
 */
@RestController
@Profile("!cosmos")
@RequestMapping("/api/checkout")
public class CheckoutController {
    
    @Autowired
    private CheckoutService checkoutService;
    
    @Autowired
    private StripeCheckoutService stripeCheckoutService;
    
    /**
     * Initiate checkout and create order from cart.
     */
    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        
        try {
            // Create order from cart
            Order order = checkoutService.createOrderFromCart(
                user.getId(),
                request.getShippingAddressId(),
                request.getBillingAddressId()
            );
            
            // Return order details
            // Note: Next step would be to create Stripe session for payment
            CheckoutResponse response = new CheckoutResponse();
            response.setOrderId(order.getId());
            response.setStatus(order.getStatus().toString());
            response.setTotalAmount(order.getTotalAmount());
            response.setMessage("Order created successfully. Proceed to payment.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Create order from cart (authenticated users).
     * Alternative endpoint path for frontend compatibility.
     */
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody CheckoutRequest request,
            Authentication authentication) {
        return checkout(request, authentication);
    }
    
    /**
     * Create order from cart for guest users (no authentication required).
     */
    @PostMapping("/create-order-guest")
    public ResponseEntity<?> createOrderGuest(
            @Valid @RequestBody GuestCheckoutRequest request) {
        
        try {
            // Create order for guest user with cart items
            Order order = checkoutService.createOrderFromCartGuest(
                request.getGuestEmail(),
                request.getShippingAddress(),
                request.getBillingAddress(),
                request.getCartItems()
            );
            
            CheckoutResponse response = new CheckoutResponse();
            response.setOrderId(order.getId());
            response.setStatus(order.getStatus().toString());
            response.setTotalAmount(order.getTotalAmount());
            response.setMessage("Order created successfully. Proceed to payment.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Initiate Stripe payment for an order.
     * Returns Stripe Checkout Session URL for frontend redirect.
     */
    @PostMapping("/{orderId}/initiate-payment")
    public ResponseEntity<?> initiatePayment(
            @PathVariable UUID orderId,
            @RequestParam String successUrl,
            @RequestParam String cancelUrl,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        
        try {
            // Verify order belongs to user
            if (!checkoutService.doesOrderBelongToUser(orderId, user.getId())) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("You don't have access to this order"));
            }
            
            // Create Stripe Checkout Session
            StripeCheckoutSessionResponse stripeSession = stripeCheckoutService.createCheckoutSession(
                orderId,
                successUrl,
                cancelUrl
            );
            
            return ResponseEntity.ok(stripeSession);
        } catch (StripeException e) {
            return ResponseEntity
                .status(HttpStatus.PAYMENT_REQUIRED)
                .body(new ErrorResponse("Stripe payment error: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Get order details.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(
            @PathVariable UUID orderId,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        
        try {
            // Verify order belongs to user
            if (!checkoutService.doesOrderBelongToUser(orderId, user.getId())) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("You don't have access to this order"));
            }
            
            Order order = checkoutService.getOrder(orderId);
            return ResponseEntity.ok(OrderMapper.toResponse(order));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Confirm order payment (called from Stripe webhook or frontend).
     */
    @PostMapping("/{orderId}/confirm-payment")
    public ResponseEntity<?> confirmPayment(
            @PathVariable UUID orderId,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        
        try {
            // Verify order belongs to user
            if (!checkoutService.doesOrderBelongToUser(orderId, user.getId())) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("You don't have access to this order"));
            }
            
            Order order = checkoutService.confirmOrderPayment(orderId);
            
            CheckoutResponse response = new CheckoutResponse();
            response.setOrderId(order.getId());
            response.setStatus(order.getStatus().toString());
            response.setTotalAmount(order.getTotalAmount());
            response.setMessage("Order confirmed. Thank you for your purchase!");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Cancel order and release stock.
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(
            @PathVariable UUID orderId,
            @RequestParam(required = false) String reason,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        
        try {
            // Verify order belongs to user
            if (!checkoutService.doesOrderBelongToUser(orderId, user.getId())) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("You don't have access to this order"));
            }
            
            Order order = checkoutService.cancelOrder(orderId, reason != null ? reason : "User requested cancellation");
            
            CheckoutResponse response = new CheckoutResponse();
            response.setOrderId(order.getId());
            response.setStatus(order.getStatus().toString());
            response.setMessage("Order cancelled successfully. Stock has been restored.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Simple error response DTO
     */
    public static class ErrorResponse {
        private String error;
        private String timestamp;
        
        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = java.time.LocalDateTime.now().toString();
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
        
        public String getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}

package com.orchid.orchid_marketplace.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.AddToCartRequest;
import com.orchid.orchid_marketplace.dto.CartResponse;
import com.orchid.orchid_marketplace.mapper.CartMapper;
import com.orchid.orchid_marketplace.model.Cart;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.service.CartService;

import jakarta.validation.Valid;

/**
 * REST controller for shopping cart operations.
 * Provides endpoints for viewing and managing the user's shopping cart.
 */
@RestController
@RequestMapping("/api/cart")
@Profile("!cosmos")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * Get current user's cart.
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Cart cart = cartService.getCart(user.getId());
        return ResponseEntity.ok(CartMapper.toResponse(cart));
    }
    
    /**
     * Add item to cart.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        
        try {
            Cart cart = cartService.addToCart(
                user.getId(),
                request.getProductId(),
                request.getQuantity(),
                request.getShippingOptionId()
            );
            return ResponseEntity.ok(CartMapper.toResponse(cart));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Update cart item quantity.
     */
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<?> updateCartItemQuantity(
            @PathVariable UUID cartItemId,
            @RequestParam Integer quantity,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        
        try {
            Cart cart = cartService.updateCartItemQuantity(user.getId(), cartItemId, quantity);
            return ResponseEntity.ok(CartMapper.toResponse(cart));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Update cart item shipping option.
     */
    @PutMapping("/items/{cartItemId}/shipping")
    public ResponseEntity<?> updateShippingOption(
            @PathVariable UUID cartItemId,
            @RequestParam(required = false) UUID shippingOptionId,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        
        try {
            Cart cart = cartService.updateShippingOption(user.getId(), cartItemId, shippingOptionId);
            return ResponseEntity.ok(CartMapper.toResponse(cart));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Remove item from cart.
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable UUID cartItemId,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        
        try {
            Cart cart = cartService.removeFromCart(user.getId(), cartItemId);
            return ResponseEntity.ok(CartMapper.toResponse(cart));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Clear entire cart.
     */
    @DeleteMapping
    public ResponseEntity<CartResponse> clearCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Cart cart = cartService.clearCart(user.getId());
        return ResponseEntity.ok(CartMapper.toResponse(cart));
    }
    
    /**
     * Validate cart before checkout.
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        try {
            cartService.validateCart(user.getId());
            return ResponseEntity.ok().build();
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

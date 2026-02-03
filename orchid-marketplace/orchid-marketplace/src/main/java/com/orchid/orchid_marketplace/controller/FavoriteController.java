package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.ProductResponse;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.service.FavoriteService;
import com.orchid.orchid_marketplace.service.UserService;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/favorites")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Get all favorites for the authenticated user
     * GET /api/favorites
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getFavorites(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<ProductResponse> favorites = favoriteService.getFavoritesByUserId(user.getId());
        return ResponseEntity.ok(favorites);
    }
    
    /**
     * Add a product to favorites
     * POST /api/favorites/{productId}
     */
    @PostMapping("/{productId}")
    public ResponseEntity<ProductResponse> addFavorite(
            @PathVariable String productId,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        try {
            UUID productUuid = UUID.fromString(productId);
            ProductResponse product = favoriteService.addFavorite(user.getId(), productUuid);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (IllegalStateException e) {
            // Already favorited
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            // Invalid product ID or product not found
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Remove a product from favorites
     * DELETE /api/favorites/{productId}
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable String productId,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        try {
            UUID productUuid = UUID.fromString(productId);
            favoriteService.removeFavorite(user.getId(), productUuid);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Invalid product ID or favorite not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Check if a product is favorited
     * GET /api/favorites/check/{productId}
     */
    @GetMapping("/check/{productId}")
    public ResponseEntity<Boolean> checkFavorite(
            @PathVariable String productId,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(false);
        }
        
        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        try {
            UUID productUuid = UUID.fromString(productId);
            boolean isFavorited = favoriteService.isFavorited(user.getId(), productUuid);
            return ResponseEntity.ok(isFavorited);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

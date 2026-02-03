package com.orchid.orchid_marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.dto.ProductResponse;
import com.orchid.orchid_marketplace.mapper.ProductMapper;
import com.orchid.orchid_marketplace.model.Favorite;
import com.orchid.orchid_marketplace.model.Favorite.FavoriteId;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.FavoriteRepository;
import com.orchid.orchid_marketplace.repository.ProductRepository;
import com.orchid.orchid_marketplace.repository.UserRepository;

@Service
@Profile("!cosmos")
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all favorited products for a user
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getFavoritesByUserId(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        
        return favorites.stream()
                .map(favorite -> ProductMapper.toResponse(favorite.getProduct()))
                .collect(Collectors.toList());
    }
    
    /**
     * Add a product to user's favorites
     */
    @Transactional
    public ProductResponse addFavorite(UUID userId, UUID productId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        
        // Check if already favorited
        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new IllegalStateException("Product is already in favorites");
        }
        
        // Get user and product
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        // Create and save favorite
        Favorite favorite = new Favorite(user, product);
        favoriteRepository.save(favorite);
        
        return ProductMapper.toResponse(product);
    }
    
    /**
     * Remove a product from user's favorites
     */
    @Transactional
    public void removeFavorite(UUID userId, UUID productId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        
        FavoriteId id = new FavoriteId(userId, productId);
        
        if (!favoriteRepository.existsById(id)) {
            throw new IllegalArgumentException("Favorite not found");
        }
        
        favoriteRepository.deleteById(id);
    }
    
    /**
     * Check if a product is favorited by a user
     */
    @Transactional(readOnly = true)
    public boolean isFavorited(UUID userId, UUID productId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        
        return favoriteRepository.existsByUserIdAndProductId(userId, productId);
    }
    
    /**
     * Get count of favorites for a product
     */
    @Transactional(readOnly = true)
    public long getProductFavoriteCount(UUID productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return favoriteRepository.countByProductId(productId);
    }
    
    /**
     * Get count of total favorites for a user
     */
    @Transactional(readOnly = true)
    public long getUserFavoriteCount(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return favoriteRepository.countByUserId(userId);
    }
}


package com.orchid.orchid_marketplace.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.Cart;

/**
 * Repository for Cart entity operations.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    
    /**
     * Find cart by user ID.
     */
    Optional<Cart> findByUserId(UUID userId);
    
    /**
     * Delete cart by user ID.
     */
    void deleteByUserId(UUID userId);
    
    /**
     * Check if cart exists for user.
     */
    boolean existsByUserId(UUID userId);
}

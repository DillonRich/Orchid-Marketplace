package com.orchid.orchid_marketplace.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.CartItem;

/**
 * Repository for CartItem entity operations.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    
    /**
     * Find cart item by cart ID and product ID.
     */
    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);
    
    /**
     * Delete cart item by cart ID and product ID.
     */
    void deleteByCartIdAndProductId(UUID cartId, UUID productId);
}

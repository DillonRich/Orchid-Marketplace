package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.Favorite;
import com.orchid.orchid_marketplace.model.Favorite.FavoriteId;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    
    /**
     * Find all favorites for a specific user
     */
    @Query("SELECT f FROM Favorite f JOIN FETCH f.product WHERE f.user.id = :userId ORDER BY f.createdAt DESC")
    List<Favorite> findByUserId(@Param("userId") UUID userId);
    
    /**
     * Check if a product is favorited by a user
     */
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
    
    /**
     * Find a specific favorite
     */
    Optional<Favorite> findByUserIdAndProductId(UUID userId, UUID productId);
    
    /**
     * Count total favorites for a product
     */
    long countByProductId(UUID productId);
    
    /**
     * Count total favorites for a user
     */
    long countByUserId(UUID userId);
}

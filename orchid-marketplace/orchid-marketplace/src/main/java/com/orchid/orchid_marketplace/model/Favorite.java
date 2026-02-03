package com.orchid.orchid_marketplace.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Represents a user's favorited/liked product (wishlist).
 * Uses composite key of userId and productId.
 */
@Entity
@Table(name = "favorites")
public class Favorite {

    @EmbeddedId
    private FavoriteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Favorite() {
    }

    public Favorite(User user, Product product) {
        this.user = user;
        this.product = product;
        this.id = new FavoriteId(user.getId(), product.getId());
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public FavoriteId getId() {
        return id;
    }

    public void setId(FavoriteId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Composite primary key for Favorite entity
     */
    @Embeddable
    public static class FavoriteId implements Serializable {
        
        private static final long serialVersionUID = 1L;

        @Column(name = "user_id")
        private UUID userId;

        @Column(name = "product_id")
        private UUID productId;

        public FavoriteId() {
        }

        public FavoriteId(UUID userId, UUID productId) {
            this.userId = userId;
            this.productId = productId;
        }

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

        public UUID getProductId() {
            return productId;
        }

        public void setProductId(UUID productId) {
            this.productId = productId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof FavoriteId)) return false;
            FavoriteId that = (FavoriteId) o;
            return userId != null && userId.equals(that.userId) &&
                   productId != null && productId.equals(that.productId);
        }

        @Override
        public int hashCode() {
            return 31 * (userId != null ? userId.hashCode() : 0) + 
                   (productId != null ? productId.hashCode() : 0);
        }
    }
}

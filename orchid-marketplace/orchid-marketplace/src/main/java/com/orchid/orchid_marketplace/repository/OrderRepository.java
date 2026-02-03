package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByBuyerId(UUID buyerId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByBuyerIdAndStatus(UUID buyerId, Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.buyer.id = :buyerId AND o.isActive = true ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersByBuyer(@Param("buyerId") UUID buyerId);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.buyer.id = :buyerId AND o.isActive = true")
    long countOrdersByBuyer(@Param("buyerId") UUID buyerId);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status AND o.isActive = true")
    long countByStatus(@Param("status") Order.OrderStatus status);
    
    Optional<Order> findByStripePaymentIntentId(String stripePaymentIntentId);
    
    Optional<Order> findByStripeChargeId(String stripeChargeId);
}
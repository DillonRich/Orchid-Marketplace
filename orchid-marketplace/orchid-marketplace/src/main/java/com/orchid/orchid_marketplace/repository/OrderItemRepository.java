package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    
    List<OrderItem> findByOrderId(UUID orderId);
    
    List<OrderItem> findByProductId(UUID productId);
    
    List<OrderItem> findByStoreId(UUID storeId);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.isActive = true")
    List<OrderItem> findActiveByOrder(@Param("orderId") UUID orderId);
    
    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.isActive = true")
    long countByOrderId(@Param("orderId") UUID orderId);
    
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId AND oi.isActive = true")
    Long sumQuantityByProductId(@Param("productId") UUID productId);
}
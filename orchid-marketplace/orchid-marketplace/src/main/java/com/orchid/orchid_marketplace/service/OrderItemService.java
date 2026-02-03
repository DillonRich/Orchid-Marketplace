package com.orchid.orchid_marketplace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.OrderItem;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.repository.OrderItemRepository;
import com.orchid.orchid_marketplace.repository.OrderRepository;
import com.orchid.orchid_marketplace.repository.ProductRepository;

@Service
@Profile("!cosmos")
public class OrderItemService {
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;
    
    // Get all order items
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }
    
    // Get order item by ID
    public Optional<OrderItem> getOrderItemById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return orderItemRepository.findById(id);
    }
    
    // Get order items by order ID
    public List<OrderItem> getOrderItemsByOrderId(UUID orderId) {
        Objects.requireNonNull(orderId, "orderId must not be null");
        return orderItemRepository.findActiveByOrder(orderId);
    }
    
    // Get order items by product ID
    public List<OrderItem> getOrderItemsByProductId(UUID productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return orderItemRepository.findByProductId(productId);
    }
    
    // Get order items by store ID
    public List<OrderItem> getOrderItemsByStoreId(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return orderItemRepository.findByStoreId(storeId);
    }
    
    // Create a new order item
    public OrderItem createOrderItem(OrderItem orderItem) {
        Objects.requireNonNull(orderItem, "orderItem must not be null");
        // Calculate item total
        if (orderItem.getQuantity() != null && orderItem.getUnitPrice() != null) {
            orderItem.setItemTotal(
                orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
            );
        }
        
        return orderItemRepository.save(orderItem);
    }

    public OrderItem createOrderItem(UUID orderId, UUID productId, int quantity) {
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be >= 1");
        }

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (product.getStore() == null) {
            throw new RuntimeException("Product has no store; cannot create order item");
        }
        if (product.getPrice() == null) {
            throw new RuntimeException("Product has no price; cannot create order item");
        }

        OrderItem oi = new OrderItem();
        oi.setOrder(order);
        oi.setProduct(product);
        oi.setStore(product.getStore());
        oi.setQuantity(quantity);
        oi.setUnitPrice(product.getPrice());
        // item total recalculates via setters

        return createOrderItem(oi);
    }
    
    // Update an order item
    public OrderItem updateOrderItem(UUID id, OrderItem orderItemDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(orderItemDetails, "orderItemDetails must not be null");
        return orderItemRepository.findById(id)
            .map(existingOrderItem -> {
                // Update fields
                if (orderItemDetails.getQuantity() != null) {
                    existingOrderItem.setQuantity(orderItemDetails.getQuantity());
                }
                
                if (orderItemDetails.getUnitPrice() != null) {
                    existingOrderItem.setUnitPrice(orderItemDetails.getUnitPrice());
                }
                
                if (orderItemDetails.getActualShippingCost() != null) {
                    existingOrderItem.setActualShippingCost(orderItemDetails.getActualShippingCost());
                }
                
                if (orderItemDetails.getStatus() != null) {
                    existingOrderItem.setStatus(orderItemDetails.getStatus());
                }
                
                // Recalculate totals
                if (existingOrderItem.getQuantity() != null && existingOrderItem.getUnitPrice() != null) {
                    existingOrderItem.setItemTotal(
                        existingOrderItem.getUnitPrice().multiply(
                            BigDecimal.valueOf(existingOrderItem.getQuantity())
                        )
                    );
                }
                
                return orderItemRepository.save(existingOrderItem);
            })
            .orElseThrow(() -> new RuntimeException("OrderItem not found with ID: " + id));
    }
    
    // Soft delete an order item
    public void deleteOrderItem(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        orderItemRepository.findById(id)
            .ifPresentOrElse(
                orderItem -> {
                    orderItem.softDelete();
                    orderItemRepository.save(orderItem);
                },
                () -> { throw new RuntimeException("OrderItem not found with ID: " + id); }
            );
    }
    
    // Calculate total with shipping for an order item
    public BigDecimal calculateTotalWithShipping(UUID orderItemId) {
        Objects.requireNonNull(orderItemId, "orderItemId must not be null");
        return orderItemRepository.findById(orderItemId)
            .map(orderItem -> {
                BigDecimal itemTotal = orderItem.getItemTotal() != null ? 
                    orderItem.getItemTotal() : BigDecimal.ZERO;
                BigDecimal shippingCost = orderItem.getActualShippingCost() != null ? 
                    orderItem.getActualShippingCost() : BigDecimal.ZERO;
                return itemTotal.add(shippingCost);
            })
            .orElse(BigDecimal.ZERO);
    }
    
    // Update status of an order item
    public OrderItem updateOrderItemStatus(UUID id, OrderItem.OrderItemStatus status) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(status, "status must not be null");
        return orderItemRepository.findById(id)
            .map(orderItem -> { orderItem.setStatus(status); return orderItemRepository.save(orderItem); })
            .orElseThrow(() -> new RuntimeException("OrderItem not found with ID: " + id));
    }
    
    // Count items in an order
    public long countByOrderId(UUID orderId) {
        Objects.requireNonNull(orderId, "orderId must not be null");
        return orderItemRepository.countByOrderId(orderId);
    }
}
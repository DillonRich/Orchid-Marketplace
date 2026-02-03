package com.orchid.orchid_marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.repository.OrderRepository;

@Service
@Profile("!cosmos")
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    // Get order by ID
    public Optional<Order> getOrderById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return orderRepository.findById(id);
    }
    
    // Get order by order number
    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    // Get orders by buyer ID
    public List<Order> getOrdersByBuyerId(UUID buyerId) {
        Objects.requireNonNull(buyerId, "buyerId must not be null");
        return orderRepository.findByBuyerId(buyerId);
    }
    
    // Get orders by status
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    // Create a new order
    public Order createOrder(Order order) {
        Objects.requireNonNull(order, "order must not be null");
        // Generate order number if not provided
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            order.setOrderNumber(generateOrderNumber());
        }
        
        @SuppressWarnings("null")
        Order saved = orderRepository.save(order);
        return saved;
    }
    
    // Update an order
    public Order updateOrder(UUID id, Order orderDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(orderDetails, "orderDetails must not be null");
        return orderRepository.findById(id)
            .map(existingOrder -> {
                if (orderDetails.getStatus() != null) {
                    existingOrder.setStatus(orderDetails.getStatus());
                }
                
                if (orderDetails.getShippingAddress() != null) {
                    existingOrder.setShippingAddress(orderDetails.getShippingAddress());
                }
                
                if (orderDetails.getBillingAddress() != null) {
                    existingOrder.setBillingAddress(orderDetails.getBillingAddress());
                }
                
                if (orderDetails.getShippingAmount() != null) {
                    existingOrder.setShippingAmount(orderDetails.getShippingAmount());
                }
                
                if (orderDetails.getTaxAmount() != null) {
                    existingOrder.setTaxAmount(orderDetails.getTaxAmount());
                }

                @SuppressWarnings("null")
                Order saved = orderRepository.save(existingOrder);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }
    
    // Soft delete an order
    public void deleteOrder(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        orderRepository.findById(id)
            .ifPresentOrElse(
                order -> {
                    order.softDelete();
                    orderRepository.save(order);
                },
                () -> { throw new RuntimeException("Order not found with ID: " + id); }
            );
    }
    
    // Update order status
    public Order updateOrderStatus(UUID id, Order.OrderStatus status) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(status, "status must not be null");
        return orderRepository.findById(id)
            .map(order -> {
                order.setStatus(status);
                @SuppressWarnings("null")
                Order saved = orderRepository.save(order);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }
    
    // Get recent order for a buyer
    public Optional<Order> getRecentOrderForBuyer(UUID buyerId) {
        Objects.requireNonNull(buyerId, "buyerId must not be null");
        return orderRepository.findRecentOrdersByBuyer(buyerId)
            .stream()
            .findFirst();
    }
    
    // Count orders by buyer
    public long countOrdersByBuyer(UUID buyerId) {
        Objects.requireNonNull(buyerId, "buyerId must not be null");
        return orderRepository.countOrdersByBuyer(buyerId);
    }
    
    // Count orders by status
    public long countByStatus(Order.OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
    
    // Generate unique order number
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
}
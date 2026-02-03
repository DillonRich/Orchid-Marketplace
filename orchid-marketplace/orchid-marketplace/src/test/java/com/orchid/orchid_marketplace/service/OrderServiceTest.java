package com.orchid.orchid_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    
    @InjectMocks
    private OrderService orderService;
    
    private Order order;
    private User buyer;
    private UUID orderId;
    private UUID buyerId;
    
    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        buyerId = UUID.randomUUID();
        
        buyer = new User();
        buyer.setId(buyerId);
        buyer.setEmail("buyer@test.com");
        
        order = new Order("ORD-001", buyer);
        order.setId(orderId);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setSubtotal(new BigDecimal("100.00"));
        order.setTaxAmount(new BigDecimal("10.00"));
        order.setShippingAmount(new BigDecimal("5.00"));
        order.setTotalAmount(new BigDecimal("115.00"));
    }
    
    @Test
    void testGetAllOrders() {
        List<Order> orders = List.of(order);
        when(orderRepository.findAll()).thenReturn(orders);
        
        List<Order> result = orderService.getAllOrders();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }
    
    @Test
    void testGetOrderById() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        
        Optional<Order> result = orderService.getOrderById(orderId);
        
        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getId());
        verify(orderRepository, times(1)).findById(orderId);
    }
    
    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        
        Optional<Order> result = orderService.getOrderById(orderId);
        
        assertFalse(result.isPresent());
        verify(orderRepository, times(1)).findById(orderId);
    }
    
    @Test
    void testGetOrderByOrderNumber() {
        when(orderRepository.findByOrderNumber("ORD-001")).thenReturn(Optional.of(order));
        
        Optional<Order> result = orderService.getOrderByOrderNumber("ORD-001");
        
        assertTrue(result.isPresent());
        assertEquals("ORD-001", result.get().getOrderNumber());
        verify(orderRepository, times(1)).findByOrderNumber("ORD-001");
    }
    
    @Test
    void testGetOrdersByBuyerId() {
        List<Order> orders = List.of(order);
        when(orderRepository.findByBuyerId(buyerId)).thenReturn(orders);
        
        List<Order> result = orderService.getOrdersByBuyerId(buyerId);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByBuyerId(buyerId);
    }
    
    @Test
    void testGetOrdersByStatus() {
        List<Order> orders = List.of(order);
        when(orderRepository.findByStatus(Order.OrderStatus.PENDING)).thenReturn(orders);
        
        List<Order> result = orderService.getOrdersByStatus(Order.OrderStatus.PENDING);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByStatus(Order.OrderStatus.PENDING);
    }
    
    @Test
    void testCreateOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        
        Order result = orderService.createOrder(order);
        
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    void testCreateOrderNullThrowsException() {
        assertThrows(NullPointerException.class, () -> orderService.createOrder(null));
    }
    
    @Test
    void testGetOrderByIdNullThrowsException() {
        assertThrows(NullPointerException.class, () -> orderService.getOrderById(null));
    }
    
    @Test
    void testGetOrdersByBuyerIdNullThrowsException() {
        assertThrows(NullPointerException.class, () -> orderService.getOrdersByBuyerId(null));
    }
    
    @Test
    void testOrderTotalCalculation() {
        BigDecimal expectedTotal = new BigDecimal("115.00");
        order.setTotalAmount(expectedTotal);
        
        assertEquals(expectedTotal, order.getTotalAmount());
    }
    
    @Test
    void testOrderStatusTransition() {
        order.setStatus(Order.OrderStatus.PROCESSING);
        assertEquals(Order.OrderStatus.PROCESSING, order.getStatus());
        
        order.setStatus(Order.OrderStatus.SHIPPED);
        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());
    }
}

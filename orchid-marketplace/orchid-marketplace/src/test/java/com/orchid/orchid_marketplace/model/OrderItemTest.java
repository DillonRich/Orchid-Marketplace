package com.orchid.orchid_marketplace.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderItemTest {
    
    private OrderItem orderItem;
    private UUID orderItemId;
    
    @BeforeEach
    void setUp() {
        orderItemId = UUID.randomUUID();
        orderItem = new OrderItem();
        orderItem.setId(orderItemId);
    }
    
    @Test
    void testOrderItemIdGetterSetter() {
        assertEquals(orderItemId, orderItem.getId());
    }
    
    @Test
    void testQuantityGetterSetter() {
        orderItem.setQuantity(3);
        assertEquals(3, orderItem.getQuantity());
    }
    
    @Test
    void testUnitPriceGetterSetter() {
        BigDecimal price = new BigDecimal("99.99");
        orderItem.setUnitPrice(price);
        assertEquals(price, orderItem.getUnitPrice());
    }
    
    @Test
    void testItemTotalGetterSetter() {
        BigDecimal total = new BigDecimal("299.97");
        orderItem.setItemTotal(total);
        assertEquals(total, orderItem.getItemTotal());
    }
    
    @Test
    void testSellerPayoutGetterSetter() {
        BigDecimal payout = new BigDecimal("270.00");
        orderItem.setSellerPayout(payout);
        assertEquals(payout, orderItem.getSellerPayout());
    }
    
    @Test
    void testActualShippingCostGetterSetter() {
        BigDecimal cost = new BigDecimal("15.50");
        orderItem.setActualShippingCost(cost);
        assertEquals(cost, orderItem.getActualShippingCost());
    }
    
    @Test
    void testStatusGetterSetter() {
        orderItem.setStatus(OrderItem.OrderItemStatus.PROCESSING);
        assertEquals(OrderItem.OrderItemStatus.PROCESSING, orderItem.getStatus());
    }
    
    @Test
    void testStripeTransferIdGetterSetter() {
        orderItem.setStripeTransferId("tr_123");
        assertEquals("tr_123", orderItem.getStripeTransferId());
    }
    
    @Test
    void testStripePayoutIdGetterSetter() {
        orderItem.setStripePayoutId("po_456");
        assertEquals("po_456", orderItem.getStripePayoutId());
    }
    
    @Test
    void testProcessingQueueIdGetterSetter() {
        orderItem.setProcessingQueueId("queue-789");
        assertEquals("queue-789", orderItem.getProcessingQueueId());
    }
    
    @Test
    void testAllOrderItemStatuses() {
        orderItem.setStatus(OrderItem.OrderItemStatus.PENDING);
        assertEquals(OrderItem.OrderItemStatus.PENDING, orderItem.getStatus());
        
        orderItem.setStatus(OrderItem.OrderItemStatus.SHIPPED);
        assertEquals(OrderItem.OrderItemStatus.SHIPPED, orderItem.getStatus());
        
        orderItem.setStatus(OrderItem.OrderItemStatus.DELIVERED);
        assertEquals(OrderItem.OrderItemStatus.DELIVERED, orderItem.getStatus());
        
        orderItem.setStatus(OrderItem.OrderItemStatus.CANCELLED);
        assertEquals(OrderItem.OrderItemStatus.CANCELLED, orderItem.getStatus());
    }
    
    @Test
    void testOrderItemConstructorWithParameters() {
        Order order = new Order();
        Product product = new Product();
        Store store = new Store();
        BigDecimal price = new BigDecimal("50.00");
        
        OrderItem item = new OrderItem(order, product, store, 2, price);
        assertEquals(2, item.getQuantity());
        assertEquals(price, item.getUnitPrice());
    }
}

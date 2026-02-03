package com.orchid.orchid_marketplace.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DTOComprehensiveTest3 {

    // OrderRequest Tests
    @Test
    void testOrderRequest_RequiredFields() {
        OrderRequest req = new OrderRequest();
        UUID buyerId = UUID.randomUUID();
        
        req.setBuyerId(buyerId);
        
        assertEquals(buyerId, req.getBuyerId());
    }

    @Test
    void testOrderRequest_AllFields() {
        OrderRequest req = new OrderRequest();
        UUID buyerId = UUID.randomUUID();
        UUID shippingId = UUID.randomUUID();
        UUID billingId = UUID.randomUUID();
        
        req.setBuyerId(buyerId);
        req.setShippingAddressId(shippingId);
        req.setBillingAddressId(billingId);
        
        assertEquals(buyerId, req.getBuyerId());
        assertEquals(shippingId, req.getShippingAddressId());
        assertEquals(billingId, req.getBillingAddressId());
    }

    @Test
    void testOrderRequest_SameShippingAndBilling() {
        OrderRequest req = new OrderRequest();
        UUID buyerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        
        req.setBuyerId(buyerId);
        req.setShippingAddressId(addressId);
        req.setBillingAddressId(addressId);
        
        assertEquals(addressId, req.getShippingAddressId());
        assertEquals(addressId, req.getBillingAddressId());
    }

    // OrderResponse Tests
    @Test
    void testOrderResponse_AllFields() {
        OrderResponse res = new OrderResponse();
        UUID id = UUID.randomUUID();
        UUID buyerId = UUID.randomUUID();
        String orderNumber = "ORD-12345";
        LocalDateTime createdAt = LocalDateTime.now();
        
        res.setId(id);
        res.setOrderNumber(orderNumber);
        res.setBuyerId(buyerId);
        res.setCreatedAt(createdAt);
        
        assertEquals(id, res.getId());
        assertEquals(orderNumber, res.getOrderNumber());
        assertEquals(buyerId, res.getBuyerId());
        assertEquals(createdAt, res.getCreatedAt());
    }

    @Test
    void testOrderResponse_MinimalFields() {
        OrderResponse res = new OrderResponse();
        UUID id = UUID.randomUUID();
        
        res.setId(id);
        
        assertEquals(id, res.getId());
        assertNull(res.getOrderNumber());
        assertNull(res.getBuyerId());
        assertNull(res.getCreatedAt());
    }

    // UserRequest Tests
    @Test
    void testUserRequest_RequiredFields() {
        UserRequest req = new UserRequest();
        
        req.setEmail("user@example.com");
        req.setPassword("password123");
        req.setFullName("John Doe");
        
        assertEquals("user@example.com", req.getEmail());
        assertEquals("password123", req.getPassword());
        assertEquals("John Doe", req.getFullName());
    }

    @Test
    void testUserRequest_AllFields() {
        UserRequest req = new UserRequest();
        
        req.setEmail("seller@example.com");
        req.setPassword("securepass");
        req.setFullName("Jane Smith");
        req.setPhoneNumber("+1234567890");
        req.setRole("SELLER");
        
        assertEquals("seller@example.com", req.getEmail());
        assertEquals("securepass", req.getPassword());
        assertEquals("Jane Smith", req.getFullName());
        assertEquals("+1234567890", req.getPhoneNumber());
        assertEquals("SELLER", req.getRole());
    }

    @Test
    void testUserRequest_OptionalFieldsNull() {
        UserRequest req = new UserRequest();
        
        req.setEmail("buyer@example.com");
        req.setPassword("mypassword");
        req.setFullName("Bob Jones");
        
        assertNull(req.getPhoneNumber());
        assertNull(req.getRole());
    }

    @Test
    void testUserRequest_MinimumPasswordLength() {
        UserRequest req = new UserRequest();
        
        req.setPassword("123456"); // Exactly 6 characters (min)
        
        assertEquals("123456", req.getPassword());
        assertEquals(6, req.getPassword().length());
    }

    // UserResponse Tests
    @Test
    void testUserResponse_AllFields() {
        UserResponse res = new UserResponse();
        UUID id = UUID.randomUUID();
        
        res.setId(id);
        res.setEmail("user@example.com");
        res.setFullName("John Doe");
        res.setPhoneNumber("+1234567890");
        res.setRole("BUYER");
        res.setIsActive(true);
        
        assertEquals(id, res.getId());
        assertEquals("user@example.com", res.getEmail());
        assertEquals("John Doe", res.getFullName());
        assertEquals("+1234567890", res.getPhoneNumber());
        assertEquals("BUYER", res.getRole());
        assertTrue(res.getIsActive());
    }

    @Test
    void testUserResponse_InactiveUser() {
        UserResponse res = new UserResponse();
        UUID id = UUID.randomUUID();
        
        res.setId(id);
        res.setEmail("inactive@example.com");
        res.setFullName("Inactive User");
        res.setIsActive(false);
        
        assertEquals(id, res.getId());
        assertFalse(res.getIsActive());
    }

    @Test
    void testUserResponse_NoPhoneNumber() {
        UserResponse res = new UserResponse();
        UUID id = UUID.randomUUID();
        
        res.setId(id);
        res.setEmail("nophone@example.com");
        res.setFullName("No Phone");
        res.setRole("BUYER");
        
        assertNull(res.getPhoneNumber());
    }

    // OrderItemRequest Tests
    @Test
    void testOrderItemRequest_AllFields() {
        OrderItemRequest req = new OrderItemRequest();
        UUID productId = UUID.randomUUID();
        
        req.setProductId(productId);
        req.setQuantity(5);
        
        assertEquals(productId, req.getProductId());
        assertEquals(5, req.getQuantity());
    }

    @Test
    void testOrderItemRequest_MinimumQuantity() {
        OrderItemRequest req = new OrderItemRequest();
        UUID productId = UUID.randomUUID();
        
        req.setProductId(productId);
        req.setQuantity(1);
        
        assertEquals(1, req.getQuantity());
    }

    @Test
    void testOrderItemRequest_LargeQuantity() {
        OrderItemRequest req = new OrderItemRequest();
        UUID productId = UUID.randomUUID();
        
        req.setProductId(productId);
        req.setQuantity(1000);
        
        assertEquals(1000, req.getQuantity());
    }

    // OrderItemResponse Tests
    @Test
    void testOrderItemResponse_AllFields() {
        OrderItemResponse res = new OrderItemResponse();
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        
        res.setId(id);
        res.setProductId(productId);
        res.setQuantity(3);
        
        assertEquals(id, res.getId());
        assertEquals(productId, res.getProductId());
        assertEquals(3, res.getQuantity());
    }

    @Test
    void testOrderItemResponse_SingleItem() {
        OrderItemResponse res = new OrderItemResponse();
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        
        res.setId(id);
        res.setProductId(productId);
        res.setQuantity(1);
        
        assertEquals(1, res.getQuantity());
    }

    @Test
    void testOrderItemResponse_NullQuantity() {
        OrderItemResponse res = new OrderItemResponse();
        UUID id = UUID.randomUUID();
        
        res.setId(id);
        
        assertNull(res.getQuantity());
    }
}

package com.orchid.orchid_marketplace.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DTOComprehensiveTest4 {

    // PaymentMethodRequest Tests
    @Test
    void testPaymentMethodRequest_AllFields() {
        PaymentMethodRequest req = new PaymentMethodRequest();
        UUID userId = UUID.randomUUID();
        
        req.setUserId(userId);
        req.setBrand("Visa");
        req.setLast4("4242");
        req.setExpMonth(12);
        req.setExpYear(2025);
        
        assertEquals(userId, req.getUserId());
        assertEquals("Visa", req.getBrand());
        assertEquals("4242", req.getLast4());
        assertEquals(12, req.getExpMonth());
        assertEquals(2025, req.getExpYear());
    }

    @Test
    void testPaymentMethodRequest_MinimalFields() {
        PaymentMethodRequest req = new PaymentMethodRequest();
        UUID userId = UUID.randomUUID();
        
        req.setUserId(userId);
        
        assertEquals(userId, req.getUserId());
        assertNull(req.getBrand());
    }

    @Test
    void testPaymentMethodRequest_JanuaryExpiration() {
        PaymentMethodRequest req = new PaymentMethodRequest();
        
        req.setExpMonth(1);
        
        assertEquals(1, req.getExpMonth());
    }

    // PaymentMethodResponse Tests
    @Test
    void testPaymentMethodResponse_AllFields() {
        PaymentMethodResponse res = new PaymentMethodResponse();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        res.setId(id);
        res.setUserId(userId);
        res.setBrand("Mastercard");
        res.setLast4("8888");
        res.setExpMonth(6);
        res.setExpYear(2026);
        
        assertEquals(id, res.getId());
        assertEquals(userId, res.getUserId());
        assertEquals("Mastercard", res.getBrand());
        assertEquals("8888", res.getLast4());
        assertEquals(6, res.getExpMonth());
        assertEquals(2026, res.getExpYear());
    }

    @Test
    void testPaymentMethodResponse_ExpiredCard() {
        PaymentMethodResponse res = new PaymentMethodResponse();
        
        res.setExpMonth(12);
        res.setExpYear(2020);
        
        assertEquals(12, res.getExpMonth());
        assertEquals(2020, res.getExpYear());
    }

    // AddressRequest Tests
    @Test
    void testAddressRequest_RequiredFields() {
        AddressRequest req = new AddressRequest();
        UUID userId = UUID.randomUUID();
        
        req.setUserId(userId);
        req.setStreetAddress("123 Main St");
        
        assertEquals(userId, req.getUserId());
        assertEquals("123 Main St", req.getStreetAddress());
    }

    @Test
    void testAddressRequest_FullAddress() {
        AddressRequest req = new AddressRequest();
        UUID userId = UUID.randomUUID();
        
        req.setUserId(userId);
        req.setStreetAddress("456 Oak Ave");
        req.setCity("Springfield");
        req.setState("IL");
        req.setCountry("USA");
        req.setPostalCode("62701");
        req.setRecipientName("John Doe");
        req.setPhoneNumber("+1234567890");
        req.setAddressType("SHIPPING");
        req.setIsDefault(true);
        
        assertEquals(userId, req.getUserId());
        assertEquals("456 Oak Ave", req.getStreetAddress());
        assertEquals("Springfield", req.getCity());
        assertEquals("IL", req.getState());
        assertEquals("USA", req.getCountry());
        assertEquals("62701", req.getPostalCode());
        assertEquals("John Doe", req.getRecipientName());
        assertEquals("+1234567890", req.getPhoneNumber());
        assertEquals("SHIPPING", req.getAddressType());
        assertTrue(req.getIsDefault());
    }

    @Test
    void testAddressRequest_BillingAddress() {
        AddressRequest req = new AddressRequest();
        UUID userId = UUID.randomUUID();
        
        req.setUserId(userId);
        req.setStreetAddress("789 Elm St");
        req.setAddressType("BILLING");
        req.setIsDefault(false);
        
        assertEquals("BILLING", req.getAddressType());
        assertFalse(req.getIsDefault());
    }

    @Test
    void testAddressRequest_BothAddressType() {
        AddressRequest req = new AddressRequest();
        UUID userId = UUID.randomUUID();
        
        req.setUserId(userId);
        req.setStreetAddress("100 Main St");
        req.setAddressType("BOTH");
        
        assertEquals("BOTH", req.getAddressType());
    }

    // AddressResponse Tests
    @Test
    void testAddressResponse_FullAddress() {
        AddressResponse res = new AddressResponse();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        res.setId(id);
        res.setUserId(userId);
        res.setStreetAddress("321 Pine St");
        res.setCity("Chicago");
        res.setState("IL");
        res.setCountry("USA");
        res.setPostalCode("60601");
        res.setRecipientName("Jane Smith");
        res.setPhoneNumber("+9876543210");
        res.setAddressType("SHIPPING");
        res.setIsDefault(true);
        
        assertEquals(id, res.getId());
        assertEquals(userId, res.getUserId());
        assertEquals("321 Pine St", res.getStreetAddress());
        assertEquals("Chicago", res.getCity());
        assertEquals("IL", res.getState());
        assertEquals("USA", res.getCountry());
        assertEquals("60601", res.getPostalCode());
        assertEquals("Jane Smith", res.getRecipientName());
        assertEquals("+9876543210", res.getPhoneNumber());
        assertEquals("SHIPPING", res.getAddressType());
        assertTrue(res.getIsDefault());
    }

    @Test
    void testAddressResponse_MinimalFields() {
        AddressResponse res = new AddressResponse();
        UUID id = UUID.randomUUID();
        
        res.setId(id);
        res.setStreetAddress("999 Test St");
        
        assertEquals(id, res.getId());
        assertEquals("999 Test St", res.getStreetAddress());
        assertNull(res.getCity());
        assertNull(res.getState());
    }

    // CartItemResponse Tests
    @Test
    void testCartItemResponse_AllFields() {
        CartItemResponse res = new CartItemResponse();
        UUID cartItemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID shippingOptionId = UUID.randomUUID();
        
        res.setCartItemId(cartItemId);
        res.setProductId(productId);
        res.setProductName("Test Product");
        res.setProductDescription("Description");
        res.setQuantity(2);
        res.setUnitPrice(new BigDecimal("19.99"));
        res.setLineTotal(new BigDecimal("39.98"));
        res.setShippingOptionId(shippingOptionId);
        res.setShippingMethod("Standard");
        res.setShippingCost(new BigDecimal("5.00"));
        res.setStockAvailable(100);
        
        assertEquals(cartItemId, res.getCartItemId());
        assertEquals(productId, res.getProductId());
        assertEquals("Test Product", res.getProductName());
        assertEquals("Description", res.getProductDescription());
        assertEquals(2, res.getQuantity());
        assertEquals(new BigDecimal("19.99"), res.getUnitPrice());
        assertEquals(new BigDecimal("39.98"), res.getLineTotal());
        assertEquals(shippingOptionId, res.getShippingOptionId());
        assertEquals("Standard", res.getShippingMethod());
        assertEquals(new BigDecimal("5.00"), res.getShippingCost());
        assertEquals(100, res.getStockAvailable());
    }

    @Test
    void testCartItemResponse_LowStock() {
        CartItemResponse res = new CartItemResponse();
        
        res.setQuantity(5);
        res.setStockAvailable(3);
        
        assertEquals(5, res.getQuantity());
        assertEquals(3, res.getStockAvailable());
        assertTrue(res.getQuantity() > res.getStockAvailable());
    }

    @Test
    void testCartItemResponse_FreeShipping() {
        CartItemResponse res = new CartItemResponse();
        
        res.setShippingMethod("Free");
        res.setShippingCost(BigDecimal.ZERO);
        
        assertEquals("Free", res.getShippingMethod());
        assertEquals(BigDecimal.ZERO, res.getShippingCost());
    }

    // CartResponse Tests
    @Test
    void testCartResponse_EmptyCart() {
        CartResponse res = new CartResponse();
        
        res.setItems(new ArrayList<>());
        res.setSubtotal(BigDecimal.ZERO);
        res.setTax(BigDecimal.ZERO);
        res.setShippingCost(BigDecimal.ZERO);
        res.setTotal(BigDecimal.ZERO);
        res.setItemCount(0);
        
        assertTrue(res.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, res.getSubtotal());
        assertEquals(BigDecimal.ZERO, res.getTax());
        assertEquals(BigDecimal.ZERO, res.getShippingCost());
        assertEquals(BigDecimal.ZERO, res.getTotal());
        assertEquals(0, res.getItemCount());
    }

    @Test
    void testCartResponse_WithItems() {
        CartResponse res = new CartResponse();
        CartItemResponse item1 = new CartItemResponse();
        item1.setProductName("Product 1");
        item1.setQuantity(2);
        
        List<CartItemResponse> items = new ArrayList<>();
        items.add(item1);
        
        res.setItems(items);
        res.setSubtotal(new BigDecimal("100.00"));
        res.setTax(new BigDecimal("8.00"));
        res.setShippingCost(new BigDecimal("10.00"));
        res.setTotal(new BigDecimal("118.00"));
        res.setItemCount(2);
        
        assertEquals(1, res.getItems().size());
        assertEquals("Product 1", res.getItems().get(0).getProductName());
        assertEquals(new BigDecimal("100.00"), res.getSubtotal());
        assertEquals(new BigDecimal("8.00"), res.getTax());
        assertEquals(new BigDecimal("10.00"), res.getShippingCost());
        assertEquals(new BigDecimal("118.00"), res.getTotal());
        assertEquals(2, res.getItemCount());
    }

    @Test
    void testCartResponse_MultipleItems() {
        CartResponse res = new CartResponse();
        List<CartItemResponse> items = new ArrayList<>();
        
        CartItemResponse item1 = new CartItemResponse();
        item1.setQuantity(2);
        CartItemResponse item2 = new CartItemResponse();
        item2.setQuantity(1);
        CartItemResponse item3 = new CartItemResponse();
        item3.setQuantity(3);
        
        items.add(item1);
        items.add(item2);
        items.add(item3);
        
        res.setItems(items);
        res.setItemCount(6);
        
        assertEquals(3, res.getItems().size());
        assertEquals(6, res.getItemCount());
    }
}

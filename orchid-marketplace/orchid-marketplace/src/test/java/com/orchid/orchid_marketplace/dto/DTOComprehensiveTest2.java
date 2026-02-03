package com.orchid.orchid_marketplace.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Additional DTO tests - Part 2
 * Covers Store, Product, Conversation, and Message DTOs
 */
class DTOComprehensiveTest2 {

    @Test
    void testStoreRequest_RequiredFields() {
        StoreRequest dto = new StoreRequest();
        UUID sellerId = UUID.randomUUID();
        dto.setSellerId(sellerId);
        dto.setStoreName("My Store");
        dto.setSlug("my-store");
        
        assertEquals(sellerId, dto.getSellerId());
        assertEquals("My Store", dto.getStoreName());
        assertEquals("my-store", dto.getSlug());
    }

    @Test
    void testStoreRequest_OptionalFields() {
        StoreRequest dto = new StoreRequest();
        dto.setProfileImageUrl("https://example.com/profile.jpg");
        dto.setBannerImageUrl("https://example.com/banner.jpg");
        dto.setAboutText("About our store");
        dto.setReturnPolicyText("30 day returns");
        dto.setIsPublic(true);
        
        assertEquals("https://example.com/profile.jpg", dto.getProfileImageUrl());
        assertEquals("https://example.com/banner.jpg", dto.getBannerImageUrl());
        assertEquals("About our store", dto.getAboutText());
        assertEquals("30 day returns", dto.getReturnPolicyText());
        assertTrue(dto.getIsPublic());
    }

    @Test
    void testStoreResponse_AllFields() {
        StoreResponse dto = new StoreResponse();
        UUID id = UUID.randomUUID();
        UUID sellerId = UUID.randomUUID();
        
        dto.setId(id);
        dto.setSellerId(sellerId);
        dto.setStoreName("Tech Shop");
        dto.setSlug("tech-shop");
        dto.setProfileImageUrl("profile.jpg");
        dto.setBannerImageUrl("banner.jpg");
        dto.setAboutText("We sell tech");
        dto.setReturnPolicyText("Returns accepted");
        dto.setTotalSales(100);
        dto.setAverageRating(new BigDecimal("4.5"));
        dto.setIsPublic(true);
        
        assertEquals(id, dto.getId());
        assertEquals(sellerId, dto.getSellerId());
        assertEquals("Tech Shop", dto.getStoreName());
        assertEquals("tech-shop", dto.getSlug());
        assertEquals("profile.jpg", dto.getProfileImageUrl());
        assertEquals("banner.jpg", dto.getBannerImageUrl());
        assertEquals("We sell tech", dto.getAboutText());
        assertEquals("Returns accepted", dto.getReturnPolicyText());
        assertEquals(100, dto.getTotalSales());
        assertEquals(new BigDecimal("4.5"), dto.getAverageRating());
        assertTrue(dto.getIsPublic());
    }

    @Test
    void testStoreResponse_PrivateStore() {
        StoreResponse dto = new StoreResponse();
        dto.setStoreName("Private Store");
        dto.setIsPublic(false);
        dto.setTotalSales(0);
        
        assertFalse(dto.getIsPublic());
        assertEquals(0, dto.getTotalSales());
    }

    @Test
    void testProductRequest_RequiredFields() {
        ProductRequest dto = new ProductRequest();
        dto.setName("Laptop");
        dto.setPrice(999.99);
        dto.setStock(10);
        
        assertEquals("Laptop", dto.getName());
        assertEquals(999.99, dto.getPrice());
        assertEquals(10, dto.getStock());
    }

    @Test
    void testProductRequest_WithDescription() {
        ProductRequest dto = new ProductRequest();
        dto.setName("Mouse");
        dto.setDescription("Wireless gaming mouse");
        dto.setPrice(49.99);
        dto.setStock(50);
        
        assertEquals("Wireless gaming mouse", dto.getDescription());
    }

    @Test
    void testProductRequest_WithCategoryAndStore() {
        ProductRequest dto = new ProductRequest();
        UUID categoryId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        
        dto.setName("Keyboard");
        dto.setPrice(79.99);
        dto.setStock(25);
        dto.setCategoryId(categoryId);
        dto.setStoreId(storeId);
        
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals(storeId, dto.getStoreId());
    }

    @Test
    void testProductRequest_ZeroPrice() {
        ProductRequest dto = new ProductRequest();
        dto.setName("Free Item");
        dto.setPrice(0.0);
        dto.setStock(100);
        
        assertEquals(0.0, dto.getPrice());
    }

    @Test
    void testProductRequest_ZeroStock() {
        ProductRequest dto = new ProductRequest();
        dto.setName("Out of Stock");
        dto.setPrice(29.99);
        dto.setStock(0);
        
        assertEquals(0, dto.getStock());
    }

    @Test
    void testProductResponse_AllFields() {
        ProductResponse dto = new ProductResponse();
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        
        dto.setId(id);
        dto.setTitle("Gaming Mouse");
        dto.setDescription("RGB gaming mouse");
        dto.setPrice(59.99);
        dto.setStock(15);
        dto.setCategoryId(categoryId);
        dto.setStoreId(storeId);
        
        assertEquals(id, dto.getId());
        assertEquals("Gaming Mouse", dto.getTitle());
        assertEquals("RGB gaming mouse", dto.getDescription());
        assertEquals(59.99, dto.getPrice());
        assertEquals(15, dto.getStock());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals(storeId, dto.getStoreId());
    }

    @Test
    void testProductResponse_MinimalFields() {
        ProductResponse dto = new ProductResponse();
        UUID id = UUID.randomUUID();
        
        dto.setId(id);
        dto.setTitle("Basic Product");
        dto.setPrice(10.00);
        dto.setStock(1);
        
        assertEquals(id, dto.getId());
        assertEquals("Basic Product", dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getCategoryId());
        assertNull(dto.getStoreId());
    }

    @Test
    void testConversationResponse_NoArgConstructor() {
        ConversationResponse dto = new ConversationResponse();
        assertNotNull(dto);
    }

    @Test
    void testConversationResponse_BuyerSellerFields() {
        ConversationResponse dto = new ConversationResponse();
        UUID buyerId = UUID.randomUUID();
        UUID sellerId = UUID.randomUUID();
        
        dto.setBuyerId(buyerId);
        dto.setBuyerName("John Buyer");
        dto.setBuyerEmail("buyer@example.com");
        dto.setSellerId(sellerId);
        dto.setSellerName("Jane Seller");
        dto.setSellerEmail("seller@example.com");
        
        assertEquals(buyerId, dto.getBuyerId());
        assertEquals("John Buyer", dto.getBuyerName());
        assertEquals("buyer@example.com", dto.getBuyerEmail());
        assertEquals(sellerId, dto.getSellerId());
        assertEquals("Jane Seller", dto.getSellerName());
        assertEquals("seller@example.com", dto.getSellerEmail());
    }

    @Test
    void testConversationResponse_OrderFields() {
        ConversationResponse dto = new ConversationResponse();
        UUID orderId = UUID.randomUUID();
        
        dto.setOrderId(orderId);
        dto.setOrderNumber("ORD-12345");
        
        assertEquals(orderId, dto.getOrderId());
        assertEquals("ORD-12345", dto.getOrderNumber());
    }

    @Test
    void testConversationResponse_MessageMetadata() {
        ConversationResponse dto = new ConversationResponse();
        LocalDateTime now = LocalDateTime.now();
        
        dto.setMessageCount(5);
        dto.setLastMessageAt(now);
        dto.setIsArchived(false);
        
        assertEquals(5, dto.getMessageCount());
        assertEquals(now, dto.getLastMessageAt());
        assertFalse(dto.getIsArchived());
    }

    @Test
    void testConversationResponse_WithMessages() {
        ConversationResponse dto = new ConversationResponse();
        MessageResponse msg1 = new MessageResponse();
        msg1.setContent("Hello");
        List<MessageResponse> messages = List.of(msg1);
        
        dto.setMessages(messages);
        
        assertNotNull(dto.getMessages());
        assertEquals(1, dto.getMessages().size());
        assertEquals("Hello", dto.getMessages().get(0).getContent());
    }

    @Test
    void testConversationResponse_ArchivedConversation() {
        ConversationResponse dto = new ConversationResponse();
        dto.setIsArchived(true);
        dto.setMessageCount(10);
        
        assertTrue(dto.getIsArchived());
        assertEquals(10, dto.getMessageCount());
    }

    @Test
    void testMessageResponse_NoArgConstructor() {
        MessageResponse dto = new MessageResponse();
        assertNotNull(dto);
    }

    @Test
    void testMessageResponse_AllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime readAt = now.plusMinutes(5);
        
        MessageResponse dto = new MessageResponse(
            id, conversationId, senderId, "John Doe", "john@example.com",
            "Hello, I have a question", false, null, true, readAt, now, now
        );
        
        assertEquals(id, dto.getId());
        assertEquals(conversationId, dto.getConversationId());
        assertEquals(senderId, dto.getSenderId());
        assertEquals("John Doe", dto.getSenderName());
        assertEquals("john@example.com", dto.getSenderEmail());
        assertEquals("Hello, I have a question", dto.getContent());
        assertFalse(dto.getHasFlaggedKeywords());
        assertNull(dto.getFlaggedKeywords());
        assertTrue(dto.getIsRead());
        assertEquals(readAt, dto.getReadAt());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void testMessageResponse_GettersSetters() {
        MessageResponse dto = new MessageResponse();
        UUID id = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        dto.setId(id);
        dto.setConversationId(conversationId);
        dto.setSenderId(senderId);
        dto.setSenderName("Jane");
        dto.setSenderEmail("jane@test.com");
        dto.setContent("Test message");
        dto.setHasFlaggedKeywords(false);
        dto.setFlaggedKeywords(null);
        dto.setIsRead(false);
        dto.setReadAt(null);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        
        assertEquals(id, dto.getId());
        assertEquals(conversationId, dto.getConversationId());
        assertEquals(senderId, dto.getSenderId());
        assertEquals("Jane", dto.getSenderName());
        assertEquals("jane@test.com", dto.getSenderEmail());
        assertEquals("Test message", dto.getContent());
        assertFalse(dto.getHasFlaggedKeywords());
        assertNull(dto.getFlaggedKeywords());
        assertFalse(dto.getIsRead());
        assertNull(dto.getReadAt());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void testMessageResponse_FlaggedContent() {
        MessageResponse dto = new MessageResponse();
        dto.setContent("Spam content here");
        dto.setHasFlaggedKeywords(true);
        dto.setFlaggedKeywords("spam, inappropriate");
        
        assertEquals("Spam content here", dto.getContent());
        assertTrue(dto.getHasFlaggedKeywords());
        assertEquals("spam, inappropriate", dto.getFlaggedKeywords());
    }

    @Test
    void testMessageResponse_UnreadMessage() {
        MessageResponse dto = new MessageResponse();
        dto.setIsRead(false);
        dto.setReadAt(null);
        
        assertFalse(dto.getIsRead());
        assertNull(dto.getReadAt());
    }

    @Test
    void testMessageResponse_ReadMessage() {
        MessageResponse dto = new MessageResponse();
        LocalDateTime readTime = LocalDateTime.now();
        
        dto.setIsRead(true);
        dto.setReadAt(readTime);
        
        assertTrue(dto.getIsRead());
        assertEquals(readTime, dto.getReadAt());
    }
}

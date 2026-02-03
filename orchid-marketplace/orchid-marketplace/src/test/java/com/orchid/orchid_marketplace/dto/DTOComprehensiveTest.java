package com.orchid.orchid_marketplace.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Comprehensive DTO tests for data transfer objects.
 * These tests validate getters/setters, constructors, and basic data integrity.
 */
class DTOComprehensiveTest {

    @Test
    void testCategoryRequest_GettersSetters() {
        CategoryRequest dto = new CategoryRequest();
        dto.setName("Electronics");
        
        assertEquals("Electronics", dto.getName());
    }

    @Test
    void testCategoryResponse_GettersSetters() {
        CategoryResponse dto = new CategoryResponse();
        UUID id = UUID.randomUUID();
        dto.setId(id);
        dto.setName("Books");
        
        assertEquals(id, dto.getId());
        assertEquals("Books", dto.getName());
    }

    @Test
    void testLoginRequest_NoArgConstructor() {
        LoginRequest dto = new LoginRequest();
        assertNotNull(dto);
    }

    @Test
    void testLoginRequest_AllArgsConstructor() {
        LoginRequest dto = new LoginRequest("user@example.com", "password123");
        
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("password123", dto.getPassword());
    }

    @Test
    void testLoginRequest_GettersSetters() {
        LoginRequest dto = new LoginRequest();
        dto.setEmail("test@example.com");
        dto.setPassword("securepass");
        
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("securepass", dto.getPassword());
    }

    @Test
    void testAuthResponse_NoArgConstructor() {
        AuthResponse dto = new AuthResponse();
        assertNotNull(dto);
    }

    @Test
    void testAuthResponse_AllArgsConstructor() {
        AuthResponse dto = new AuthResponse("jwt-token", "johndoe", "john@example.com", "BUYER", "Login successful");
        
        assertEquals("jwt-token", dto.getToken());
        assertEquals("johndoe", dto.getUsername());
        assertEquals("john@example.com", dto.getEmail());
        assertEquals("BUYER", dto.getRole());
        assertEquals("Login successful", dto.getMessage());
    }

    @Test
    void testAuthResponse_GettersSetters() {
        AuthResponse dto = new AuthResponse();
        dto.setToken("token123");
        dto.setUsername("user");
        dto.setEmail("user@test.com");
        dto.setRole("SELLER");
        dto.setMessage("Success");
        
        assertEquals("token123", dto.getToken());
        assertEquals("user", dto.getUsername());
        assertEquals("user@test.com", dto.getEmail());
        assertEquals("SELLER", dto.getRole());
        assertEquals("Success", dto.getMessage());
    }

    @Test
    void testSellerDashboardDTO_SalesMetrics() {
        SellerDashboardDTO dto = new SellerDashboardDTO();
        dto.setTotalOrders(100L);
        dto.setPendingOrders(10L);
        dto.setShippedOrders(70L);
        dto.setDeliveredOrders(20L);
        
        assertEquals(100L, dto.getTotalOrders());
        assertEquals(10L, dto.getPendingOrders());
        assertEquals(70L, dto.getShippedOrders());
        assertEquals(20L, dto.getDeliveredOrders());
    }

    @Test
    void testSellerDashboardDTO_RevenueMetrics() {
        SellerDashboardDTO dto = new SellerDashboardDTO();
        dto.setTotalRevenue(new BigDecimal("50000.00"));
        dto.setMonthlyRevenue(new BigDecimal("5000.00"));
        dto.setWeeklyRevenue(new BigDecimal("1250.00"));
        
        assertEquals(new BigDecimal("50000.00"), dto.getTotalRevenue());
        assertEquals(new BigDecimal("5000.00"), dto.getMonthlyRevenue());
        assertEquals(new BigDecimal("1250.00"), dto.getWeeklyRevenue());
    }

    @Test
    void testSellerDashboardDTO_ProductMetrics() {
        SellerDashboardDTO dto = new SellerDashboardDTO();
        dto.setTotalProducts(50L);
        dto.setActiveProducts(45L);
        dto.setOutOfStockProducts(5L);
        
        assertEquals(50L, dto.getTotalProducts());
        assertEquals(45L, dto.getActiveProducts());
        assertEquals(5L, dto.getOutOfStockProducts());
    }

    @Test
    void testSellerDashboardDTO_StripeMetrics() {
        SellerDashboardDTO dto = new SellerDashboardDTO();
        dto.setStripeConnected(true);
        dto.setStripeAccountId("acct_123456");
        
        assertTrue(dto.getStripeConnected());
        assertEquals("acct_123456", dto.getStripeAccountId());
    }

    @Test
    void testSellerDashboardDTO_StoreInfo() {
        SellerDashboardDTO dto = new SellerDashboardDTO();
        UUID storeId = UUID.randomUUID();
        dto.setStoreId(storeId);
        dto.setStoreName("My Awesome Store");
        
        assertEquals(storeId, dto.getStoreId());
        assertEquals("My Awesome Store", dto.getStoreName());
    }

    @Test
    void testSellerDashboardDTO_PerformanceMetrics() {
        SellerDashboardDTO dto = new SellerDashboardDTO();
        dto.setAverageOrderValue(new BigDecimal("125.50"));
        dto.setFulfillmentRate(95.5);
        
        assertEquals(new BigDecimal("125.50"), dto.getAverageOrderValue());
        assertEquals(95.5, dto.getFulfillmentRate());
    }

    @Test
    void testProductReviewDTO_NoArgConstructor() {
        ProductReviewDTO dto = new ProductReviewDTO();
        assertNotNull(dto);
    }

    @Test
    void testProductReviewDTO_AllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID reviewerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime responseDate = now.plusDays(1);
        
        ProductReviewDTO dto = new ProductReviewDTO(
            id, productId, reviewerId, "John Doe",
            5, "Excellent!", "Great product, highly recommend",
            true, 10, now, "Thank you!", responseDate
        );
        
        assertEquals(id, dto.getId());
        assertEquals(productId, dto.getProductId());
        assertEquals(reviewerId, dto.getReviewerId());
        assertEquals("John Doe", dto.getReviewerName());
        assertEquals(5, dto.getRating());
        assertEquals("Excellent!", dto.getTitle());
        assertEquals("Great product, highly recommend", dto.getComment());
        assertTrue(dto.getVerifiedPurchase());
        assertEquals(10, dto.getHelpfulCount());
        assertEquals(now, dto.getCreatedAt());
        assertEquals("Thank you!", dto.getSellerResponse());
        assertEquals(responseDate, dto.getSellerResponseDate());
    }

    @Test
    void testProductReviewDTO_GettersSetters() {
        ProductReviewDTO dto = new ProductReviewDTO();
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID reviewerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        dto.setId(id);
        dto.setProductId(productId);
        dto.setReviewerId(reviewerId);
        dto.setReviewerName("Jane Smith");
        dto.setRating(4);
        dto.setTitle("Good product");
        dto.setComment("Works as expected");
        dto.setVerifiedPurchase(true);
        dto.setHelpfulCount(5);
        dto.setCreatedAt(now);
        dto.setSellerResponse("Thanks for feedback");
        dto.setSellerResponseDate(now);
        
        assertEquals(id, dto.getId());
        assertEquals(productId, dto.getProductId());
        assertEquals(reviewerId, dto.getReviewerId());
        assertEquals("Jane Smith", dto.getReviewerName());
        assertEquals(4, dto.getRating());
        assertEquals("Good product", dto.getTitle());
        assertEquals("Works as expected", dto.getComment());
        assertTrue(dto.getVerifiedPurchase());
        assertEquals(5, dto.getHelpfulCount());
        assertEquals(now, dto.getCreatedAt());
        assertEquals("Thanks for feedback", dto.getSellerResponse());
        assertEquals(now, dto.getSellerResponseDate());
    }

    @Test
    void testProductSearchDTO_NoArgConstructor() {
        ProductSearchDTO dto = new ProductSearchDTO();
        assertNotNull(dto);
    }

    @Test
    void testProductSearchDTO_AllArgsConstructor() {
        UUID id = UUID.randomUUID();
        ProductSearchDTO dto = new ProductSearchDTO(
            id, "Laptop", "Gaming laptop with RTX 4090",
            new BigDecimal("1999.99"), "Electronics",
            4.5, 120L, 10, "Tech Store"
        );
        
        assertEquals(id, dto.getId());
        assertEquals("Laptop", dto.getTitle());
        assertEquals("Gaming laptop with RTX 4090", dto.getDescription());
        assertEquals(new BigDecimal("1999.99"), dto.getPrice());
        assertEquals("Electronics", dto.getCategory());
        assertEquals(4.5, dto.getAverageRating());
        assertEquals(120L, dto.getReviewCount());
        assertEquals(10, dto.getStock());
        assertEquals("Tech Store", dto.getStoreName());
    }

    @Test
    void testProductSearchDTO_GettersSetters() {
        ProductSearchDTO dto = new ProductSearchDTO();
        UUID id = UUID.randomUUID();
        
        dto.setId(id);
        dto.setTitle("Smartphone");
        dto.setDescription("Latest flagship phone");
        dto.setPrice(new BigDecimal("899.99"));
        dto.setCategory("Mobile");
        dto.setAverageRating(4.8);
        dto.setReviewCount(500L);
        dto.setStock(25);
        dto.setStoreName("Mobile World");
        
        assertEquals(id, dto.getId());
        assertEquals("Smartphone", dto.getTitle());
        assertEquals("Latest flagship phone", dto.getDescription());
        assertEquals(new BigDecimal("899.99"), dto.getPrice());
        assertEquals("Mobile", dto.getCategory());
        assertEquals(4.8, dto.getAverageRating());
        assertEquals(500L, dto.getReviewCount());
        assertEquals(25, dto.getStock());
        assertEquals("Mobile World", dto.getStoreName());
    }

    @Test
    void testProductSearchDTO_NullableFields() {
        ProductSearchDTO dto = new ProductSearchDTO();
        dto.setId(UUID.randomUUID());
        dto.setTitle("Product");
        dto.setPrice(new BigDecimal("50.00"));
        
        // These can be null
        dto.setDescription(null);
        dto.setCategory(null);
        dto.setAverageRating(null);
        dto.setReviewCount(null);
        dto.setStoreName(null);
        
        assertNull(dto.getDescription());
        assertNull(dto.getCategory());
        assertNull(dto.getAverageRating());
        assertNull(dto.getReviewCount());
        assertNull(dto.getStoreName());
    }

    @Test
    void testProductReviewDTO_UnverifiedPurchase() {
        ProductReviewDTO dto = new ProductReviewDTO();
        dto.setVerifiedPurchase(false);
        dto.setHelpfulCount(0);
        
        assertFalse(dto.getVerifiedPurchase());
        assertEquals(0, dto.getHelpfulCount());
    }

    @Test
    void testSellerDashboardDTO_ZeroMetrics() {
        SellerDashboardDTO dto = new SellerDashboardDTO();
        dto.setTotalOrders(0L);
        dto.setTotalRevenue(BigDecimal.ZERO);
        dto.setStripeConnected(false);
        
        assertEquals(0L, dto.getTotalOrders());
        assertEquals(BigDecimal.ZERO, dto.getTotalRevenue());
        assertFalse(dto.getStripeConnected());
    }

    @Test
    void testProductSearchDTO_OutOfStock() {
        ProductSearchDTO dto = new ProductSearchDTO();
        dto.setStock(0);
        dto.setTitle("Out of Stock Item");
        
        assertEquals(0, dto.getStock());
        assertEquals("Out of Stock Item", dto.getTitle());
    }

    @Test
    void testProductReviewDTO_NoSellerResponse() {
        ProductReviewDTO dto = new ProductReviewDTO();
        dto.setSellerResponse(null);
        dto.setSellerResponseDate(null);
        
        assertNull(dto.getSellerResponse());
        assertNull(dto.getSellerResponseDate());
    }
}

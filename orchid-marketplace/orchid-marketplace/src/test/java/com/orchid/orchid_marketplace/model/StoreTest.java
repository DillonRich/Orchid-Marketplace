package com.orchid.orchid_marketplace.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoreTest {
    
    private Store store;
    private UUID storeId;
    
    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        store = new Store();
        store.setId(storeId);
    }
    
    @Test
    void testStoreIdGetterSetter() {
        assertEquals(storeId, store.getId());
    }
    
    @Test
    void testStoreNameGetterSetter() {
        store.setStoreName("My Test Store");
        assertEquals("My Test Store", store.getStoreName());
    }
    
    @Test
    void testSlugGetterSetter() {
        store.setSlug("my-test-store");
        assertEquals("my-test-store", store.getSlug());
    }
    
    @Test
    void testProfileImageUrlGetterSetter() {
        store.setProfileImageUrl("https://example.com/profile.jpg");
        assertEquals("https://example.com/profile.jpg", store.getProfileImageUrl());
    }
    
    @Test
    void testBannerImageUrlGetterSetter() {
        store.setBannerImageUrl("https://example.com/banner.jpg");
        assertEquals("https://example.com/banner.jpg", store.getBannerImageUrl());
    }
    
    @Test
    void testAboutTextGetterSetter() {
        store.setAboutText("About our store");
        assertEquals("About our store", store.getAboutText());
    }
    
    @Test
    void testReturnPolicyTextGetterSetter() {
        store.setReturnPolicyText("30-day return policy");
        assertEquals("30-day return policy", store.getReturnPolicyText());
    }
    
    @Test
    void testTotalSalesGetterSetter() {
        store.setTotalSales(100);
        assertEquals(100, store.getTotalSales());
    }
    
    @Test
    void testAverageRatingGetterSetter() {
        BigDecimal rating = new BigDecimal("4.5");
        store.setAverageRating(rating);
        assertEquals(rating, store.getAverageRating());
    }
    
    @Test
    void testIsPublicGetterSetter() {
        store.setIsPublic(true);
        assertTrue(store.getIsPublic());
    }
    
    @Test
    void testSearchVectorGetterSetter() {
        store.setSearchVector("vector-data");
        assertEquals("vector-data", store.getSearchVector());
    }
    
    @Test
    void testCacheKeyGetterSetter() {
        store.setCacheKey("cache-123");
        assertEquals("cache-123", store.getCacheKey());
    }
}

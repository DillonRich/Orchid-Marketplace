package com.orchid.orchid_marketplace.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {
    
    private Product product;
    private UUID productId;
    
    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = new Product();
        product.setId(productId);
    }
    
    @Test
    void testProductIdGetterSetter() {
        assertEquals(productId, product.getId());
    }
    
    @Test
    void testTitleGetterSetter() {
        product.setTitle("Test Product");
        assertEquals("Test Product", product.getTitle());
    }
    
    @Test
    void testDescriptionGetterSetter() {
        product.setDescription("Test Description");
        assertEquals("Test Description", product.getDescription());
    }
    
    @Test
    void testPriceGetterSetter() {
        BigDecimal price = new BigDecimal("99.99");
        product.setPrice(price);
        assertEquals(price, product.getPrice());
    }
    
    @Test
    void testStockQuantityGetterSetter() {
        product.setStockQuantity(100);
        assertEquals(100, product.getStockQuantity());
    }
    
    @Test
    void testSoldCountGetterSetter() {
        product.setSoldCount(25);
        assertEquals(25, product.getSoldCount());
    }
    
    @Test
    void testConditionGetterSetter() {
        product.setCondition(Product.ProductCondition.NEW);
        assertEquals(Product.ProductCondition.NEW, product.getCondition());
    }
    
    @Test
    void testWeightLbsGetterSetter() {
        BigDecimal weight = new BigDecimal("5.5");
        product.setWeightLbs(weight);
        assertEquals(weight, product.getWeightLbs());
    }
    
    @Test
    void testPackageDimensionsGetterSetter() {
        product.setPackageLengthIn(new BigDecimal("12"));
        product.setPackageWidthIn(new BigDecimal("8"));
        product.setPackageHeightIn(new BigDecimal("4"));
        
        assertEquals(new BigDecimal("12"), product.getPackageLengthIn());
        assertEquals(new BigDecimal("8"), product.getPackageWidthIn());
        assertEquals(new BigDecimal("4"), product.getPackageHeightIn());
    }
    
    @Test
    void testRequiresSpecialHandlingGetterSetter() {
        product.setRequiresSpecialHandling(true);
        assertTrue(product.getRequiresSpecialHandling());
    }
    
    @Test
    void testSearchIndexIdGetterSetter() {
        product.setSearchIndexId("idx-123");
        assertEquals("idx-123", product.getSearchIndexId());
    }
    
    @Test
    void testAllProductConditions() {
        product.setCondition(Product.ProductCondition.LIKE_NEW);
        assertEquals(Product.ProductCondition.LIKE_NEW, product.getCondition());
        
        product.setCondition(Product.ProductCondition.GOOD);
        assertEquals(Product.ProductCondition.GOOD, product.getCondition());
        
        product.setCondition(Product.ProductCondition.FAIR);
        assertEquals(Product.ProductCondition.FAIR, product.getCondition());
    }
    
    @Test
    void testPriceWithDecimalPrecision() {
        BigDecimal price = new BigDecimal("1234.56");
        product.setPrice(price);
        assertEquals(0, price.compareTo(product.getPrice()));
    }
}

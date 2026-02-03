package com.orchid.orchid_marketplace.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {
    
    private Category category;
    private UUID categoryId;
    
    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        category = new Category();
        category.setId(categoryId);
    }
    
    @Test
    void testCategoryIdGetterSetter() {
        assertEquals(categoryId, category.getId());
    }
    
    @Test
    void testNameGetterSetter() {
        category.setName("Electronics");
        assertEquals("Electronics", category.getName());
    }
    
    @Test
    void testDisplayNameGetterSetter() {
        category.setDisplayName("Consumer Electronics");
        assertEquals("Consumer Electronics", category.getDisplayName());
    }
    
    @Test
    void testDescriptionGetterSetter() {
        category.setDescription("Electronic devices and accessories");
        assertEquals("Electronic devices and accessories", category.getDescription());
    }
    
    @Test
    void testIconClassGetterSetter() {
        category.setIconClass("icon-electronics");
        assertEquals("icon-electronics", category.getIconClass());
    }
    
    @Test
    void testDisplayOrderGetterSetter() {
        category.setDisplayOrder(5);
        assertEquals(5, category.getDisplayOrder());
    }
    
    @Test
    void testSearchFacetGetterSetter() {
        category.setSearchFacet("facet-electronics");
        assertEquals("facet-electronics", category.getSearchFacet());
    }
    
    @Test
    void testCacheKeyPrefixGetterSetter() {
        category.setCacheKeyPrefix("cat:");
        assertEquals("cat:", category.getCacheKeyPrefix());
    }
    
    @Test
    void testCategoryConstructorWithParameters() {
        Category cat = new Category("Books", "Books & Literature", "All types of books");
        assertEquals("Books", cat.getName());
        assertEquals("Books & Literature", cat.getDisplayName());
        assertEquals("All types of books", cat.getDescription());
    }
}

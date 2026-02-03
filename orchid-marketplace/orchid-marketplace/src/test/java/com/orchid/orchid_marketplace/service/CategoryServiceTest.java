package com.orchid.orchid_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orchid.orchid_marketplace.model.Category;
import com.orchid.orchid_marketplace.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @InjectMocks
    private CategoryService categoryService;
    
    private Category category;
    private UUID categoryId;
    
    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        
        category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");
        category.setDisplayName("Consumer Electronics");
        category.setDescription("Electronic devices and accessories");
        category.setIsActive(true);
    }
    
    @Test
    void testGetAllCategories() {
        List<Category> categories = List.of(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        
        List<Category> result = categoryService.getAllCategories();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }
    
    @Test
    void testGetCategoryById() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        
        assertTrue(result.isPresent());
        assertEquals(categoryId, result.get().getId());
        verify(categoryRepository, times(1)).findById(categoryId);
    }
    
    @Test
    void testGetCategoryByName() {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        
        Optional<Category> result = categoryService.getCategoryByName("Electronics");
        
        assertTrue(result.isPresent());
        assertEquals("Electronics", result.get().getName());
        verify(categoryRepository, times(1)).findByName("Electronics");
    }
    
    @Test
    void testGetRootCategories() {
        List<Category> categories = List.of(category);
        when(categoryRepository.findActiveRootCategories()).thenReturn(categories);
        
        List<Category> result = categoryService.getRootCategories();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findActiveRootCategories();
    }
    
    @Test
    void testCreateCategoryNullThrowsException() {
        assertThrows(NullPointerException.class, () -> categoryService.createCategory(null));
    }
    
    @Test
    void testGetCategoryByIdNullThrowsException() {
        assertThrows(NullPointerException.class, () -> categoryService.getCategoryById(null));
    }
    
    @Test
    void testCreateCategoryWithDuplicateNameThrowsException() {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        
        assertThrows(RuntimeException.class, () -> categoryService.createCategory(category));
    }
}

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

import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private SellerLedgerService sellerLedgerService;
    
    @InjectMocks
    private ProductService productService;
    
    private Product product;
    private UUID productId;
    private UUID storeId;
    private UUID categoryId;
    
    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        
        product = new Product();
        product.setId(productId);
        product.setTitle("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));
        product.setStockQuantity(10);
        product.setIsActive(true);
    }
    
    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);
        
        List<Product> result = productService.getAllProducts();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }
    
    @Test
    void testGetProductById() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        Optional<Product> result = productService.getProductById(productId);
        
        assertTrue(result.isPresent());
        assertEquals(productId, result.get().getId());
        verify(productRepository, times(1)).findById(productId);
    }
    
    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        Optional<Product> result = productService.getProductById(productId);
        
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(productId);
    }
    
    @Test
    void testGetProductsByStoreId() {
        List<Product> products = List.of(product);
        when(productRepository.findByStoreIdAndIsActiveTrue(storeId)).thenReturn(products);
        
        List<Product> result = productService.getProductsByStoreId(storeId);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByStoreIdAndIsActiveTrue(storeId);
    }
    
    @Test
    void testGetProductsByCategoryId() {
        List<Product> products = List.of(product);
        when(productRepository.findByCategoryId(categoryId)).thenReturn(products);
        
        List<Product> result = productService.getProductsByCategoryId(categoryId);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByCategoryId(categoryId);
    }
    
    @Test
    void testSearchProducts() {
        String keyword = "test";
        List<Product> products = List.of(product);
        when(productRepository.searchByKeyword(keyword)).thenReturn(products);
        
        List<Product> result = productService.searchProducts(keyword);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).searchByKeyword(keyword);
    }
    
    @Test
    void testGetProductsByPriceRange() {
        BigDecimal minPrice = new BigDecimal("50");
        BigDecimal maxPrice = new BigDecimal("150");
        List<Product> products = List.of(product);
        when(productRepository.findByPriceRange(minPrice, maxPrice)).thenReturn(products);
        
        List<Product> result = productService.getProductsByPriceRange(minPrice, maxPrice);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByPriceRange(minPrice, maxPrice);
    }
    
    @Test
    void testGetProductsInStock() {
        List<Product> products = List.of(product);
        when(productRepository.findInStockProducts()).thenReturn(products);
        
        List<Product> result = productService.getProductsInStock();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findInStockProducts();
    }
    
    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        
        Product result = productService.createProduct(product);
        
        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(sellerLedgerService, times(1)).accrueListingFeeForProduct(product);
    }
    
    @Test
    void testCreateProductNullThrowsException() {
        assertThrows(NullPointerException.class, () -> productService.createProduct(null));
    }
    
    @Test
    void testGetProductByIdNullThrowsException() {
        assertThrows(NullPointerException.class, () -> productService.getProductById(null));
    }
}

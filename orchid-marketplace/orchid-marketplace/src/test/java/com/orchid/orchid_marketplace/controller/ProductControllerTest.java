package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.orchid.orchid_marketplace.dto.ProductRequest;
import com.orchid.orchid_marketplace.dto.ProductResponse;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.Category;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.service.ProductService;

class ProductControllerTest {

    private ProductController controller;

    @Mock
    private ProductService productService;

    private UUID productId;
    private UUID storeId;
    private UUID categoryId;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProductController();
        ReflectionTestUtils.setField(controller, "productService", productService);

        productId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        categoryId = UUID.randomUUID();

        testProduct = new Product();
        testProduct.setId(productId);
        testProduct.setPrice(BigDecimal.valueOf(29.99));
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        when(productService.getAllProducts()).thenReturn(List.of(testProduct));

        List<ProductResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(productService.getAllProducts()).thenReturn(List.of());

        List<ProductResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_MultipleProducts() {
        Product product2 = new Product();
        product2.setId(UUID.randomUUID());

        when(productService.getAllProducts()).thenReturn(List.of(testProduct, product2));

        List<ProductResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testListAll_VerifyServiceCalled() {
        when(productService.getAllProducts()).thenReturn(List.of());

        controller.listAll();

        verify(productService).getAllProducts();
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(productService.getProductById(productId)).thenReturn(Optional.of(testProduct));

        ResponseEntity<ProductResponse> result = controller.getById(productId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(productService.getProductById(productId)).thenReturn(Optional.empty());

        ResponseEntity<ProductResponse> result = controller.getById(productId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(productService.getProductById(productId)).thenReturn(Optional.of(testProduct));

        controller.getById(productId);

        verify(productService).getProductById(productId);
    }

    // ========== search Tests ==========

    @Test
    void testSearch_Success() {
        when(productService.searchProducts("test")).thenReturn(List.of(testProduct));

        List<ProductResponse> result = controller.search("test");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSearch_NoResults() {
        when(productService.searchProducts("nonexistent")).thenReturn(List.of());

        List<ProductResponse> result = controller.search("nonexistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearch_EmptyQuery() {
        when(productService.searchProducts("")).thenReturn(List.of());

        List<ProductResponse> result = controller.search("");

        assertNotNull(result);
    }

    @Test
    void testSearch_VerifyServiceCalled() {
        when(productService.searchProducts("keyword")).thenReturn(List.of(testProduct));

        controller.search("keyword");

        verify(productService).searchProducts("keyword");
    }

    // ========== byStore Tests ==========

    @Test
    void testByStore_Success() {
        when(productService.getProductsByStoreId(storeId)).thenReturn(List.of(testProduct));

        List<ProductResponse> result = controller.byStore(storeId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testByStore_Empty() {
        when(productService.getProductsByStoreId(storeId)).thenReturn(List.of());

        List<ProductResponse> result = controller.byStore(storeId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testByStore_VerifyServiceCalled() {
        when(productService.getProductsByStoreId(storeId)).thenReturn(List.of(testProduct));

        controller.byStore(storeId);

        verify(productService).getProductsByStoreId(storeId);
    }

    // ========== byCategory Tests ==========

    @Test
    void testByCategory_Success() {
        when(productService.getProductsByCategoryId(categoryId)).thenReturn(List.of(testProduct));

        List<ProductResponse> result = controller.byCategory(categoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testByCategory_Empty() {
        when(productService.getProductsByCategoryId(categoryId)).thenReturn(List.of());

        List<ProductResponse> result = controller.byCategory(categoryId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testByCategory_VerifyServiceCalled() {
        when(productService.getProductsByCategoryId(categoryId)).thenReturn(List.of(testProduct));

        controller.byCategory(categoryId);

        verify(productService).getProductsByCategoryId(categoryId);
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        ProductRequest request = new ProductRequest();

        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        ProductResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        ProductRequest request = new ProductRequest();

        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        controller.create(request);

        verify(productService).createProduct(any(Product.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        ProductRequest request = new ProductRequest();

        when(productService.updateProduct(eq(productId), any(Product.class))).thenReturn(testProduct);

        ProductResponse result = controller.update(productId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        ProductRequest request = new ProductRequest();

        when(productService.updateProduct(eq(productId), any(Product.class))).thenReturn(testProduct);

        controller.update(productId, request);

        verify(productService).updateProduct(eq(productId), any(Product.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(productService).deleteProduct(productId);

        ResponseEntity<Void> result = controller.delete(productId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(productService).deleteProduct(productId);

        controller.delete(productId);

        verify(productService).deleteProduct(productId);
    }
}

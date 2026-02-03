package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import com.orchid.orchid_marketplace.dto.ProductImageRequest;
import com.orchid.orchid_marketplace.dto.ProductImageResponse;
import com.orchid.orchid_marketplace.model.ProductImage;
import com.orchid.orchid_marketplace.service.ProductImageService;

class ProductImageControllerTest {

    private ProductImageController controller;

    @Mock
    private ProductImageService productImageService;

    private UUID imageId;
    private UUID productId;
    private ProductImage testImage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProductImageController();
        ReflectionTestUtils.setField(controller, "productImageService", productImageService);

        imageId = UUID.randomUUID();
        productId = UUID.randomUUID();
        testImage = new ProductImage();
        testImage.setId(imageId);
        testImage.setImageUrl("https://example.com/image.jpg");
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        when(productImageService.getAllProductImages()).thenReturn(List.of(testImage));

        List<ProductImageResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(productImageService.getAllProductImages()).thenReturn(List.of());

        List<ProductImageResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_MultipleImages() {
        ProductImage image2 = new ProductImage();
        image2.setId(UUID.randomUUID());
        image2.setImageUrl("https://example.com/image2.jpg");

        when(productImageService.getAllProductImages()).thenReturn(List.of(testImage, image2));

        List<ProductImageResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(productImageService.getProductImageById(imageId)).thenReturn(Optional.of(testImage));

        ResponseEntity<ProductImageResponse> result = controller.getById(imageId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(productImageService.getProductImageById(imageId)).thenReturn(Optional.empty());

        ResponseEntity<ProductImageResponse> result = controller.getById(imageId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(productImageService.getProductImageById(imageId)).thenReturn(Optional.of(testImage));

        controller.getById(imageId);

        verify(productImageService).getProductImageById(imageId);
    }

    // ========== byProduct Tests ==========

    @Test
    void testByProduct_Success() {
        when(productImageService.getProductImagesByProductId(productId)).thenReturn(List.of(testImage));

        List<ProductImageResponse> result = controller.byProduct(productId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testByProduct_Empty() {
        when(productImageService.getProductImagesByProductId(productId)).thenReturn(List.of());

        List<ProductImageResponse> result = controller.byProduct(productId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testByProduct_MultipleImages() {
        ProductImage image2 = new ProductImage();
        image2.setId(UUID.randomUUID());

        when(productImageService.getProductImagesByProductId(productId)).thenReturn(List.of(testImage, image2));

        List<ProductImageResponse> result = controller.byProduct(productId);

        assertEquals(2, result.size());
    }

    @Test
    void testByProduct_VerifyServiceCalled() {
        when(productImageService.getProductImagesByProductId(productId)).thenReturn(List.of(testImage));

        controller.byProduct(productId);

        verify(productImageService).getProductImagesByProductId(productId);
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        ProductImageRequest request = new ProductImageRequest();

        when(productImageService.createProductImage(any(ProductImage.class))).thenReturn(testImage);

        ProductImageResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        ProductImageRequest request = new ProductImageRequest();

        when(productImageService.createProductImage(any(ProductImage.class))).thenReturn(testImage);

        controller.create(request);

        verify(productImageService).createProductImage(any(ProductImage.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        ProductImageRequest request = new ProductImageRequest();

        when(productImageService.updateProductImage(eq(imageId), any(ProductImage.class))).thenReturn(testImage);

        ProductImageResponse result = controller.update(imageId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        ProductImageRequest request = new ProductImageRequest();

        when(productImageService.updateProductImage(eq(imageId), any(ProductImage.class))).thenReturn(testImage);

        controller.update(imageId, request);

        verify(productImageService).updateProductImage(eq(imageId), any(ProductImage.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(productImageService).deleteProductImage(imageId);

        ResponseEntity<Void> result = controller.delete(imageId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(productImageService).deleteProductImage(imageId);

        controller.delete(imageId);

        verify(productImageService).deleteProductImage(imageId);
    }
}

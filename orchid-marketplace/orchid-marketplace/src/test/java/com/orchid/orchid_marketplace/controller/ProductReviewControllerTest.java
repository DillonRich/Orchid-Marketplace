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

import com.orchid.orchid_marketplace.dto.ProductReviewRequest;
import com.orchid.orchid_marketplace.dto.ProductReviewResponse;
import com.orchid.orchid_marketplace.dto.SellerResponseRequest;
import com.orchid.orchid_marketplace.model.ProductReview;
import com.orchid.orchid_marketplace.service.ProductReviewService;

class ProductReviewControllerTest {

    private ProductReviewController controller;

    @Mock
    private ProductReviewService reviewService;

    private UUID reviewId;
    private UUID productId;
    private ProductReview testReview;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProductReviewController();
        ReflectionTestUtils.setField(controller, "reviewService", reviewService);

        reviewId = UUID.randomUUID();
        productId = UUID.randomUUID();
        testReview = new ProductReview();
        testReview.setId(reviewId);
        testReview.setRating(5);
        testReview.setComment("Great product!");
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        when(reviewService.getAllProductReviews()).thenReturn(List.of(testReview));

        List<ProductReviewResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(reviewService.getAllProductReviews()).thenReturn(List.of());

        List<ProductReviewResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_MultipleReviews() {
        ProductReview review2 = new ProductReview();
        review2.setId(UUID.randomUUID());
        review2.setRating(4);

        when(reviewService.getAllProductReviews()).thenReturn(List.of(testReview, review2));

        List<ProductReviewResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(reviewService.getProductReviewById(reviewId)).thenReturn(Optional.of(testReview));

        ResponseEntity<ProductReviewResponse> result = controller.getById(reviewId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(reviewService.getProductReviewById(reviewId)).thenReturn(Optional.empty());

        ResponseEntity<ProductReviewResponse> result = controller.getById(reviewId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(reviewService.getProductReviewById(reviewId)).thenReturn(Optional.of(testReview));

        controller.getById(reviewId);

        verify(reviewService).getProductReviewById(reviewId);
    }

    // ========== byProduct Tests ==========

    @Test
    void testByProduct_Success() {
        when(reviewService.getReviewsByProductId(productId)).thenReturn(List.of(testReview));

        List<ProductReviewResponse> result = controller.byProduct(productId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testByProduct_Empty() {
        when(reviewService.getReviewsByProductId(productId)).thenReturn(List.of());

        List<ProductReviewResponse> result = controller.byProduct(productId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testByProduct_MultipleReviews() {
        ProductReview review2 = new ProductReview();
        review2.setId(UUID.randomUUID());

        when(reviewService.getReviewsByProductId(productId)).thenReturn(List.of(testReview, review2));

        List<ProductReviewResponse> result = controller.byProduct(productId);

        assertEquals(2, result.size());
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        ProductReviewRequest request = new ProductReviewRequest();

        when(reviewService.createProductReview(any(ProductReview.class))).thenReturn(testReview);

        ProductReviewResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        ProductReviewRequest request = new ProductReviewRequest();

        when(reviewService.createProductReview(any(ProductReview.class))).thenReturn(testReview);

        controller.create(request);

        verify(reviewService).createProductReview(any(ProductReview.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        ProductReviewRequest request = new ProductReviewRequest();

        when(reviewService.updateProductReview(eq(reviewId), any(ProductReview.class))).thenReturn(testReview);

        ProductReviewResponse result = controller.update(reviewId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        ProductReviewRequest request = new ProductReviewRequest();

        when(reviewService.updateProductReview(eq(reviewId), any(ProductReview.class))).thenReturn(testReview);

        controller.update(reviewId, request);

        verify(reviewService).updateProductReview(eq(reviewId), any(ProductReview.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(reviewService).deleteProductReview(reviewId);

        ResponseEntity<Void> result = controller.delete(reviewId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(reviewService).deleteProductReview(reviewId);

        controller.delete(reviewId);

        verify(reviewService).deleteProductReview(reviewId);
    }

    // ========== addSellerResponse Tests ==========

    @Test
    void testAddSellerResponse_Success() {
        SellerResponseRequest request = new SellerResponseRequest();
        request.setResponse("Thank you for your review!");

        when(reviewService.addSellerResponse(reviewId, "Thank you for your review!")).thenReturn(testReview);

        ProductReviewResponse result = controller.addSellerResponse(reviewId, request);

        assertNotNull(result);
    }

    @Test
    void testAddSellerResponse_VerifyServiceCalled() {
        SellerResponseRequest request = new SellerResponseRequest();
        request.setResponse("Thank you!");

        when(reviewService.addSellerResponse(reviewId, "Thank you!")).thenReturn(testReview);

        controller.addSellerResponse(reviewId, request);

        verify(reviewService).addSellerResponse(reviewId, "Thank you!");
    }
}

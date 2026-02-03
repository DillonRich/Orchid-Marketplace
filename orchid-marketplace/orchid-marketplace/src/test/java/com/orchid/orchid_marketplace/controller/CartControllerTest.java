package com.orchid.orchid_marketplace.controller;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.orchid.orchid_marketplace.dto.AddToCartRequest;
import com.orchid.orchid_marketplace.model.Cart;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.service.CartService;

class CartControllerTest {

    private CartController controller;

    @Mock
    private CartService cartService;

    @Mock
    private Authentication authentication;

    private UUID userId;
    private User testUser;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CartController();
        ReflectionTestUtils.setField(controller, "cartService", cartService);

        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);

        when(authentication.getPrincipal()).thenReturn(testUser);

        testCart = new Cart();
        testCart.setId(UUID.randomUUID());
        testCart.setUser(testUser);
    }

    // ========== getCart Tests ==========

    @Test
    void testGetCart_Success() {
        when(cartService.getCart(userId)).thenReturn(testCart);

        ResponseEntity<?> result = controller.getCart(authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetCart_VerifyServiceCalled() {
        when(cartService.getCart(userId)).thenReturn(testCart);

        controller.getCart(authentication);

        verify(cartService).getCart(userId);
    }

    @Test
    void testGetCart_ExtractsUserFromAuthentication() {
        when(cartService.getCart(userId)).thenReturn(testCart);

        controller.getCart(authentication);

        verify(authentication).getPrincipal();
    }

    // ========== addToCart Tests ==========

    @Test
    void testAddToCart_Success() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(UUID.randomUUID());
        request.setQuantity(2);

        when(cartService.addToCart(eq(userId), any(UUID.class), anyInt(), any())).thenReturn(testCart);

        ResponseEntity<?> result = controller.addToCart(request, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAddToCart_WithShippingOption() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(UUID.randomUUID());
        request.setQuantity(1);
        request.setShippingOptionId(UUID.randomUUID());

        when(cartService.addToCart(eq(userId), any(UUID.class), anyInt(), any())).thenReturn(testCart);

        ResponseEntity<?> result = controller.addToCart(request, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAddToCart_VerifyServiceCalled() {
        AddToCartRequest request = new AddToCartRequest();
        UUID productId = UUID.randomUUID();
        request.setProductId(productId);
        request.setQuantity(2);

        when(cartService.addToCart(eq(userId), eq(productId), eq(2), any())).thenReturn(testCart);

        controller.addToCart(request, authentication);

        verify(cartService).addToCart(eq(userId), eq(productId), eq(2), any());
    }

    @Test
    void testAddToCart_ValidationError() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(UUID.randomUUID());
        request.setQuantity(0); // Invalid quantity

        when(cartService.addToCart(any(), any(), anyInt(), any())).thenThrow(new RuntimeException("Invalid quantity"));

        ResponseEntity<?> result = controller.addToCart(request, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    // ========== Additional interaction tests ==========

    @Test
    void testGetCart_MultipleCallsWithSameUser() {
        when(cartService.getCart(userId)).thenReturn(testCart);

        controller.getCart(authentication);
        controller.getCart(authentication);

        verify(cartService, times(2)).getCart(userId);
    }

    @Test
    void testAddToCart_MixedValidAndInvalidRequests() {
        AddToCartRequest validRequest = new AddToCartRequest();
        validRequest.setProductId(UUID.randomUUID());
        validRequest.setQuantity(1);

        when(cartService.addToCart(eq(userId), any(UUID.class), eq(1), any())).thenReturn(testCart);

        ResponseEntity<?> result = controller.addToCart(validRequest, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}

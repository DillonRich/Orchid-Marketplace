package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.orchid.orchid_marketplace.dto.StripeCheckoutSessionRequest;
import com.orchid.orchid_marketplace.dto.StripeCheckoutSessionResponse;
import com.orchid.orchid_marketplace.service.stripe.StripeCheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.exception.ApiConnectionException;

class StripeCheckoutControllerTest {

    private StripeCheckoutController controller;

    @Mock
    private StripeCheckoutService stripeCheckoutService;

    private UUID orderId;
    private String successUrl;
    private String cancelUrl;
    private StripeCheckoutSessionResponse mockResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new StripeCheckoutController();
        ReflectionTestUtils.setField(controller, "stripeCheckoutService", stripeCheckoutService);

        orderId = UUID.randomUUID();
        successUrl = "https://example.com/success";
        cancelUrl = "https://example.com/cancel";
        mockResponse = new StripeCheckoutSessionResponse();
    }

    // ========== createCheckoutSession - Success Tests ==========

    @Test
    void testCreateCheckoutSession_Success() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(orderId);
        request.setSuccessUrl(successUrl);
        request.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(orderId, successUrl, cancelUrl))
            .thenReturn(mockResponse);

        ResponseEntity<?> result = controller.createCheckoutSession(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testCreateCheckoutSession_VerifyServiceCalled() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(orderId);
        request.setSuccessUrl(successUrl);
        request.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(orderId, successUrl, cancelUrl))
            .thenReturn(mockResponse);

        controller.createCheckoutSession(request);

        verify(stripeCheckoutService).createCheckoutSession(orderId, successUrl, cancelUrl);
    }

    @Test
    void testCreateCheckoutSession_ResponseContainsSessionId() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(orderId);
        request.setSuccessUrl(successUrl);
        request.setCancelUrl(cancelUrl);

        mockResponse.setSessionId("cs_test_123");
        when(stripeCheckoutService.createCheckoutSession(orderId, successUrl, cancelUrl))
            .thenReturn(mockResponse);

        ResponseEntity<?> result = controller.createCheckoutSession(request);

        assertInstanceOf(StripeCheckoutSessionResponse.class, result.getBody());
    }

    @Test
    void testCreateCheckoutSession_WithDifferentUrls() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(orderId);
        request.setSuccessUrl("https://different.com/success");
        request.setCancelUrl("https://different.com/cancel");

        when(stripeCheckoutService.createCheckoutSession(
            orderId,
            "https://different.com/success",
            "https://different.com/cancel"))
            .thenReturn(mockResponse);

        ResponseEntity<?> result = controller.createCheckoutSession(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    // ========== createCheckoutSession - Error Tests ==========

    @Test
    void testCreateCheckoutSession_StripeException() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(orderId);
        request.setSuccessUrl(successUrl);
        request.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(orderId, successUrl, cancelUrl))
            .thenThrow(new ApiConnectionException("Stripe API error"));

        ResponseEntity<?> result = controller.createCheckoutSession(request);

        assertEquals(HttpStatus.BAD_GATEWAY, result.getStatusCode());
    }

    @Test
    void testCreateCheckoutSession_StripeException_ResponseBody() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(orderId);
        request.setSuccessUrl(successUrl);
        request.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(orderId, successUrl, cancelUrl))
            .thenThrow(new ApiConnectionException("Stripe API error"));

        ResponseEntity<?> result = controller.createCheckoutSession(request);

        assertTrue(result.getBody().toString().contains("Stripe error"));
    }

    @Test
    void testCreateCheckoutSession_MultipleStripeExceptions() throws StripeException {
        StripeCheckoutSessionRequest request1 = new StripeCheckoutSessionRequest();
        request1.setOrderId(orderId);
        request1.setSuccessUrl(successUrl);
        request1.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(orderId, successUrl, cancelUrl))
            .thenThrow(new ApiConnectionException("Error 1"))
            .thenThrow(new ApiConnectionException("Error 2"));

        ResponseEntity<?> result1 = controller.createCheckoutSession(request1);
        ResponseEntity<?> result2 = controller.createCheckoutSession(request1);

        assertEquals(HttpStatus.BAD_GATEWAY, result1.getStatusCode());
        assertEquals(HttpStatus.BAD_GATEWAY, result2.getStatusCode());
    }

    @Test
    void testCreateCheckoutSession_WithNullOrderId() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(null);
        request.setSuccessUrl(successUrl);
        request.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(null, successUrl, cancelUrl))
            .thenThrow(new ApiConnectionException("Invalid order ID"));

        ResponseEntity<?> result = controller.createCheckoutSession(request);

        assertEquals(HttpStatus.BAD_GATEWAY, result.getStatusCode());
    }

    @Test
    void testCreateCheckoutSession_SuccessUrlEmpty() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(orderId);
        request.setSuccessUrl("");
        request.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(orderId, "", cancelUrl))
            .thenThrow(new ApiConnectionException("Invalid URL"));

        ResponseEntity<?> result = controller.createCheckoutSession(request);

        assertEquals(HttpStatus.BAD_GATEWAY, result.getStatusCode());
    }

    @Test
    void testCreateCheckoutSession_SuccessOnSecondAttempt() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(orderId);
        request.setSuccessUrl(successUrl);
        request.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(orderId, successUrl, cancelUrl))
            .thenThrow(new ApiConnectionException("Temporary error"))
            .thenReturn(mockResponse);

        ResponseEntity<?> result1 = controller.createCheckoutSession(request);
        ResponseEntity<?> result2 = controller.createCheckoutSession(request);

        assertEquals(HttpStatus.BAD_GATEWAY, result1.getStatusCode());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
    }

    @Test
    void testCreateCheckoutSession_VerifyCallCountOnError() throws StripeException {
        StripeCheckoutSessionRequest request = new StripeCheckoutSessionRequest();
        request.setOrderId(orderId);
        request.setSuccessUrl(successUrl);
        request.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(orderId, successUrl, cancelUrl))
            .thenThrow(new ApiConnectionException("Error"));

        controller.createCheckoutSession(request);
        controller.createCheckoutSession(request);

        verify(stripeCheckoutService, times(2)).createCheckoutSession(orderId, successUrl, cancelUrl);
    }

    @Test
    void testCreateCheckoutSession_DifferentOrderIds() throws StripeException {
        UUID orderId2 = UUID.randomUUID();

        StripeCheckoutSessionRequest request1 = new StripeCheckoutSessionRequest();
        request1.setOrderId(orderId);
        request1.setSuccessUrl(successUrl);
        request1.setCancelUrl(cancelUrl);

        StripeCheckoutSessionRequest request2 = new StripeCheckoutSessionRequest();
        request2.setOrderId(orderId2);
        request2.setSuccessUrl(successUrl);
        request2.setCancelUrl(cancelUrl);

        when(stripeCheckoutService.createCheckoutSession(orderId, successUrl, cancelUrl))
            .thenReturn(mockResponse);
        when(stripeCheckoutService.createCheckoutSession(orderId2, successUrl, cancelUrl))
            .thenReturn(mockResponse);

        ResponseEntity<?> result1 = controller.createCheckoutSession(request1);
        ResponseEntity<?> result2 = controller.createCheckoutSession(request2);

        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
    }
}

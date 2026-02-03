package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.orchid.orchid_marketplace.dto.StripeConnectAuthorizeRequest;
import com.orchid.orchid_marketplace.dto.StripeConnectAuthorizeResponse;
import com.orchid.orchid_marketplace.dto.StripeConnectCallbackResponse;
import com.orchid.orchid_marketplace.service.stripe.StripeConnectService;

class StripeConnectControllerTest {

    private StripeConnectController controller;

    @Mock
    private StripeConnectService stripeConnectService;

    private UUID storeId;
    private String returnUrl;
    private String authCode;
    private UUID state;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stripeConnectService = mock(StripeConnectService.class);
        controller = new StripeConnectController(stripeConnectService);

        storeId = UUID.randomUUID();
        returnUrl = "https://example.com/return";
        authCode = "code_12345";
        state = UUID.randomUUID();
    }

    // ========== authorize Tests ==========

    @Test
    void testAuthorize_Success() {
        StripeConnectAuthorizeRequest request = new StripeConnectAuthorizeRequest();
        request.setStoreId(storeId);
        request.setReturnUrl(returnUrl);

        StripeConnectAuthorizeResponse response = new StripeConnectAuthorizeResponse();
        response.setUrl("https://stripe.com/authorize");

        when(stripeConnectService.createAuthorizeUrl(storeId, returnUrl)).thenReturn(response);

        ResponseEntity<?> result = controller.authorize(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testAuthorize_VerifyServiceCalled() {
        StripeConnectAuthorizeRequest request = new StripeConnectAuthorizeRequest();
        request.setStoreId(storeId);
        request.setReturnUrl(returnUrl);

        StripeConnectAuthorizeResponse response = new StripeConnectAuthorizeResponse();
        when(stripeConnectService.createAuthorizeUrl(storeId, returnUrl)).thenReturn(response);

        controller.authorize(request);

        verify(stripeConnectService).createAuthorizeUrl(storeId, returnUrl);
    }

    @Test
    void testAuthorize_IllegalStateException() {
        StripeConnectAuthorizeRequest request = new StripeConnectAuthorizeRequest();
        request.setStoreId(storeId);
        request.setReturnUrl(returnUrl);

        when(stripeConnectService.createAuthorizeUrl(storeId, returnUrl))
            .thenThrow(new IllegalStateException("Store already connected"));

        ResponseEntity<?> result = controller.authorize(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(result.getBody().toString().contains("already connected"));
    }

    @Test
    void testAuthorize_WithDifferentReturnUrl() {
        StripeConnectAuthorizeRequest request = new StripeConnectAuthorizeRequest();
        request.setStoreId(storeId);
        request.setReturnUrl("https://different.com/return");

        StripeConnectAuthorizeResponse response = new StripeConnectAuthorizeResponse();
        when(stripeConnectService.createAuthorizeUrl(storeId, "https://different.com/return")).thenReturn(response);

        ResponseEntity<?> result = controller.authorize(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAuthorize_MultipleRequests() {
        StripeConnectAuthorizeRequest request = new StripeConnectAuthorizeRequest();
        request.setStoreId(storeId);
        request.setReturnUrl(returnUrl);

        StripeConnectAuthorizeResponse response = new StripeConnectAuthorizeResponse();
        when(stripeConnectService.createAuthorizeUrl(storeId, returnUrl)).thenReturn(response);

        ResponseEntity<?> result1 = controller.authorize(request);
        ResponseEntity<?> result2 = controller.authorize(request);

        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
    }

    @Test
    void testAuthorize_DifferentStoreIds() {
        UUID storeId2 = UUID.randomUUID();

        StripeConnectAuthorizeRequest request1 = new StripeConnectAuthorizeRequest();
        request1.setStoreId(storeId);
        request1.setReturnUrl(returnUrl);

        StripeConnectAuthorizeRequest request2 = new StripeConnectAuthorizeRequest();
        request2.setStoreId(storeId2);
        request2.setReturnUrl(returnUrl);

        StripeConnectAuthorizeResponse response = new StripeConnectAuthorizeResponse();
        when(stripeConnectService.createAuthorizeUrl(anyUUID(), eq(returnUrl))).thenReturn(response);

        ResponseEntity<?> result1 = controller.authorize(request1);
        ResponseEntity<?> result2 = controller.authorize(request2);

        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
    }

    // ========== callback Tests ==========

    @Test
    void testCallback_Success() {
        StripeConnectCallbackResponse response = new StripeConnectCallbackResponse();
        response.setStripeAccountId("acct_123456");

        when(stripeConnectService.handleCallback(authCode, state)).thenReturn(response);

        ResponseEntity<?> result = controller.callback(authCode, state);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testCallback_VerifyServiceCalled() {
        StripeConnectCallbackResponse response = new StripeConnectCallbackResponse();
        when(stripeConnectService.handleCallback(authCode, state)).thenReturn(response);

        controller.callback(authCode, state);

        verify(stripeConnectService).handleCallback(authCode, state);
    }

    @Test
    void testCallback_IllegalArgumentException() {
        when(stripeConnectService.handleCallback(authCode, state))
            .thenThrow(new IllegalArgumentException("Invalid code"));

        ResponseEntity<?> result = controller.callback(authCode, state);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testCallback_IllegalStateException() {
        when(stripeConnectService.handleCallback(authCode, state))
            .thenThrow(new IllegalStateException("Store mismatch"));

        ResponseEntity<?> result = controller.callback(authCode, state);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void testCallback_GenericRuntimeException() {
        when(stripeConnectService.handleCallback(authCode, state))
            .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> result = controller.callback(authCode, state);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    // ========== connected Tests ==========

    @Test
    void testConnected_Success() {
        when(stripeConnectService.isStoreConnected(storeId)).thenReturn(true);

        ResponseEntity<?> result = controller.connected(storeId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(true, result.getBody());
        assertEquals(true, result.getBody());
    }

    @Test
    void testConnected_NotConnected() {
        when(stripeConnectService.isStoreConnected(storeId)).thenReturn(false);

        ResponseEntity<?> result = controller.connected(storeId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(false, result.getBody());
    }

    @Test
    void testConnected_VerifyServiceCalled() {
        when(stripeConnectService.isStoreConnected(storeId)).thenReturn(true);

        controller.connected(storeId);

        verify(stripeConnectService).isStoreConnected(storeId);
    }

    private UUID anyUUID() {
        return any(UUID.class);
    }
}

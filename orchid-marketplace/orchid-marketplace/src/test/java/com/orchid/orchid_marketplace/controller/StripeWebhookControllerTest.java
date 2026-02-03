package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.orchid.orchid_marketplace.service.stripe.StripeWebhookService;

class StripeWebhookControllerTest {

    private StripeWebhookController controller;

    @Mock
    private StripeWebhookService stripeWebhookService;

    private String testPayload;
    private String testSignature;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stripeWebhookService = mock(StripeWebhookService.class);
        controller = new StripeWebhookController(stripeWebhookService);

        testPayload = "{\"type\":\"payment_intent.succeeded\"}";
        testSignature = "t=1614556800,v1=test_signature_123";
    }

    // ========== handleWebhook - Success Tests ==========

    @Test
    void testHandleWebhook_Success() {
        doNothing().when(stripeWebhookService).handleWebhook(testPayload, testSignature);

        ResponseEntity<String> result = controller.handleWebhook(testPayload, testSignature);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("received", result.getBody());
    }

    @Test
    void testHandleWebhook_VerifyServiceCalled() {
        doNothing().when(stripeWebhookService).handleWebhook(testPayload, testSignature);

        controller.handleWebhook(testPayload, testSignature);

        verify(stripeWebhookService).handleWebhook(testPayload, testSignature);
    }

    @Test
    void testHandleWebhook_WithNullSignature() {
        doNothing().when(stripeWebhookService).handleWebhook(testPayload, null);

        ResponseEntity<String> result = controller.handleWebhook(testPayload, null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testHandleWebhook_MultipleEvents() {
        String payload2 = "{\"type\":\"charge.completed\"}";

        doNothing().when(stripeWebhookService).handleWebhook(anyString(), anyString());

        ResponseEntity<String> result1 = controller.handleWebhook(testPayload, testSignature);
        ResponseEntity<String> result2 = controller.handleWebhook(payload2, testSignature);

        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
    }

    @Test
    void testHandleWebhook_EmptyPayload() {
        doNothing().when(stripeWebhookService).handleWebhook("", testSignature);

        ResponseEntity<String> result = controller.handleWebhook("", testSignature);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testHandleWebhook_LargePayload() {
        String largePayload = "{\"type\":\"payment_intent.succeeded\",\"data\":\"" + "x".repeat(10000) + "\"}";

        doNothing().when(stripeWebhookService).handleWebhook(largePayload, testSignature);

        ResponseEntity<String> result = controller.handleWebhook(largePayload, testSignature);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testHandleWebhook_VerifyCallCount() {
        doNothing().when(stripeWebhookService).handleWebhook(anyString(), anyString());

        controller.handleWebhook(testPayload, testSignature);
        controller.handleWebhook(testPayload, testSignature);
        controller.handleWebhook(testPayload, testSignature);

        verify(stripeWebhookService, times(3)).handleWebhook(testPayload, testSignature);
    }

    // ========== handleWebhook - Error Tests ==========

    @Test
    void testHandleWebhook_IllegalArgumentException() {
        doThrow(new IllegalArgumentException("Invalid signature")).when(stripeWebhookService).handleWebhook(testPayload, testSignature);

        ResponseEntity<String> result = controller.handleWebhook(testPayload, testSignature);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(result.getBody().contains("Invalid signature"));
    }

    @Test
    void testHandleWebhook_IllegalStateException() {
        doThrow(new IllegalStateException("Webhook already processed")).when(stripeWebhookService).handleWebhook(testPayload, testSignature);

        ResponseEntity<String> result = controller.handleWebhook(testPayload, testSignature);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(result.getBody().contains("already processed"));
    }

    @Test
    void testHandleWebhook_InvalidSignatureFormat() {
        String invalidSignature = "invalid_format";

        doThrow(new IllegalArgumentException("Invalid signature format")).when(stripeWebhookService)
            .handleWebhook(testPayload, invalidSignature);

        ResponseEntity<String> result = controller.handleWebhook(testPayload, invalidSignature);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testHandleWebhook_MalformedJson() {
        String malformedPayload = "{\"type\":\"invalid}";

        doThrow(new IllegalArgumentException("Malformed JSON")).when(stripeWebhookService)
            .handleWebhook(malformedPayload, testSignature);

        ResponseEntity<String> result = controller.handleWebhook(malformedPayload, testSignature);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testHandleWebhook_MultipleExceptionTypes() {
        doThrow(new IllegalArgumentException("Invalid sig"))
            .doThrow(new IllegalStateException("Already processed"))
            .doNothing()
            .when(stripeWebhookService)
            .handleWebhook(anyString(), anyString());

        ResponseEntity<String> result1 = controller.handleWebhook(testPayload, testSignature);
        ResponseEntity<String> result2 = controller.handleWebhook(testPayload, testSignature);
        ResponseEntity<String> result3 = controller.handleWebhook(testPayload, testSignature);

        assertEquals(HttpStatus.BAD_REQUEST, result1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, result2.getStatusCode());
        assertEquals(HttpStatus.OK, result3.getStatusCode());
    }

    @Test
    void testHandleWebhook_DifferentPayloads() {
        String payload1 = "{\"type\":\"payment_intent.succeeded\"}";
        String payload2 = "{\"type\":\"charge.failed\"}";

        doNothing().when(stripeWebhookService).handleWebhook(anyString(), anyString());

        ResponseEntity<String> result1 = controller.handleWebhook(payload1, testSignature);
        ResponseEntity<String> result2 = controller.handleWebhook(payload2, testSignature);

        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        verify(stripeWebhookService, times(2)).handleWebhook(anyString(), anyString());
    }

    @Test
    void testHandleWebhook_DifferentSignatures() {
        String sig1 = "t=1614556800,v1=sig1";
        String sig2 = "t=1614556801,v1=sig2";

        doNothing().when(stripeWebhookService).handleWebhook(eq(testPayload), anyString());

        ResponseEntity<String> result1 = controller.handleWebhook(testPayload, sig1);
        ResponseEntity<String> result2 = controller.handleWebhook(testPayload, sig2);

        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
    }
}

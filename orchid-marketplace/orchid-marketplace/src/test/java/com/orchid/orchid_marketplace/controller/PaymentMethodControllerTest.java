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

import com.orchid.orchid_marketplace.dto.PaymentMethodRequest;
import com.orchid.orchid_marketplace.dto.PaymentMethodResponse;
import com.orchid.orchid_marketplace.model.PaymentMethod;
import com.orchid.orchid_marketplace.service.PaymentMethodService;

class PaymentMethodControllerTest {

    private PaymentMethodController controller;

    @Mock
    private PaymentMethodService paymentMethodService;

    private UUID paymentMethodId;
    private PaymentMethod testPaymentMethod;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PaymentMethodController();
        ReflectionTestUtils.setField(controller, "paymentMethodService", paymentMethodService);

        paymentMethodId = UUID.randomUUID();
        testPaymentMethod = new PaymentMethod();
        testPaymentMethod.setId(paymentMethodId);
        testPaymentMethod.setBrand("VISA");
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        when(paymentMethodService.getAllPaymentMethods()).thenReturn(List.of(testPaymentMethod));

        List<PaymentMethodResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(paymentMethodService.getAllPaymentMethods()).thenReturn(List.of());

        List<PaymentMethodResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_MultiplePaymentMethods() {
        PaymentMethod pm2 = new PaymentMethod();
        pm2.setId(UUID.randomUUID());
        pm2.setBrand("MASTERCARD");

        when(paymentMethodService.getAllPaymentMethods()).thenReturn(List.of(testPaymentMethod, pm2));

        List<PaymentMethodResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(paymentMethodService.getPaymentMethodById(paymentMethodId)).thenReturn(Optional.of(testPaymentMethod));

        ResponseEntity<PaymentMethodResponse> result = controller.getById(paymentMethodId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(paymentMethodService.getPaymentMethodById(paymentMethodId)).thenReturn(Optional.empty());

        ResponseEntity<PaymentMethodResponse> result = controller.getById(paymentMethodId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(paymentMethodService.getPaymentMethodById(paymentMethodId)).thenReturn(Optional.of(testPaymentMethod));

        controller.getById(paymentMethodId);

        verify(paymentMethodService).getPaymentMethodById(paymentMethodId);
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        PaymentMethodRequest request = new PaymentMethodRequest();

        when(paymentMethodService.createPaymentMethod(any(PaymentMethod.class))).thenReturn(testPaymentMethod);

        PaymentMethodResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        PaymentMethodRequest request = new PaymentMethodRequest();

        when(paymentMethodService.createPaymentMethod(any(PaymentMethod.class))).thenReturn(testPaymentMethod);

        controller.create(request);

        verify(paymentMethodService).createPaymentMethod(any(PaymentMethod.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        PaymentMethodRequest request = new PaymentMethodRequest();

        when(paymentMethodService.updatePaymentMethod(eq(paymentMethodId), any(PaymentMethod.class))).thenReturn(testPaymentMethod);

        PaymentMethodResponse result = controller.update(paymentMethodId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        PaymentMethodRequest request = new PaymentMethodRequest();

        when(paymentMethodService.updatePaymentMethod(eq(paymentMethodId), any(PaymentMethod.class))).thenReturn(testPaymentMethod);

        controller.update(paymentMethodId, request);

        verify(paymentMethodService).updatePaymentMethod(eq(paymentMethodId), any(PaymentMethod.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(paymentMethodService).deletePaymentMethod(paymentMethodId);

        ResponseEntity<Void> result = controller.delete(paymentMethodId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(paymentMethodService).deletePaymentMethod(paymentMethodId);

        controller.delete(paymentMethodId);

        verify(paymentMethodService).deletePaymentMethod(paymentMethodId);
    }
}

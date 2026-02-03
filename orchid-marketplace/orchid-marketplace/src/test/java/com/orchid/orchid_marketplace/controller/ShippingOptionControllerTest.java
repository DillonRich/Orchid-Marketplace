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

import com.orchid.orchid_marketplace.dto.ShippingOptionRequest;
import com.orchid.orchid_marketplace.dto.ShippingOptionResponse;
import com.orchid.orchid_marketplace.model.ShippingOption;
import com.orchid.orchid_marketplace.service.ShippingOptionService;

class ShippingOptionControllerTest {

    private ShippingOptionController controller;

    @Mock
    private ShippingOptionService shippingService;

    private UUID shippingId;
    private ShippingOption testOption;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ShippingOptionController();
        ReflectionTestUtils.setField(controller, "shippingService", shippingService);

        shippingId = UUID.randomUUID();
        testOption = new ShippingOption();
        testOption.setId(shippingId);
        testOption.setOptionName("Standard");
    }

    // ========== listAll Tests ==========

    @Test
    void testListAll_Success() {
        when(shippingService.getAllShippingOptions()).thenReturn(List.of(testOption));

        List<ShippingOptionResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListAll_Empty() {
        when(shippingService.getAllShippingOptions()).thenReturn(List.of());

        List<ShippingOptionResponse> result = controller.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAll_MultipleOptions() {
        ShippingOption option2 = new ShippingOption();
        option2.setId(UUID.randomUUID());
        option2.setOptionName("Express");

        when(shippingService.getAllShippingOptions()).thenReturn(List.of(testOption, option2));

        List<ShippingOptionResponse> result = controller.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========== getById Tests ==========

    @Test
    void testGetById_Success() {
        when(shippingService.getShippingOptionById(shippingId)).thenReturn(Optional.of(testOption));

        ResponseEntity<ShippingOptionResponse> result = controller.getById(shippingId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(shippingService.getShippingOptionById(shippingId)).thenReturn(Optional.empty());

        ResponseEntity<ShippingOptionResponse> result = controller.getById(shippingId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetById_VerifyServiceCalled() {
        when(shippingService.getShippingOptionById(shippingId)).thenReturn(Optional.of(testOption));

        controller.getById(shippingId);

        verify(shippingService).getShippingOptionById(shippingId);
    }

    // ========== create Tests ==========

    @Test
    void testCreate_Success() {
        ShippingOptionRequest request = new ShippingOptionRequest();

        when(shippingService.createShippingOption(any(ShippingOption.class))).thenReturn(testOption);

        ShippingOptionResponse result = controller.create(request);

        assertNotNull(result);
    }

    @Test
    void testCreate_VerifyServiceCalled() {
        ShippingOptionRequest request = new ShippingOptionRequest();

        when(shippingService.createShippingOption(any(ShippingOption.class))).thenReturn(testOption);

        controller.create(request);

        verify(shippingService).createShippingOption(any(ShippingOption.class));
    }

    // ========== update Tests ==========

    @Test
    void testUpdate_Success() {
        ShippingOptionRequest request = new ShippingOptionRequest();

        when(shippingService.updateShippingOption(eq(shippingId), any(ShippingOption.class))).thenReturn(testOption);

        ShippingOptionResponse result = controller.update(shippingId, request);

        assertNotNull(result);
    }

    @Test
    void testUpdate_VerifyServiceCalled() {
        ShippingOptionRequest request = new ShippingOptionRequest();

        when(shippingService.updateShippingOption(eq(shippingId), any(ShippingOption.class))).thenReturn(testOption);

        controller.update(shippingId, request);

        verify(shippingService).updateShippingOption(eq(shippingId), any(ShippingOption.class));
    }

    // ========== delete Tests ==========

    @Test
    void testDelete_Success() {
        doNothing().when(shippingService).deleteShippingOption(shippingId);

        ResponseEntity<Void> result = controller.delete(shippingId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testDelete_VerifyServiceCalled() {
        doNothing().when(shippingService).deleteShippingOption(shippingId);

        controller.delete(shippingId);

        verify(shippingService).deleteShippingOption(shippingId);
    }
}

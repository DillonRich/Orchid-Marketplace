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

import com.orchid.orchid_marketplace.dto.SellerDashboardDTO;
import com.orchid.orchid_marketplace.service.SellerDashboardService;

class SellerDashboardControllerTest {

    private SellerDashboardController controller;

    @Mock
    private SellerDashboardService dashboardService;

    private UUID storeId;
    private SellerDashboardDTO testDashboard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new SellerDashboardController(dashboardService);

        storeId = UUID.randomUUID();
        testDashboard = new SellerDashboardDTO();
    }

    // ========== getDashboard Tests ==========

    @Test
    void testGetDashboard_Success() {
        when(dashboardService.getDashboard(storeId)).thenReturn(testDashboard);

        ResponseEntity<SellerDashboardDTO> result = controller.getDashboard(storeId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testGetDashboard_VerifyServiceCalled() {
        when(dashboardService.getDashboard(storeId)).thenReturn(testDashboard);

        controller.getDashboard(storeId);

        verify(dashboardService).getDashboard(storeId);
    }

    @Test
    void testGetDashboard_DifferentStoreIds() {
        UUID storeId2 = UUID.randomUUID();
        SellerDashboardDTO dashboard2 = new SellerDashboardDTO();

        when(dashboardService.getDashboard(storeId)).thenReturn(testDashboard);
        when(dashboardService.getDashboard(storeId2)).thenReturn(dashboard2);

        ResponseEntity<SellerDashboardDTO> result1 = controller.getDashboard(storeId);
        ResponseEntity<SellerDashboardDTO> result2 = controller.getDashboard(storeId2);

        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        verify(dashboardService).getDashboard(storeId);
        verify(dashboardService).getDashboard(storeId2);
    }

    @Test
    void testGetDashboard_WithData() {
        SellerDashboardDTO dashboard = new SellerDashboardDTO();
        when(dashboardService.getDashboard(storeId)).thenReturn(dashboard);

        ResponseEntity<SellerDashboardDTO> result = controller.getDashboard(storeId);

        assertNotNull(result.getBody());
    }

    @Test
    void testGetDashboard_MultipleCallsSameStore() {
        when(dashboardService.getDashboard(storeId)).thenReturn(testDashboard);

        controller.getDashboard(storeId);
        controller.getDashboard(storeId);

        verify(dashboardService, times(2)).getDashboard(storeId);
    }

    @Test
    void testGetDashboard_ReturnsCorrectType() {
        when(dashboardService.getDashboard(storeId)).thenReturn(testDashboard);

        ResponseEntity<SellerDashboardDTO> result = controller.getDashboard(storeId);

        assertNotNull(result.getBody());
        assertTrue(result.getBody() instanceof SellerDashboardDTO);
    }

    @Test
    void testGetDashboard_StatusCodeOK() {
        when(dashboardService.getDashboard(storeId)).thenReturn(testDashboard);

        ResponseEntity<SellerDashboardDTO> result = controller.getDashboard(storeId);

        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testGetDashboard_BodyNotNull() {
        when(dashboardService.getDashboard(storeId)).thenReturn(testDashboard);

        ResponseEntity<SellerDashboardDTO> result = controller.getDashboard(storeId);

        assertNotNull(result.getBody());
    }
}

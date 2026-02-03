package com.orchid.orchid_marketplace.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.orchid.orchid_marketplace.dto.CreateShipmentRequest;
import com.orchid.orchid_marketplace.model.OrderShipment;
import com.orchid.orchid_marketplace.service.OrderShipmentService;

class OrderShipmentControllerTest {

    private OrderShipmentController controller;

    @Mock
    private OrderShipmentService shipmentService;

    private UUID shipmentId;
    private UUID orderId;
    private OrderShipment testShipment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shipmentService = mock(OrderShipmentService.class);
        controller = new OrderShipmentController(shipmentService);

        shipmentId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        testShipment = new OrderShipment();
        testShipment.setId(shipmentId);
        testShipment.setTrackingNumber("TRACK123456");
        testShipment.setCarrier("FedEx");
    }

    // ========== createShipment Tests ==========

    @Test
    void testCreateShipment_Success() {
        CreateShipmentRequest request = new CreateShipmentRequest();
        request.setOrderId(orderId);
        request.setTrackingNumber("TRACK123456");
        request.setCarrier("FedEx");

        when(shipmentService.createShipment(any(), any(), any(), any(), any())).thenReturn(testShipment);

        ResponseEntity<OrderShipment> result = controller.createShipment(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("TRACK123456", result.getBody().getTrackingNumber());
    }

    @Test
    void testCreateShipment_VerifyServiceCalled() {
        CreateShipmentRequest request = new CreateShipmentRequest();
        request.setOrderId(orderId);
        request.setTrackingNumber("TRACK123456");
        request.setCarrier("FedEx");

        when(shipmentService.createShipment(any(), any(), any(), any(), any())).thenReturn(testShipment);

        controller.createShipment(request);

        verify(shipmentService).createShipment(any(), any(), any(), any(), any());
    }

    // ========== markAsShipped Tests ==========

    @Test
    void testMarkAsShipped_Success() {
        when(shipmentService.markAsShipped(shipmentId)).thenReturn(testShipment);

        ResponseEntity<OrderShipment> result = controller.markAsShipped(shipmentId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testMarkAsShipped_VerifyServiceCalled() {
        when(shipmentService.markAsShipped(shipmentId)).thenReturn(testShipment);

        controller.markAsShipped(shipmentId);

        verify(shipmentService).markAsShipped(shipmentId);
    }

    // ========== updateStatus Tests ==========

    @Test
    void testUpdateStatus_Success() {
        when(shipmentService.updateShipmentStatus(eq(shipmentId), any(OrderShipment.ShipmentStatus.class))).thenReturn(testShipment);

        ResponseEntity<OrderShipment> result = controller.updateStatus(shipmentId, OrderShipment.ShipmentStatus.SHIPPED);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testUpdateStatus_VerifyServiceCalled() {
        when(shipmentService.updateShipmentStatus(eq(shipmentId), any(OrderShipment.ShipmentStatus.class))).thenReturn(testShipment);

        controller.updateStatus(shipmentId, OrderShipment.ShipmentStatus.SHIPPED);

        verify(shipmentService).updateShipmentStatus(eq(shipmentId), any(OrderShipment.ShipmentStatus.class));
    }

    @Test
    void testUpdateStatus_DifferentStatuses() {
        when(shipmentService.updateShipmentStatus(eq(shipmentId), any(OrderShipment.ShipmentStatus.class))).thenReturn(testShipment);

        controller.updateStatus(shipmentId, OrderShipment.ShipmentStatus.IN_TRANSIT);

        verify(shipmentService).updateShipmentStatus(eq(shipmentId), eq(OrderShipment.ShipmentStatus.IN_TRANSIT));
    }

    // ========== getShipmentsByOrder Tests ==========

    @Test
    void testGetShipmentsByOrder_Success() {
        when(shipmentService.getShipmentsByOrder(orderId)).thenReturn(List.of(testShipment));

        ResponseEntity<List<OrderShipment>> result = controller.getShipmentsByOrder(orderId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testGetShipmentsByOrder_Empty() {
        when(shipmentService.getShipmentsByOrder(orderId)).thenReturn(List.of());

        ResponseEntity<List<OrderShipment>> result = controller.getShipmentsByOrder(orderId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void testGetShipmentsByOrder_MultipleShipments() {
        OrderShipment shipment2 = new OrderShipment();
        shipment2.setId(UUID.randomUUID());
        shipment2.setTrackingNumber("TRACK789");

        when(shipmentService.getShipmentsByOrder(orderId)).thenReturn(List.of(testShipment, shipment2));

        ResponseEntity<List<OrderShipment>> result = controller.getShipmentsByOrder(orderId);

        assertEquals(2, result.getBody().size());
    }

    @Test
    void testGetShipmentsByOrder_VerifyServiceCalled() {
        when(shipmentService.getShipmentsByOrder(orderId)).thenReturn(List.of(testShipment));

        controller.getShipmentsByOrder(orderId);

        verify(shipmentService).getShipmentsByOrder(orderId);
    }

    // ========== getShipmentByTracking Tests ==========

    @Test
    void testGetShipmentByTracking_Success() {
        when(shipmentService.getShipmentByTrackingNumber("TRACK123456")).thenReturn(testShipment);

        ResponseEntity<OrderShipment> result = controller.getShipmentByTracking("TRACK123456");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("TRACK123456", result.getBody().getTrackingNumber());
    }

    @Test
    void testGetShipmentByTracking_VerifyServiceCalled() {
        when(shipmentService.getShipmentByTrackingNumber("TRACK123456")).thenReturn(testShipment);

        controller.getShipmentByTracking("TRACK123456");

        verify(shipmentService).getShipmentByTrackingNumber("TRACK123456");
    }

    @Test
    void testGetShipmentByTracking_DifferentTrackingNumbers() {
        when(shipmentService.getShipmentByTrackingNumber("TRACK999")).thenReturn(testShipment);

        ResponseEntity<OrderShipment> result = controller.getShipmentByTracking("TRACK999");

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}

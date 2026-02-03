package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.CreateShipmentRequest;
import com.orchid.orchid_marketplace.model.OrderShipment;
import com.orchid.orchid_marketplace.service.OrderShipmentService;

import jakarta.validation.Valid;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/shipments")
@Validated
public class OrderShipmentController {

    private final OrderShipmentService shipmentService;

    public OrderShipmentController(OrderShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ResponseEntity<OrderShipment> createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        OrderShipment shipment = shipmentService.createShipment(
            request.getOrderId(),
            request.getTrackingNumber(),
            request.getCarrier(),
            request.getEstimatedDeliveryAt(),
            request.getNotes()
        );
        return ResponseEntity.ok(shipment);
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderShipment> markAsShipped(@PathVariable UUID id) {
        OrderShipment shipment = shipmentService.markAsShipped(id);
        return ResponseEntity.ok(shipment);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderShipment> updateStatus(
        @PathVariable UUID id,
        @RequestParam OrderShipment.ShipmentStatus status
    ) {
        OrderShipment shipment = shipmentService.updateShipmentStatus(id, status);
        return ResponseEntity.ok(shipment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderShipment>> getShipmentsByOrder(@PathVariable UUID orderId) {
        List<OrderShipment> shipments = shipmentService.getShipmentsByOrder(orderId);
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<OrderShipment> getShipmentByTracking(@PathVariable String trackingNumber) {
        OrderShipment shipment = shipmentService.getShipmentByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(shipment);
    }
}

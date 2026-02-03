package com.orchid.orchid_marketplace.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.OrderShipment;
import com.orchid.orchid_marketplace.repository.OrderRepository;
import com.orchid.orchid_marketplace.repository.OrderShipmentRepository;

@Service
@Profile("!cosmos")
public class OrderShipmentService {

    private final OrderShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;

    public OrderShipmentService(
        OrderShipmentRepository shipmentRepository,
        OrderRepository orderRepository
    ) {
        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @SuppressWarnings("null")
    public OrderShipment createShipment(UUID orderId, String trackingNumber, String carrier, 
                                       LocalDateTime estimatedDelivery, String notes) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        // Validate order is in correct state for shipment
        if (order.getStatus() != Order.OrderStatus.PROCESSING) {
            throw new IllegalStateException("Order must be in PROCESSING status to create shipment");
        }

        OrderShipment shipment = new OrderShipment();
        shipment.setOrder(order);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setCarrier(carrier);
        shipment.setStatus(OrderShipment.ShipmentStatus.PENDING);
        shipment.setEstimatedDeliveryAt(estimatedDelivery);
        shipment.setNotes(notes);

        return shipmentRepository.save(shipment);
    }

    @Transactional
    @SuppressWarnings("null")
    public OrderShipment markAsShipped(UUID shipmentId) {
        OrderShipment shipment = shipmentRepository.findById(shipmentId)
            .orElseThrow(() -> new RuntimeException("Shipment not found"));

        shipment.setStatus(OrderShipment.ShipmentStatus.SHIPPED);
        shipment.setShippedAt(LocalDateTime.now());

        // Update order status
        Order order = shipment.getOrder();
        if (order != null && order.getStatus() == Order.OrderStatus.PROCESSING) {
            order.setStatus(Order.OrderStatus.SHIPPED);
            orderRepository.save(order);
        }

        return shipmentRepository.save(shipment);
    }

    @Transactional
    @SuppressWarnings("null")
    public OrderShipment updateShipmentStatus(UUID shipmentId, OrderShipment.ShipmentStatus newStatus) {
        OrderShipment shipment = shipmentRepository.findById(shipmentId)
            .orElseThrow(() -> new RuntimeException("Shipment not found"));

        shipment.setStatus(newStatus);

        // Set timestamps based on status
        if (newStatus == OrderShipment.ShipmentStatus.DELIVERED && shipment.getDeliveredAt() == null) {
            shipment.setDeliveredAt(LocalDateTime.now());
            
            // Update order status to DELIVERED
            Order order = shipment.getOrder();
            if (order != null) {
                order.setStatus(Order.OrderStatus.DELIVERED);
                orderRepository.save(order);
            }
        }

        return shipmentRepository.save(shipment);
    }

    @Transactional(readOnly = true)
    public List<OrderShipment> getShipmentsByOrder(UUID orderId) {
        return shipmentRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public OrderShipment getShipmentByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
            .orElseThrow(() -> new RuntimeException("Shipment not found with tracking number: " + trackingNumber));
    }
}

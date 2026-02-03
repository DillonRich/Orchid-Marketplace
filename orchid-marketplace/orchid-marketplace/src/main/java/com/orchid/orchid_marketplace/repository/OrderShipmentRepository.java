package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.OrderShipment;

@Repository
public interface OrderShipmentRepository extends JpaRepository<OrderShipment, UUID> {
    
    List<OrderShipment> findByOrderId(UUID orderId);
    
    Optional<OrderShipment> findByTrackingNumber(String trackingNumber);
    
    List<OrderShipment> findByStatus(OrderShipment.ShipmentStatus status);
}

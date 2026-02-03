package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.OrderItemCreateRequest;
import com.orchid.orchid_marketplace.dto.OrderItemRequest;
import com.orchid.orchid_marketplace.dto.OrderItemResponse;
import com.orchid.orchid_marketplace.mapper.OrderItemMapper;
import com.orchid.orchid_marketplace.model.OrderItem;
import com.orchid.orchid_marketplace.service.OrderItemService;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public List<OrderItemResponse> listAll() {
        return orderItemService.getAllOrderItems()
            .stream()
            .map(OrderItemMapper::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getById(@PathVariable UUID id) {
        return orderItemService.getOrderItemById(id).map(OrderItemMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OrderItemResponse create(@jakarta.validation.Valid @RequestBody OrderItemCreateRequest req) {
        OrderItem saved = orderItemService.createOrderItem(req.getOrderId(), req.getProductId(), req.getQuantity());
        return OrderItemMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public OrderItemResponse update(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody OrderItemRequest req) {
        OrderItem oi = OrderItemMapper.toEntity(req);
        OrderItem updated = orderItemService.updateOrderItem(id, oi);
        return OrderItemMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}

package com.orchid.orchid_marketplace.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.OrderRequest;
import com.orchid.orchid_marketplace.dto.OrderResponse;
import com.orchid.orchid_marketplace.mapper.OrderMapper;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@Profile("!cosmos")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderResponse> listAll() {
        return orderService.getAllOrders()
            .stream()
            .map(OrderMapper::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable UUID id) {
        return orderService.getOrderById(id).map(OrderMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-buyer/{buyerId}")
    public List<Order> byBuyer(@PathVariable UUID buyerId) {
        return orderService.getOrdersByBuyerId(buyerId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public OrderResponse create(@jakarta.validation.Valid @RequestBody OrderRequest req) {
        Order o = OrderMapper.toEntity(req);
        Order saved = orderService.createOrder(o);
        return OrderMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public OrderResponse update(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody OrderRequest req) {
        Order o = OrderMapper.toEntity(req);
        Order updated = orderService.updateOrder(id, o);
        return OrderMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Order> byOrderNumber(@RequestParam String number) {
        return orderService.getOrderByOrderNumber(number).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

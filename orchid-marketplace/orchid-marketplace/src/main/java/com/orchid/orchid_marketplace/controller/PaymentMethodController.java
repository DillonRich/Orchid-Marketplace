package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.PaymentMethodRequest;
import com.orchid.orchid_marketplace.dto.PaymentMethodResponse;
import com.orchid.orchid_marketplace.mapper.PaymentMethodMapper;
import com.orchid.orchid_marketplace.model.PaymentMethod;
import com.orchid.orchid_marketplace.service.PaymentMethodService;

import jakarta.validation.Valid;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
    public List<PaymentMethodResponse> listAll() {
        return paymentMethodService.getAllPaymentMethods()
            .stream()
            .map(PaymentMethodMapper::toResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodResponse> getById(@PathVariable UUID id) {
        return paymentMethodService.getPaymentMethodById(id).map(PaymentMethodMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public PaymentMethodResponse create(@Valid @RequestBody PaymentMethodRequest req) {
        PaymentMethod pm = PaymentMethodMapper.toEntity(req);
        PaymentMethod saved = paymentMethodService.createPaymentMethod(pm);
        return PaymentMethodMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public PaymentMethodResponse update(@PathVariable UUID id, @Valid @RequestBody PaymentMethodRequest req) {
        PaymentMethod pm = PaymentMethodMapper.toEntity(req);
        PaymentMethod updated = paymentMethodService.updatePaymentMethod(id, pm);
        return PaymentMethodMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }
}

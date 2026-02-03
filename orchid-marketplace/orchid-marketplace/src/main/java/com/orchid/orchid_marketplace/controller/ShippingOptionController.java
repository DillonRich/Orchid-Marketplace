package com.orchid.orchid_marketplace.controller;

import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.orchid.orchid_marketplace.dto.ShippingOptionRequest;
import com.orchid.orchid_marketplace.dto.ShippingOptionResponse;
import com.orchid.orchid_marketplace.mapper.ShippingOptionMapper;
import com.orchid.orchid_marketplace.model.ShippingOption;
import com.orchid.orchid_marketplace.service.ShippingOptionService;

import jakarta.validation.Valid;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/shipping-options")
public class ShippingOptionController {

    @Autowired
    private ShippingOptionService shippingService;

    @GetMapping
    public List<ShippingOptionResponse> listAll() {
        return shippingService.getAllShippingOptions()
            .stream()
            .map(ShippingOptionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingOptionResponse> getById(@PathVariable UUID id) {
        return shippingService.getShippingOptionById(id).map(ShippingOptionMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ShippingOptionResponse create(@Valid @RequestBody ShippingOptionRequest req) {
        ShippingOption s = ShippingOptionMapper.toEntity(req);
        ShippingOption saved = shippingService.createShippingOption(s);
        return ShippingOptionMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public ShippingOptionResponse update(@PathVariable UUID id, @Valid @RequestBody ShippingOptionRequest req) {
        ShippingOption s = ShippingOptionMapper.toEntity(req);
        ShippingOption updated = shippingService.updateShippingOption(id, s);
        return ShippingOptionMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        shippingService.deleteShippingOption(id);
        return ResponseEntity.noContent().build();
    }
}

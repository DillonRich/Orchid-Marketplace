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
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.AddressRequest;
import com.orchid.orchid_marketplace.dto.AddressResponse;
import com.orchid.orchid_marketplace.mapper.AddressMapper;
import com.orchid.orchid_marketplace.model.Address;
import com.orchid.orchid_marketplace.service.AddressService;

@RestController
@RequestMapping("/api/addresses")
@Profile("!cosmos")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<AddressResponse> listAll() {
        return addressService.getAll()
            .stream()
            .map(AddressMapper::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getById(@PathVariable UUID id) {
        return addressService.getById(id)
            .map(AddressMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public AddressResponse create(@jakarta.validation.Valid @RequestBody AddressRequest req) {
        Address a = AddressMapper.toEntity(req);
        Address saved = addressService.create(a);
        return AddressMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public AddressResponse update(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody AddressRequest req) {
        Address a = AddressMapper.toEntity(req);
        Address updated = addressService.update(id, a);
        return AddressMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}

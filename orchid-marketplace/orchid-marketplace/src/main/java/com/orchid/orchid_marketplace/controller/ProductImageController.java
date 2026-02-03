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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.ProductImageRequest;
import com.orchid.orchid_marketplace.dto.ProductImageResponse;
import com.orchid.orchid_marketplace.mapper.ProductImageMapper;
import com.orchid.orchid_marketplace.model.ProductImage;
import com.orchid.orchid_marketplace.service.ProductImageService;

import jakarta.validation.Valid;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/product-images")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @GetMapping
    public List<ProductImageResponse> listAll() {
        return productImageService.getAllProductImages()
            .stream()
            .map(ProductImageMapper::toResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageResponse> getById(@PathVariable UUID id) {
        return productImageService.getProductImageById(id).map(ProductImageMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-product")
    public List<ProductImageResponse> byProduct(@RequestParam UUID productId) {
        return productImageService.getProductImagesByProductId(productId)
            .stream()
            .map(ProductImageMapper::toResponse)
            .collect(Collectors.toList());
    }

    @PostMapping
    public ProductImageResponse create(@Valid @RequestBody ProductImageRequest req) {
        ProductImage pi = ProductImageMapper.toEntity(req);
        ProductImage saved = productImageService.createProductImage(pi);
        return ProductImageMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public ProductImageResponse update(@PathVariable UUID id, @Valid @RequestBody ProductImageRequest req) {
        ProductImage pi = ProductImageMapper.toEntity(req);
        ProductImage updated = productImageService.updateProductImage(id, pi);
        return ProductImageMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productImageService.deleteProductImage(id);
        return ResponseEntity.noContent().build();
    }
}

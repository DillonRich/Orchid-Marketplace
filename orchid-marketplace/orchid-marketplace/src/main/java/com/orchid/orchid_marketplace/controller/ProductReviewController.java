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

import com.orchid.orchid_marketplace.dto.ProductReviewRequest;
import com.orchid.orchid_marketplace.dto.ProductReviewResponse;
import com.orchid.orchid_marketplace.dto.SellerResponseRequest;
import com.orchid.orchid_marketplace.mapper.ProductReviewMapper;
import com.orchid.orchid_marketplace.model.ProductReview;
import com.orchid.orchid_marketplace.service.ProductReviewService;

import jakarta.validation.Valid;

@RestController
@Profile("!cosmos")
@RequestMapping("/api/reviews")
public class ProductReviewController {

    @Autowired
    private ProductReviewService reviewService;

    @GetMapping
    public List<ProductReviewResponse> listAll() {
        return reviewService.getAllProductReviews()
            .stream()
            .map(ProductReviewMapper::toResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductReviewResponse> getById(@PathVariable UUID id) {
        return reviewService.getProductReviewById(id).map(ProductReviewMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-product")
    public List<ProductReviewResponse> byProduct(@RequestParam UUID productId) {
        return reviewService.getReviewsByProductId(productId)
            .stream()
            .map(ProductReviewMapper::toResponse)
            .collect(Collectors.toList());
    }

    @PostMapping
    public ProductReviewResponse create(@Valid @RequestBody ProductReviewRequest req) {
        ProductReview pr = ProductReviewMapper.toEntity(req);
        ProductReview saved = reviewService.createProductReview(pr);
        return ProductReviewMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public ProductReviewResponse update(@PathVariable UUID id, @Valid @RequestBody ProductReviewRequest req) {
        ProductReview pr = ProductReviewMapper.toEntity(req);
        ProductReview updated = reviewService.updateProductReview(id, pr);
        return ProductReviewMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reviewService.deleteProductReview(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/seller-response")
    public ProductReviewResponse addSellerResponse(
        @PathVariable UUID id,
        @Valid @RequestBody SellerResponseRequest request
    ) {
        ProductReview updated = reviewService.addSellerResponse(id, request.getResponse());
        return ProductReviewMapper.toResponse(updated);
    }
}

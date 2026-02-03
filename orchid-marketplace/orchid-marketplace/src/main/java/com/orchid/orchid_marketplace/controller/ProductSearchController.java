package com.orchid.orchid_marketplace.controller;

import java.math.BigDecimal;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orchid.orchid_marketplace.dto.ProductSearchDTO;
import com.orchid.orchid_marketplace.service.ProductSearchService;

@RestController
@Profile("azure")
@RequestMapping("/api/products")
@Validated
public class ProductSearchController {

    private final ProductSearchService searchService;

    public ProductSearchController(ProductSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductSearchDTO>> search(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(required = false) Double minRating,
        @RequestParam(required = false) Boolean inStock,
        @RequestParam(defaultValue = "relevance") String sortBy,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Sort sort;
        switch (sortBy.toLowerCase()) {
            case "price_asc":
                sort = Sort.by("price").ascending();
                break;
            case "price_desc":
                sort = Sort.by("price").descending();
                break;
            case "rating":
                sort = Sort.by("averageRating").descending();
                break;
            case "newest":
                sort = Sort.by("createdAt").descending();
                break;
            case "relevance":
            default:
                sort = Sort.unsorted();
                break;
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductSearchDTO> results = searchService.searchProducts(
            q, category, minPrice, maxPrice, minRating, inStock, sortBy, pageable
        );

        return ResponseEntity.ok(results);
    }
}

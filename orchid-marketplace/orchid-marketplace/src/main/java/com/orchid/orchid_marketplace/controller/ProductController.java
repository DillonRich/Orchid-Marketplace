package com.orchid.orchid_marketplace.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
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

import com.orchid.orchid_marketplace.dto.ProductRequest;
import com.orchid.orchid_marketplace.dto.ProductResponse;
import com.orchid.orchid_marketplace.dto.ProductSearchRequest;
import com.orchid.orchid_marketplace.mapper.ProductMapper;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@Profile("!cosmos")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductResponse> listAll() {
        return productService.getAllProducts().stream().map(ProductMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        return productService.getProductById(id).map(ProductMapper::toResponse).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search-basic")
    public List<ProductResponse> search(@RequestParam String q) {
        return productService.searchProducts(q).stream().map(ProductMapper::toResponse).toList();
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchAdvanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false, defaultValue = "newest") String sortBy,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        
        ProductSearchRequest request = new ProductSearchRequest(
            keyword, categoryId, minPrice, maxPrice, minRating, sortBy, page, size
        );
        
        Page<Product> products = productService.searchProductsAdvanced(request);
        Page<ProductResponse> response = products.map(ProductMapper::toResponse);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-store/{storeId}")
    public List<ProductResponse> byStore(@PathVariable UUID storeId) {
        return productService.getProductsByStoreId(storeId).stream().map(ProductMapper::toResponse).toList();
    }

    @GetMapping("/by-category/{categoryId}")
    public List<ProductResponse> byCategory(@PathVariable UUID categoryId) {
        return productService.getProductsByCategoryId(categoryId).stream().map(ProductMapper::toResponse).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ProductResponse create(@Valid @RequestBody ProductRequest req) {
        Product p = ProductMapper.toEntity(req);
        Product created = productService.createProduct(p);
        return ProductMapper.toResponse(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductRequest req) {
        Product detail = ProductMapper.toEntity(req);
        Product updated = productService.updateProduct(id, detail);
        return ProductMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

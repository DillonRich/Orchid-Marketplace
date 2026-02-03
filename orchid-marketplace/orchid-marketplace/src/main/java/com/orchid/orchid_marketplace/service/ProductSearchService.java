package com.orchid.orchid_marketplace.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.orchid.orchid_marketplace.dto.ProductSearchDTO;
import com.orchid.orchid_marketplace.repository.ProductRepository;

@Service
@Profile("!cosmos")
public class ProductSearchService {

    private final ProductRepository productRepository;

    public ProductSearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductSearchDTO> searchProducts(
        String query,
        String category,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Double minRating,
        Boolean inStock,
        String sortBy,
        Pageable pageable
    ) {
        // Simplified search - return all products with mapping
        // Note: Custom repository methods (searchProductsFullFilter, etc.) need to be added to ProductRepository
        return productRepository.findAll(pageable).map(this::mapProductToDTO);
    }

    private ProductSearchDTO mapProductToDTO(com.orchid.orchid_marketplace.model.Product product) {
        String categoryName = product.getCategory() != null ? product.getCategory().getName() : "Uncategorized";
        return new ProductSearchDTO(
            product.getId(),
            product.getTitle(),
            product.getDescription(),
            product.getPrice(),
            categoryName,
            0.0, // Would come from ProductReview aggregation
            0L,  // Would come from ProductReview count
            product.getStockQuantity(),
            product.getStore() != null ? product.getStore().getStoreName() : null
        );
    }
}

package com.orchid.orchid_marketplace.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.dto.ProductSearchRequest;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.repository.ProductRepository;

@Service
@Profile("!cosmos")
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerLedgerService sellerLedgerService;
    
    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    // Get product by ID
    public Optional<Product> getProductById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return productRepository.findById(id);
    }
    
    // Get products by category ID
    public List<Product> getProductsByCategoryId(UUID categoryId) {
        Objects.requireNonNull(categoryId, "categoryId must not be null");
        return productRepository.findByCategoryId(categoryId);
    }
    
    // Get products by store ID
    public List<Product> getProductsByStoreId(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return productRepository.findByStoreIdAndIsActiveTrue(storeId);
    }
    
    // Search products by keyword
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }
    
    // Get products by price range
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    // Get products with stock available
    public List<Product> getProductsInStock() {
        return productRepository.findInStockProducts();
    }
    
    // Get products by tag
    public List<Product> getProductsByTag(String tag) {
        return productRepository.findByTag(tag);
    }
    
    // Get products by condition
    public List<Product> getProductsByCondition(Product.ProductCondition condition) {
        return productRepository.findByCondition(condition);
    }
    
    // Create a new product
    public Product createProduct(Product product) {
        Objects.requireNonNull(product, "product must not be null");
        @SuppressWarnings("null")
        Product saved = productRepository.save(product);
        sellerLedgerService.accrueListingFeeForProduct(saved);
        return saved;
    }
    
    // Update a product
    public Product updateProduct(UUID id, Product productDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(productDetails, "productDetails must not be null");
        return productRepository.findById(id)
            .map(existingProduct -> {
                if (productDetails.getTitle() != null) {
                    existingProduct.setTitle(productDetails.getTitle());
                }
                
                if (productDetails.getDescription() != null) {
                    existingProduct.setDescription(productDetails.getDescription());
                }
                
                if (productDetails.getPrice() != null) {
                    existingProduct.setPrice(productDetails.getPrice());
                }
                
                if (productDetails.getStockQuantity() != null) {
                    existingProduct.setStockQuantity(productDetails.getStockQuantity());
                }
                
                if (productDetails.getCategory() != null) {
                    existingProduct.setCategory(productDetails.getCategory());
                }
                
                if (productDetails.getStore() != null) {
                    existingProduct.setStore(productDetails.getStore());
                }
                
                if (productDetails.getCondition() != null) {
                    existingProduct.setCondition(productDetails.getCondition());
                }

                @SuppressWarnings("null")
                Product saved = productRepository.save(existingProduct);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }
    
    // Soft delete a product
    public void deleteProduct(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        productRepository.findById(id)
            .ifPresentOrElse(
                product -> {
                    product.softDelete();
                    productRepository.save(product);
                },
                () -> { throw new RuntimeException("Product not found with ID: " + id); }
            );
    }
    
    // Update product stock
    public Product updateProductStock(UUID productId, Integer quantityChange) {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(quantityChange, "quantityChange must not be null");
        return productRepository.findById(productId)
            .map(product -> {
                Integer stockQuantity = product.getStockQuantity();
                int current = stockQuantity == null ? 0 : stockQuantity;
                int newStock = current + quantityChange;
                if (newStock < 0) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getTitle());
                }
                product.setStockQuantity(newStock);
                @SuppressWarnings("null")
                Product saved = productRepository.save(product);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
    }
    
    // Check if product is in stock
    public boolean isProductInStock(UUID productId, Integer requestedQuantity) {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(requestedQuantity, "requestedQuantity must not be null");
        return productRepository.findById(productId)
            .map(product -> {
                Integer stockQuantity = product.getStockQuantity();
                int current = stockQuantity == null ? 0 : stockQuantity;
                return current >= requestedQuantity;
            })
            .orElse(false);
    }
    
    // Get total number of products
    public long countProducts() {
        return productRepository.count();
    }
    
    // Get total number of products in store
    public long countProductsByStore(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return productRepository.countByStoreIdAndIsActiveTrue(storeId);
    }
    
    // Get products with low stock
    public List<Product> getProductsWithLowStock(Integer threshold) {
        return productRepository.findLowStockProducts(threshold);
    }
    
    // Advanced search with filters
    public Page<Product> searchProductsAdvanced(ProductSearchRequest request) {
        // Start with all active products
        List<Product> allProducts = productRepository.findAll().stream()
            .filter(Product::getIsActive)
            .collect(Collectors.toList());
        
        // Apply keyword filter
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String keyword = request.getKeyword().toLowerCase();
            allProducts = allProducts.stream()
                .filter(p -> 
                    (p.getTitle() != null && p.getTitle().toLowerCase().contains(keyword)) ||
                    (p.getDescription() != null && p.getDescription().toLowerCase().contains(keyword))
                )
                .collect(Collectors.toList());
        }
        
        // Apply category filter
        if (request.getCategoryId() != null) {
            allProducts = allProducts.stream()
                .filter(p -> p.getCategory() != null && 
                    request.getCategoryId().equals(p.getCategory().getId()))
                .collect(Collectors.toList());
        }
        
        // Apply price range filter
        if (request.getMinPrice() != null) {
            allProducts = allProducts.stream()
                .filter(p -> p.getPrice() != null && 
                    p.getPrice().compareTo(request.getMinPrice()) >= 0)
                .collect(Collectors.toList());
        }
        if (request.getMaxPrice() != null) {
            allProducts = allProducts.stream()
                .filter(p -> p.getPrice() != null && 
                    p.getPrice().compareTo(request.getMaxPrice()) <= 0)
                .collect(Collectors.toList());
        }
        
        // Apply rating filter
        if (request.getMinRating() != null) {
            allProducts = allProducts.stream()
                .filter(p -> p.calculateAverageRating().doubleValue() >= request.getMinRating())
                .collect(Collectors.toList());
        }
        
        // Apply sorting
        Comparator<Product> comparator = null;
        String sortBy = request.getSortBy();
        
        if ("price_asc".equals(sortBy)) {
            comparator = Comparator.comparing(
                p -> p.getPrice() != null ? p.getPrice() : BigDecimal.ZERO);
        } else if ("price_desc".equals(sortBy)) {
            comparator = Comparator.comparing(
                (Product p) -> p.getPrice() != null ? p.getPrice() : BigDecimal.ZERO).reversed();
        } else if ("rating".equals(sortBy)) {
            comparator = Comparator.comparing(Product::calculateAverageRating).reversed();
        } else if ("popular".equals(sortBy)) {
            comparator = Comparator.comparingInt(
                (Product p) -> p.getReviews() != null ? p.getReviews().size() : 0).reversed();
        } else if ("newest".equals(sortBy)) {
            comparator = Comparator.comparing(
                (Product p) -> p.getCreatedAt() != null ? p.getCreatedAt() : java.time.LocalDateTime.MIN).reversed();
        }
        
        if (comparator != null) {
            allProducts = allProducts.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        }
        
        // Apply pagination
        int page = request.getPage();
        int size = request.getSize();
        int total = allProducts.size();
        int start = page * size;
        int end = Math.min(start + size, total);
        
        List<Product> pageContent = start < total ? 
            allProducts.subList(start, end) : List.of();
        
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(pageContent, pageable, total);
    }
}
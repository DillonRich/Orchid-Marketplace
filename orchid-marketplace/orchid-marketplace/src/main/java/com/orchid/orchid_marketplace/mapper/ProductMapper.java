package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.ProductRequest;
import com.orchid.orchid_marketplace.dto.ProductResponse;
import com.orchid.orchid_marketplace.model.Category;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.Store;
import com.orchid.orchid_marketplace.util.InputSanitizer;

public final class ProductMapper {
    private ProductMapper() {}

    public static Product toEntity(ProductRequest req) {
        if (req == null) return null;
        Product p = new Product();
        p.setTitle(InputSanitizer.sanitize(req.getName()));
        p.setDescription(InputSanitizer.sanitize(req.getDescription()));
        p.setPrice(req.getPrice() == null ? null : java.math.BigDecimal.valueOf(req.getPrice()));
        p.setStockQuantity(req.getStock());
        if (req.getCategoryId() != null) {
            Category c = new Category();
            c.setId(req.getCategoryId());
            p.setCategory(c);
        }
        if (req.getStoreId() != null) {
            Store s = new Store();
            s.setId(req.getStoreId());
            p.setStore(s);
        }
        return p;
    }

    public static ProductResponse toResponse(Product p) {
        if (p == null) return null;
        ProductResponse r = new ProductResponse();
        r.setId(p.getId());
        r.setTitle(p.getTitle());
        r.setDescription(p.getDescription());
        r.setPrice(p.getPrice() != null ? p.getPrice().doubleValue() : null);
        r.setStock(p.getStockQuantity());
        r.setCategoryId(p.getCategory() != null ? p.getCategory().getId() : null);
        r.setStoreId(p.getStore() != null ? p.getStore().getId() : null);
        return r;
    }
}

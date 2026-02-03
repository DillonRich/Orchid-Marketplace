package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.ProductImageRequest;
import com.orchid.orchid_marketplace.dto.ProductImageResponse;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.ProductImage;
import com.orchid.orchid_marketplace.util.InputSanitizer;

public final class ProductImageMapper {
    private ProductImageMapper() {}

    public static ProductImage toEntity(ProductImageRequest req) {
        if (req == null) return null;
        ProductImage pi = new ProductImage();
        pi.setImageUrl(InputSanitizer.sanitize(req.getImageUrl()));
        if (req.getProductId() != null) {
            Product p = new Product();
            p.setId(req.getProductId());
            pi.setProduct(p);
        }
        return pi;
    }

    public static ProductImageResponse toResponse(ProductImage pi) {
        if (pi == null) return null;
        ProductImageResponse r = new ProductImageResponse();
        r.setId(pi.getId());
        r.setImageUrl(pi.getImageUrl());
        r.setProductId(pi.getProduct() != null ? pi.getProduct().getId() : null);
        return r;
    }
}

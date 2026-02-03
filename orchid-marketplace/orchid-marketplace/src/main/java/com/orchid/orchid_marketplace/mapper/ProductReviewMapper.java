package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.ProductReviewRequest;
import com.orchid.orchid_marketplace.dto.ProductReviewResponse;
import com.orchid.orchid_marketplace.model.Product;
import com.orchid.orchid_marketplace.model.ProductReview;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.util.InputSanitizer;

public final class ProductReviewMapper {
    private ProductReviewMapper() {}

    public static ProductReview toEntity(ProductReviewRequest req) {
        if (req == null) return null;
        ProductReview pr = new ProductReview();
        Product p = new Product(); p.setId(req.getProductId()); pr.setProduct(p);
        User u = new User(); u.setId(req.getUserId()); pr.setUser(u);
        pr.setRating(req.getRating());
        pr.setComment(InputSanitizer.sanitize(req.getComment()));
        return pr;
    }

    public static ProductReviewResponse toResponse(ProductReview pr) {
        if (pr == null) return null;
        ProductReviewResponse r = new ProductReviewResponse();
        r.setId(pr.getId());
        r.setProductId(pr.getProduct() != null ? pr.getProduct().getId() : null);
        r.setUserId(pr.getUser() != null ? pr.getUser().getId() : null);
        r.setRating(pr.getRating());
        r.setComment(pr.getComment());
        r.setCreatedAt(pr.getCreatedAt());
        return r;
    }
}

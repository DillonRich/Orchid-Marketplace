package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.OrderItemRequest;
import com.orchid.orchid_marketplace.dto.OrderItemResponse;
import com.orchid.orchid_marketplace.model.OrderItem;
import com.orchid.orchid_marketplace.model.Product;

public final class OrderItemMapper {
    private OrderItemMapper() {}

    public static OrderItem toEntity(OrderItemRequest req) {
        if (req == null) return null;
        OrderItem oi = new OrderItem();
        Product p = new Product();
        p.setId(req.getProductId());
        oi.setProduct(p);
        oi.setQuantity(req.getQuantity());
        return oi;
    }

    public static OrderItemResponse toResponse(OrderItem oi) {
        if (oi == null) return null;
        OrderItemResponse r = new OrderItemResponse();
        r.setId(oi.getId());
        r.setProductId(oi.getProduct() != null ? oi.getProduct().getId() : null);
        r.setQuantity(oi.getQuantity());
        return r;
    }
}

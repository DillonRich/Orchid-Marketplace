package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.OrderRequest;
import com.orchid.orchid_marketplace.dto.OrderResponse;
import com.orchid.orchid_marketplace.model.Order;
import com.orchid.orchid_marketplace.model.User;

public final class OrderMapper {
    private OrderMapper() {}

    public static Order toEntity(OrderRequest req) {
        if (req == null) return null;
        Order o = new Order();
        User buyer = new User();
        buyer.setId(req.getBuyerId());
        o.setBuyer(buyer);
        return o;
    }

    public static OrderResponse toResponse(Order o) {
        if (o == null) return null;
        OrderResponse r = new OrderResponse();
        r.setId(o.getId());
        r.setOrderNumber(o.getOrderNumber());
        r.setBuyerId(o.getBuyer() != null ? o.getBuyer().getId() : null);
        r.setCreatedAt(o.getCreatedAt());
        return r;
    }
}

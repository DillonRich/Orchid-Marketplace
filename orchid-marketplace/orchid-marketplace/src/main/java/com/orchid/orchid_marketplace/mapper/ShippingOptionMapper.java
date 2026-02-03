package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.ShippingOptionRequest;
import com.orchid.orchid_marketplace.dto.ShippingOptionResponse;
import com.orchid.orchid_marketplace.model.ShippingOption;

public final class ShippingOptionMapper {
    private ShippingOptionMapper() {}

    public static ShippingOption toEntity(ShippingOptionRequest req) {
        if (req == null) return null;
        ShippingOption s = new ShippingOption();
        s.setOptionName(req.getName());
        s.setShippingCost(req.getPrice() == null ? null : java.math.BigDecimal.valueOf(req.getPrice()));
        return s;
    }

    public static ShippingOptionResponse toResponse(ShippingOption s) {
        if (s == null) return null;
        ShippingOptionResponse r = new ShippingOptionResponse();
        r.setId(s.getId());
        r.setName(s.getOptionName());
        r.setPrice(s.getShippingCost() != null ? s.getShippingCost().doubleValue() : null);
        return r;
    }
}

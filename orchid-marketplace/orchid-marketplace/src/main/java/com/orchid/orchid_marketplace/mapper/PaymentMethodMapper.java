package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.PaymentMethodRequest;
import com.orchid.orchid_marketplace.dto.PaymentMethodResponse;
import com.orchid.orchid_marketplace.model.PaymentMethod;
import com.orchid.orchid_marketplace.model.User;

public final class PaymentMethodMapper {
    private PaymentMethodMapper() {}

    public static PaymentMethod toEntity(PaymentMethodRequest req) {
        if (req == null) return null;
        PaymentMethod pm = new PaymentMethod();
        User u = new User(); u.setId(req.getUserId()); pm.setUser(u);
        pm.setBrand(req.getBrand());
        pm.setLast4(req.getLast4());
        pm.setExpMonth(req.getExpMonth());
        pm.setExpYear(req.getExpYear());
        return pm;
    }

    public static PaymentMethodResponse toResponse(PaymentMethod pm) {
        if (pm == null) return null;
        PaymentMethodResponse r = new PaymentMethodResponse();
        r.setId(pm.getId());
        r.setUserId(pm.getUser() != null ? pm.getUser().getId() : null);
        r.setBrand(pm.getBrand());
        r.setLast4(pm.getLast4());
        r.setExpMonth(pm.getExpMonth());
        r.setExpYear(pm.getExpYear());
        return r;
    }
}

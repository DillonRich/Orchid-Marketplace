package com.orchid.orchid_marketplace.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.orchid.orchid_marketplace.dto.PaymentMethodRequest;
import com.orchid.orchid_marketplace.dto.PaymentMethodResponse;
import com.orchid.orchid_marketplace.model.PaymentMethod;
import com.orchid.orchid_marketplace.model.User;

class PaymentMethodMapperTest {

    @Test
    void toEntity_mapsFields() {
        PaymentMethodRequest req = new PaymentMethodRequest();
        UUID userId = UUID.randomUUID();
        req.setUserId(userId);
        req.setBrand("VISA");
        req.setLast4("4242");
        req.setExpMonth(12);
        req.setExpYear(2030);

        PaymentMethod entity = PaymentMethodMapper.toEntity(req);

        assertEquals(userId, entity.getUser().getId());
        assertEquals("VISA", entity.getBrand());
        assertEquals("4242", entity.getLast4());
        assertEquals(12, entity.getExpMonth());
        assertEquals(2030, entity.getExpYear());
    }

    @Test
    void toResponse_mapsNullSafely() {
        assertNull(PaymentMethodMapper.toResponse(null));

        PaymentMethod pm = new PaymentMethod();
        pm.setId(UUID.randomUUID());
        User u = new User();
        u.setId(UUID.randomUUID());
        pm.setUser(u);
        pm.setBrand("MC");
        pm.setLast4("1111");
        pm.setExpMonth(1);
        pm.setExpYear(2028);

        PaymentMethodResponse resp = PaymentMethodMapper.toResponse(pm);

        assertEquals(pm.getId(), resp.getId());
        assertEquals(u.getId(), resp.getUserId());
        assertEquals("MC", resp.getBrand());
        assertEquals("1111", resp.getLast4());
        assertEquals(1, resp.getExpMonth());
        assertEquals(2028, resp.getExpYear());
    }
}

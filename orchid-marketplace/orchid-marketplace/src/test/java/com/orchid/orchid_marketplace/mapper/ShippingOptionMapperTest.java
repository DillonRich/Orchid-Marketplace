package com.orchid.orchid_marketplace.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.orchid.orchid_marketplace.dto.ShippingOptionRequest;
import com.orchid.orchid_marketplace.dto.ShippingOptionResponse;
import com.orchid.orchid_marketplace.model.ShippingOption;

class ShippingOptionMapperTest {

    @Test
    void toEntity_mapsPriceAndName() {
        ShippingOptionRequest req = new ShippingOptionRequest();
        req.setName("Express");
        req.setPrice(9.99);

        ShippingOption entity = ShippingOptionMapper.toEntity(req);

        assertEquals("Express", entity.getOptionName());
        assertEquals(new BigDecimal("9.99"), entity.getShippingCost());
    }

    @Test
    void toResponse_handlesNulls() {
        assertNull(ShippingOptionMapper.toResponse(null));

        ShippingOption option = new ShippingOption();
        option.setId(UUID.randomUUID());
        option.setOptionName("Standard");
        option.setShippingCost(new BigDecimal("4.50"));

        ShippingOptionResponse resp = ShippingOptionMapper.toResponse(option);

        assertEquals(option.getId(), resp.getId());
        assertEquals("Standard", resp.getName());
        assertEquals(4.50, resp.getPrice());
    }
}

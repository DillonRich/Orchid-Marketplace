package com.orchid.orchid_marketplace.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.orchid.orchid_marketplace.dto.AddressRequest;
import com.orchid.orchid_marketplace.dto.AddressResponse;
import com.orchid.orchid_marketplace.model.Address;
import com.orchid.orchid_marketplace.model.User;

class AddressMapperTest {

    @Test
    void toEntity_mapsAllFields_andIgnoresBadEnum() {
        AddressRequest req = new AddressRequest();
        UUID userId = UUID.randomUUID();
        req.setUserId(userId);
        req.setStreetAddress("123 Main");
        req.setCity("Austin");
        req.setState("TX");
        req.setCountry("US");
        req.setPostalCode("73301");
        req.setRecipientName("Ada Lovelace");
        req.setPhoneNumber("555-1234");
        req.setAddressType("INVALID_ENUM"); // should be ignored
        req.setIsDefault(Boolean.TRUE);

        Address entity = AddressMapper.toEntity(req);

        assertEquals(userId, entity.getUser().getId());
        assertEquals("123 Main", entity.getStreetAddress());
        assertEquals("Austin", entity.getCity());
        assertEquals("TX", entity.getState());
        assertEquals("US", entity.getCountry());
        assertEquals("73301", entity.getPostalCode());
        assertEquals("Ada Lovelace", entity.getRecipientName());
        assertEquals("555-1234", entity.getPhoneNumber());
        assertNull(entity.getAddressType(), "Invalid enum string should be ignored");
        assertEquals(Boolean.TRUE, entity.getIsDefault());
    }

    @Test
    void toResponse_mapsNullSafely() {
        AddressResponse nullResponse = AddressMapper.toResponse(null);
        assertNull(nullResponse);

        Address entity = new Address();
        entity.setId(UUID.randomUUID());
        User user = new User();
        user.setId(UUID.randomUUID());
        entity.setUser(user);
        entity.setStreetAddress("456 Side");
        entity.setCity("Dallas");
        entity.setState("TX");
        entity.setCountry("US");
        entity.setPostalCode("75001");
        entity.setRecipientName("Grace Hopper");
        entity.setPhoneNumber("555-5678");
        entity.setAddressType(Address.AddressType.BILLING);
        entity.setIsDefault(Boolean.FALSE);

        AddressResponse response = AddressMapper.toResponse(entity);

        assertEquals(entity.getId(), response.getId());
        assertEquals(user.getId(), response.getUserId());
        assertEquals("456 Side", response.getStreetAddress());
        assertEquals("Dallas", response.getCity());
        assertEquals("TX", response.getState());
        assertEquals("US", response.getCountry());
        assertEquals("75001", response.getPostalCode());
        assertEquals("Grace Hopper", response.getRecipientName());
        assertEquals("555-5678", response.getPhoneNumber());
        assertEquals("BILLING", response.getAddressType());
        assertEquals(Boolean.FALSE, response.getIsDefault());
    }
}

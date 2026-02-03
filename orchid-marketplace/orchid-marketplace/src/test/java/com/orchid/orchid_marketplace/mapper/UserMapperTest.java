package com.orchid.orchid_marketplace.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.orchid.orchid_marketplace.dto.UserRequest;
import com.orchid.orchid_marketplace.dto.UserResponse;
import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.User;

class UserMapperTest {

    @Test
    void toEntity_sanitizesAndMaps() {
        UserRequest req = new UserRequest();
        req.setEmail(" user@example.com ");
        req.setFullName(" Ada ");
        req.setPhoneNumber(" 123 ");
        req.setRole("seller");

        User entity = UserMapper.toEntity(req);

        assertEquals("user@example.com", entity.getEmail());
        assertEquals("Ada", entity.getFullName());
        assertEquals("123", entity.getPhoneNumber());
        assertEquals(Role.SELLER, entity.getRole());
    }

    @Test
    void toResponse_mapsNullSafely() {
        assertNull(UserMapper.toResponse(null));

        User user = new User();
        user.setId(java.util.UUID.randomUUID());
        user.setEmail("a@b.com");
        user.setFullName("Bob");
        user.setPhoneNumber("999");
        user.setRole(Role.CUSTOMER);
        user.setIsActive(Boolean.TRUE);

        UserResponse resp = UserMapper.toResponse(user);

        assertEquals(user.getId(), resp.getId());
        assertEquals("a@b.com", resp.getEmail());
        assertEquals("Bob", resp.getFullName());
        assertEquals("999", resp.getPhoneNumber());
        assertEquals("CUSTOMER", resp.getRole());
        assertEquals(Boolean.TRUE, resp.getIsActive());
    }
}

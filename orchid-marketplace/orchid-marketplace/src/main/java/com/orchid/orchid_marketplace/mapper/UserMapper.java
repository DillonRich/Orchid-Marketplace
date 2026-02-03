package com.orchid.orchid_marketplace.mapper;

import com.orchid.orchid_marketplace.dto.UserRequest;
import com.orchid.orchid_marketplace.dto.UserResponse;
import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.util.InputSanitizer;

public final class UserMapper {
    private UserMapper() {}

    public static User toEntity(UserRequest req) {
        if (req == null) return null;
        User u = new User();
        u.setEmail(InputSanitizer.sanitize(req.getEmail()));
        u.setFullName(InputSanitizer.sanitize(req.getFullName()));
        u.setPhoneNumber(InputSanitizer.sanitize(req.getPhoneNumber()));
        
        // Set role - default to CUSTOMER if not provided or invalid
        if (req.getRole() != null && !req.getRole().isBlank()) {
            try { 
                u.setRole(Role.valueOf(req.getRole().toUpperCase())); 
            } catch (Exception e) { 
                u.setRole(Role.CUSTOMER); // Default to CUSTOMER if invalid role
            }
        } else {
            u.setRole(Role.CUSTOMER); // Default to CUSTOMER if no role provided
        }
        
        return u;
    }

    public static UserResponse toResponse(User u) {
        if (u == null) return null;
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setEmail(u.getEmail());
        r.setFullName(u.getFullName());
        r.setPhoneNumber(u.getPhoneNumber());
        r.setRole(u.getRole() != null ? u.getRole().name() : null);
        r.setIsActive(u.getIsActive());
        return r;
    }
}

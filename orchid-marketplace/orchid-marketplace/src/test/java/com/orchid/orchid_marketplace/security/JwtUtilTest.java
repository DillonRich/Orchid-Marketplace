package com.orchid.orchid_marketplace.security;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void generate_and_validate_token_with_roles() {
        Collection<GrantedAuthority> authorities = java.util.List.of(new SimpleGrantedAuthority("ROLE_SELLER"));
        User user = new User("test@example.com", "password", authorities);

        String token = jwtUtil.generateToken(user);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals("test@example.com", jwtUtil.extractUsername(token));
        java.util.List<String> roles = jwtUtil.extractRoles(token);
        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_SELLER"));
    }
}

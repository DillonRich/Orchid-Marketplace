package com.orchid.orchid_marketplace.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {
    
    private User user;
    private UUID userId;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
    }
    
    @Test
    void testUserIdGetterSetter() {
        assertEquals(userId, user.getId());
    }
    
    @Test
    void testEmailGetterSetter() {
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }
    
    @Test
    void testFullNameGetterSetter() {
        user.setFullName("John Doe");
        assertEquals("John Doe", user.getFullName());
    }
    
    @Test
    void testPhoneNumberGetterSetter() {
        user.setPhoneNumber("555-1234");
        assertEquals("555-1234", user.getPhoneNumber());
    }
    
    @Test
    void testPasswordHashGetterSetter() {
        user.setPasswordHash("hashed_password");
        assertEquals("hashed_password", user.getPasswordHash());
    }
    
    @Test
    void testRoleGetterSetter() {
        user.setRole(Role.CUSTOMER);
        assertEquals(Role.CUSTOMER, user.getRole());
    }
    
    @Test
    void testEntraIdGetterSetter() {
        user.setEntraId("entra-123");
        assertEquals("entra-123", user.getEntraId());
    }
    
    @Test
    void testStripeCustomerIdGetterSetter() {
        user.setStripeCustomerId("cus_123");
        assertEquals("cus_123", user.getStripeCustomerId());
    }
    
    @Test
    void testIsEmailVerifiedGetterSetter() {
        user.setIsEmailVerified(true);
        assertTrue(user.getIsEmailVerified());
    }
    
    @Test
    void testIsPhoneVerifiedGetterSetter() {
        user.setIsPhoneVerified(false);
        assertFalse(user.getIsPhoneVerified());
    }
    
    @Test
    void testUserConstructorWithParameters() {
        User newUser = new User("email@test.com", "Jane Smith", Role.SELLER);
        assertEquals("email@test.com", newUser.getEmail());
        assertEquals("Jane Smith", newUser.getFullName());
        assertEquals(Role.SELLER, newUser.getRole());
    }
    
    @Test
    void testUserActiveByDefault() {
        User newUser = new User("email@test.com", "Test User", Role.CUSTOMER);
        assertTrue(newUser.getIsActive());
    }
    
    @Test
    void testMultipleRoleTypes() {
        user.setRole(Role.ADMIN);
        assertEquals(Role.ADMIN, user.getRole());
        
        user.setRole(Role.SUPPORT_AGENT);
        assertEquals(Role.SUPPORT_AGENT, user.getRole());
    }
}

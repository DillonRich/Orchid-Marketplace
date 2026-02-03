package com.orchid.orchid_marketplace.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddressTest {
    
    private Address address;
    private UUID addressId;
    
    @BeforeEach
    void setUp() {
        addressId = UUID.randomUUID();
        address = new Address();
        address.setId(addressId);
    }
    
    @Test
    void testAddressIdGetterSetter() {
        assertEquals(addressId, address.getId());
    }
    
    @Test
    void testStreetAddressGetterSetter() {
        address.setStreetAddress("123 Main St");
        assertEquals("123 Main St", address.getStreetAddress());
    }
    
    @Test
    void testCityGetterSetter() {
        address.setCity("Seattle");
        assertEquals("Seattle", address.getCity());
    }
    
    @Test
    void testStateGetterSetter() {
        address.setState("WA");
        assertEquals("WA", address.getState());
    }
    
    @Test
    void testCountryGetterSetter() {
        address.setCountry("USA");
        assertEquals("USA", address.getCountry());
    }
    
    @Test
    void testPostalCodeGetterSetter() {
        address.setPostalCode("98101");
        assertEquals("98101", address.getPostalCode());
    }
    
    @Test
    void testRecipientNameGetterSetter() {
        address.setRecipientName("John Doe");
        assertEquals("John Doe", address.getRecipientName());
    }
    
    @Test
    void testPhoneNumberGetterSetter() {
        address.setPhoneNumber("555-1234");
        assertEquals("555-1234", address.getPhoneNumber());
    }
    
    @Test
    void testAddressTypeGetterSetter() {
        address.setAddressType(Address.AddressType.SHIPPING);
        assertEquals(Address.AddressType.SHIPPING, address.getAddressType());
    }
    
    @Test
    void testIsDefaultGetterSetter() {
        address.setIsDefault(true);
        assertTrue(address.getIsDefault());
    }
    
    @Test
    void testAllAddressTypes() {
        address.setAddressType(Address.AddressType.BILLING);
        assertEquals(Address.AddressType.BILLING, address.getAddressType());
        
        address.setAddressType(Address.AddressType.BOTH);
        assertEquals(Address.AddressType.BOTH, address.getAddressType());
    }
    
    @Test
    void testAddressConstructorWithParameters() {
        User user = new User();
        Address addr = new Address(user, "456 Oak Ave", "Portland", "OR", 
                                   "USA", "97201", Address.AddressType.SHIPPING);
        assertEquals("456 Oak Ave", addr.getStreetAddress());
        assertEquals("Portland", addr.getCity());
        assertEquals("OR", addr.getState());
    }
}

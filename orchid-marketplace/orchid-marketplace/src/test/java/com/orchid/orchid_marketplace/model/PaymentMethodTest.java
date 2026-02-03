package com.orchid.orchid_marketplace.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentMethodTest {
    
    private PaymentMethod paymentMethod;
    private UUID paymentMethodId;
    
    @BeforeEach
    void setUp() {
        paymentMethodId = UUID.randomUUID();
        paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodId);
    }
    
    @Test
    void testPaymentMethodIdGetterSetter() {
        assertEquals(paymentMethodId, paymentMethod.getId());
    }
    
    @Test
    void testStripePaymentMethodIdGetterSetter() {
        paymentMethod.setStripePaymentMethodId("pm_123");
        assertEquals("pm_123", paymentMethod.getStripePaymentMethodId());
    }
    
    @Test
    void testBrandGetterSetter() {
        paymentMethod.setBrand("Visa");
        assertEquals("Visa", paymentMethod.getBrand());
    }
    
    @Test
    void testLast4GetterSetter() {
        paymentMethod.setLast4("4242");
        assertEquals("4242", paymentMethod.getLast4());
    }
    
    @Test
    void testExpMonthGetterSetter() {
        paymentMethod.setExpMonth(12);
        assertEquals(12, paymentMethod.getExpMonth());
    }
    
    @Test
    void testExpYearGetterSetter() {
        paymentMethod.setExpYear(2025);
        assertEquals(2025, paymentMethod.getExpYear());
    }
    
    @Test
    void testIsDefaultGetterSetter() {
        paymentMethod.setIsDefault(true);
        assertTrue(paymentMethod.getIsDefault());
    }
    
    @Test
    void testKeyVaultSecretIdGetterSetter() {
        paymentMethod.setKeyVaultSecretId("secret-123");
        assertEquals("secret-123", paymentMethod.getKeyVaultSecretId());
    }
    
    @Test
    void testPaymentMethodConstructorWithParameters() {
        User user = new User();
        PaymentMethod pm = new PaymentMethod(user, "pm_abc", "Mastercard", "5678", 6, 2026);
        assertEquals("pm_abc", pm.getStripePaymentMethodId());
        assertEquals("Mastercard", pm.getBrand());
        assertEquals("5678", pm.getLast4());
        assertEquals(6, pm.getExpMonth());
        assertEquals(2026, pm.getExpYear());
        assertFalse(pm.getIsDefault());
    }
    
    @Test
    void testValidExpiryMonthRange() {
        paymentMethod.setExpMonth(1);
        assertTrue(paymentMethod.getExpMonth() >= 1 && paymentMethod.getExpMonth() <= 12);
        
        paymentMethod.setExpMonth(12);
        assertTrue(paymentMethod.getExpMonth() >= 1 && paymentMethod.getExpMonth() <= 12);
    }
    
    @Test
    void testCardBrandVariations() {
        String[] brands = {"Visa", "Mastercard", "Amex", "Discover"};
        for (String brand : brands) {
            paymentMethod.setBrand(brand);
            assertEquals(brand, paymentMethod.getBrand());
        }
    }
}

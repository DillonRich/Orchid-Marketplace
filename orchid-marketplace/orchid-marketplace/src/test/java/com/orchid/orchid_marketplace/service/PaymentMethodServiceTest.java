package com.orchid.orchid_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orchid.orchid_marketplace.model.PaymentMethod;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.repository.PaymentMethodRepository;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceTest {
    
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    
    @InjectMocks
    private PaymentMethodService paymentMethodService;
    
    private PaymentMethod paymentMethod;
    private User user;
    private UUID paymentMethodId;
    private UUID userId;
    
    @BeforeEach
    void setUp() {
        paymentMethodId = UUID.randomUUID();
        userId = UUID.randomUUID();
        
        user = new User();
        user.setId(userId);
        user.setEmail("user@test.com");
        user.setFullName("Test User");
        user.setRole(Role.CUSTOMER);
        
        paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodId);
        paymentMethod.setUser(user);
        paymentMethod.setStripePaymentMethodId("pm_test123");
        paymentMethod.setBrand("Visa");
        paymentMethod.setLast4("4242");
        paymentMethod.setExpMonth(12);
        paymentMethod.setExpYear(2027);
        paymentMethod.setIsDefault(true);
        paymentMethod.setIsActive(true);
    }
    
    @Test
    void testGetAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = List.of(paymentMethod);
        when(paymentMethodRepository.findAll()).thenReturn(paymentMethods);
        
        List<PaymentMethod> result = paymentMethodService.getAllPaymentMethods();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(paymentMethodRepository, times(1)).findAll();
    }
    
    @Test
    void testGetPaymentMethodById() {
        when(paymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.of(paymentMethod));
        
        Optional<PaymentMethod> result = paymentMethodService.getPaymentMethodById(paymentMethodId);
        
        assertTrue(result.isPresent());
        assertEquals(paymentMethodId, result.get().getId());
        verify(paymentMethodRepository, times(1)).findById(paymentMethodId);
    }
    
    @Test
    void testGetPaymentMethodByIdNullThrowsException() {
        assertThrows(NullPointerException.class, () -> paymentMethodService.getPaymentMethodById(null));
    }
    
    @Test
    void testGetPaymentMethodsByUserId() {
        List<PaymentMethod> paymentMethods = List.of(paymentMethod);
        when(paymentMethodRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(paymentMethods);
        
        List<PaymentMethod> result = paymentMethodService.getPaymentMethodsByUserId(userId);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsActive());
        verify(paymentMethodRepository, times(1)).findByUserIdAndIsActiveTrue(userId);
    }
    
    @Test
    void testGetPaymentMethodsByUserIdNullThrowsException() {
        assertThrows(NullPointerException.class, () -> paymentMethodService.getPaymentMethodsByUserId(null));
    }
    
    @Test
    void testGetDefaultPaymentMethodForUser() {
        when(paymentMethodRepository.findDefaultByUser(userId)).thenReturn(Optional.of(paymentMethod));
        
        Optional<PaymentMethod> result = paymentMethodService.getDefaultPaymentMethodForUser(userId);
        
        assertTrue(result.isPresent());
        assertTrue(result.get().getIsDefault());
        verify(paymentMethodRepository, times(1)).findDefaultByUser(userId);
    }
    
    @Test
    void testGetDefaultPaymentMethodForUserNullThrowsException() {
        assertThrows(NullPointerException.class, () -> paymentMethodService.getDefaultPaymentMethodForUser(null));
    }
    
    @Test
    void testCreatePaymentMethod() {
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);
        
        PaymentMethod result = paymentMethodService.createPaymentMethod(paymentMethod);
        
        assertNotNull(result);
        assertEquals(paymentMethodId, result.getId());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }
    
    @Test
    void testCreatePaymentMethodNullThrowsException() {
        assertThrows(NullPointerException.class, () -> paymentMethodService.createPaymentMethod(null));
    }
    
    @Test
    void testUpdatePaymentMethod() {
        PaymentMethod updatedDetails = new PaymentMethod();
        updatedDetails.setBrand("Mastercard");
        updatedDetails.setExpMonth(6);
        updatedDetails.setExpYear(2028);
        updatedDetails.setLast4("5555");
        updatedDetails.setIsDefault(false);
        
        when(paymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.of(paymentMethod));
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);
        
        PaymentMethod result = paymentMethodService.updatePaymentMethod(paymentMethodId, updatedDetails);
        
        assertNotNull(result);
        verify(paymentMethodRepository, times(1)).findById(paymentMethodId);
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }
    
    @Test
    void testUpdatePaymentMethodNullIdThrowsException() {
        assertThrows(NullPointerException.class, () -> paymentMethodService.updatePaymentMethod(null, paymentMethod));
    }
    
    @Test
    void testUpdatePaymentMethodNullDetailsThrowsException() {
        assertThrows(NullPointerException.class, () -> paymentMethodService.updatePaymentMethod(paymentMethodId, null));
    }
    
    @Test
    void testUpdatePaymentMethodNotFound() {
        PaymentMethod updatedDetails = new PaymentMethod();
        when(paymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> paymentMethodService.updatePaymentMethod(paymentMethodId, updatedDetails));
    }
}

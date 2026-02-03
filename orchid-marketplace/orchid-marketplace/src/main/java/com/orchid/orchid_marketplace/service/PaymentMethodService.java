package com.orchid.orchid_marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.PaymentMethod;
import com.orchid.orchid_marketplace.repository.PaymentMethodRepository;

@Service
@Profile("!cosmos")
public class PaymentMethodService {
    
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    // Get all payment methods
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }
    
    // Get payment method by ID
    public Optional<PaymentMethod> getPaymentMethodById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return paymentMethodRepository.findById(id);
    }
    
    // Get payment methods by user ID
    public List<PaymentMethod> getPaymentMethodsByUserId(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return paymentMethodRepository.findByUserIdAndIsActiveTrue(userId);
    }
    
    // Get default payment method for user
    public Optional<PaymentMethod> getDefaultPaymentMethodForUser(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return paymentMethodRepository.findDefaultByUser(userId);
    }
    
    // Create a new payment method
    public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
        Objects.requireNonNull(paymentMethod, "paymentMethod must not be null");
        @SuppressWarnings("null")
        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return saved;
    }
    
    // Update a payment method
    public PaymentMethod updatePaymentMethod(UUID id, PaymentMethod paymentMethodDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(paymentMethodDetails, "paymentMethodDetails must not be null");
        return paymentMethodRepository.findById(id)
            .map(existingMethod -> {
                if (paymentMethodDetails.getBrand() != null) {
                    existingMethod.setBrand(paymentMethodDetails.getBrand());
                }
                
                if (paymentMethodDetails.getExpYear() != null) {
                    existingMethod.setExpYear(paymentMethodDetails.getExpYear());
                }
                
                if (paymentMethodDetails.getExpMonth() != null) {
                    existingMethod.setExpMonth(paymentMethodDetails.getExpMonth());
                }
                
                if (paymentMethodDetails.getLast4() != null) {
                    existingMethod.setLast4(paymentMethodDetails.getLast4());
                }
                
                if (paymentMethodDetails.getIsDefault() != null) {
                    existingMethod.setIsDefault(paymentMethodDetails.getIsDefault());
                }

                @SuppressWarnings("null")
                PaymentMethod saved = paymentMethodRepository.save(existingMethod);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Payment method not found with ID: " + id));
    }
    
    // Soft delete a payment method
    public void deletePaymentMethod(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        paymentMethodRepository.findById(id)
            .ifPresentOrElse(
                paymentMethod -> {
                    paymentMethod.softDelete();
                    paymentMethodRepository.save(paymentMethod);
                },
                () -> { throw new RuntimeException("Payment method not found with ID: " + id); }
            );
    }
    
    // Set a payment method as default for user
    public PaymentMethod setDefaultPaymentMethod(UUID userId, UUID paymentMethodId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(paymentMethodId, "paymentMethodId must not be null");
        List<PaymentMethod> userMethods = paymentMethodRepository.findByUserIdAndIsActiveTrue(userId);
        
        // Unset all defaults for this user
        for (PaymentMethod method : userMethods) {
            if (Boolean.TRUE.equals(method.getIsDefault())) {
                method.setIsDefault(false);
                paymentMethodRepository.save(method);
            }
        }
        
        // Set the specified method as default
        return paymentMethodRepository.findById(paymentMethodId)
            .map(method -> {
                method.setIsDefault(true);
                @SuppressWarnings("null")
                PaymentMethod saved = paymentMethodRepository.save(method);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Payment method not found with ID: " + paymentMethodId));
    }
    
    // Get valid payment methods for user
    public List<PaymentMethod> getValidPaymentMethods(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Integer currentYear = java.time.Year.now().getValue();
        return paymentMethodRepository.findValidPaymentMethodsByUser(userId, currentYear);
    }
    
    // Find by Stripe Payment Method ID
    public Optional<PaymentMethod> findByStripePaymentMethodId(String stripePaymentMethodId) {
        return paymentMethodRepository.findByStripePaymentMethodId(stripePaymentMethodId);
    }
}
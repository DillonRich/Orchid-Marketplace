package com.orchid.orchid_marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.PaymentMethod;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    
    List<PaymentMethod> findByUserId(UUID userId);
    List<PaymentMethod> findByUserIdAndIsActiveTrue(UUID userId);
    
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.user.id = :userId AND pm.isDefault = true AND pm.isActive = true")
    Optional<PaymentMethod> findDefaultByUser(@Param("userId") UUID userId);
    
    Optional<PaymentMethod> findByStripePaymentMethodId(String stripePaymentMethodId);
    
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.user.id = :userId AND pm.brand = :brand AND pm.isActive = true")
    List<PaymentMethod> findByUserAndBrand(@Param("userId") UUID userId, @Param("brand") String brand);
    
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.keyVaultSecretId IS NOT NULL AND pm.isActive = true")
    List<PaymentMethod> findPaymentMethodsWithKeyVault();
    
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.user.id = :userId AND pm.expYear >= :currentYear AND pm.isActive = true ORDER BY pm.isDefault DESC, pm.createdAt DESC")
    List<PaymentMethod> findValidPaymentMethodsByUser(@Param("userId") UUID userId, @Param("currentYear") Integer currentYear);
    
    boolean existsByUserIdAndStripePaymentMethodId(UUID userId, String stripePaymentMethodId);
}
package com.orchid.orchid_marketplace.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchid.orchid_marketplace.model.ShippingOption;

@Repository
public interface ShippingOptionRepository extends JpaRepository<ShippingOption, UUID> {
    // Find shipping options for a specific product
    List<ShippingOption> findByProductId(UUID productId);

    // Find active shipping options for a given product
    List<ShippingOption> findByProductIdAndIsActiveTrue(UUID productId);

    // Lookup by displayed option name
    Optional<ShippingOption> findByOptionName(String optionName);

    // Active options for a store (joins product -> store)
    @Query("SELECT so FROM ShippingOption so WHERE so.product.store.id = :storeId AND so.isActive = true ORDER BY so.shippingCost ASC")
    List<ShippingOption> findActiveByStore(@Param("storeId") UUID storeId);

    // Options with shippingCost less than or equal to maxCost
    @Query("SELECT so FROM ShippingOption so WHERE so.isActive = true AND so.shippingCost <= :maxCost")
    List<ShippingOption> findByMaxCost(@Param("maxCost") BigDecimal maxCost);

    // Options with estimated max days less than or equal to maxDays
    @Query("SELECT so FROM ShippingOption so WHERE so.isActive = true AND so.estimatedDaysMax <= :maxDays")
    List<ShippingOption> findByMaxDeliveryDays(@Param("maxDays") Integer maxDays);
}
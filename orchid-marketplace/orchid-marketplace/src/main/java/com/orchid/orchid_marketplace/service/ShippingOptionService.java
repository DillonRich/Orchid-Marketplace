package com.orchid.orchid_marketplace.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.orchid.orchid_marketplace.model.ShippingOption;
import com.orchid.orchid_marketplace.repository.ShippingOptionRepository;

@Service
@Profile("!cosmos")
public class ShippingOptionService {
    
    private final ShippingOptionRepository shippingOptionRepository;

    public ShippingOptionService(ShippingOptionRepository shippingOptionRepository) {
        this.shippingOptionRepository = shippingOptionRepository;
    }
    
    // Get all shipping options
    public List<ShippingOption> getAllShippingOptions() {
        return shippingOptionRepository.findAll();
    }
    
    // Get shipping option by ID
    public Optional<ShippingOption> getShippingOptionById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return shippingOptionRepository.findById(id);
    }
    
    // Get active shipping options for a store
    public List<ShippingOption> getShippingOptionsByStoreId(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return shippingOptionRepository.findActiveByStore(storeId);
    }
    
    // Get shipping options by cost range
    public List<ShippingOption> getShippingOptionsByCostRange(BigDecimal maxCost) {
        return shippingOptionRepository.findByMaxCost(maxCost);
    }
    
    // Get shipping options by delivery time
    public List<ShippingOption> getShippingOptionsByDeliveryTime(Integer maxDays) {
        return shippingOptionRepository.findByMaxDeliveryDays(maxDays);
    }
    
    // Get shipping option by name
    public Optional<ShippingOption> getShippingOptionByName(String name) {
        return shippingOptionRepository.findByOptionName(name);
    }
    
    // Create a new shipping option
    public ShippingOption createShippingOption(ShippingOption shippingOption) {
        Objects.requireNonNull(shippingOption, "shippingOption must not be null");
        @SuppressWarnings("null")
        ShippingOption saved = shippingOptionRepository.save(shippingOption);
        return saved;
    }
    
    // Update a shipping option
    public ShippingOption updateShippingOption(UUID id, ShippingOption shippingOptionDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(shippingOptionDetails, "shippingOptionDetails must not be null");
        return shippingOptionRepository.findById(id)
            .map(existingOption -> {
                if (shippingOptionDetails.getOptionName() != null) {
                    existingOption.setOptionName(shippingOptionDetails.getOptionName());
                }
                
                if (shippingOptionDetails.getShippingCost() != null) {
                    existingOption.setShippingCost(shippingOptionDetails.getShippingCost());
                }
                
                if (shippingOptionDetails.getEstimatedDaysMin() != null) {
                    existingOption.setEstimatedDaysMin(shippingOptionDetails.getEstimatedDaysMin());
                }
                
                if (shippingOptionDetails.getEstimatedDaysMax() != null) {
                    existingOption.setEstimatedDaysMax(shippingOptionDetails.getEstimatedDaysMax());
                }
                
                if (shippingOptionDetails.getCarrier() != null) {
                    existingOption.setCarrier(shippingOptionDetails.getCarrier());
                }
                
                if (shippingOptionDetails.getIsFreeShipping() != null) {
                    existingOption.setIsFreeShipping(shippingOptionDetails.getIsFreeShipping());
                }

                @SuppressWarnings("null")
                ShippingOption saved = shippingOptionRepository.save(existingOption);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("Shipping option not found with ID: " + id));
    }
    
    // Soft delete a shipping option
    public void deleteShippingOption(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        shippingOptionRepository.findById(id)
            .ifPresentOrElse(
                option -> {
                    option.softDelete();
                    shippingOptionRepository.save(option);
                },
                () -> { throw new RuntimeException("Shipping option not found with ID: " + id); }
            );
    }
    
    // Get cheapest shipping option for store
    public Optional<ShippingOption> getCheapestShippingOption(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return shippingOptionRepository.findActiveByStore(storeId)
            .stream()
            .min(Comparator.comparing(ShippingOption::getShippingCost, Comparator.nullsLast(BigDecimal::compareTo)));
    }
    
    // Get fastest shipping option for store
    public Optional<ShippingOption> getFastestShippingOption(UUID storeId) {
        Objects.requireNonNull(storeId, "storeId must not be null");
        return shippingOptionRepository.findActiveByStore(storeId)
            .stream()
            .min(Comparator.comparingInt(o -> {
                Integer daysMax = o.getEstimatedDaysMax();
                return daysMax == null ? Integer.MAX_VALUE : daysMax;
            }));
    }
}
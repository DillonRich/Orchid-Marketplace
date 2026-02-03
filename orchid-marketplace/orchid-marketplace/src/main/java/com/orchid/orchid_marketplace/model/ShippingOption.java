package com.orchid.orchid_marketplace.model;

import java.math.BigDecimal;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shipping_options")
public class ShippingOption extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "option_name", nullable = false)
    private String optionName; // "Standard", "Express", "Free Shipping"
    
    @Column(name = "shipping_cost", nullable = false)
    private BigDecimal shippingCost; // CORRECTED: was "shoppingCost"
    
    @Column(name = "estimated_days_min")
    private Integer estimatedDaysMin;
    
    @Column(name = "estimated_days_max")
    private Integer estimatedDaysMax;
    
    @Column(name = "carrier")
    private String carrier; // "USPS", "FedEx", "UPS", "Seller Delivery"
    
    @Column(name = "is_free_shipping")
    private Boolean isFreeShipping = false;
    
    // US-specific fields (expandable later)
    @Column(name = "service_type")
    private String serviceType; // "Ground", "Priority Mail", "Express"
    
    @Column(name = "max_weight_lbs")
    private BigDecimal maxWeightLbs;
    
    @Column(name = "requires_signature")
    private Boolean requiresSignature = false;
    
    // ========== Constructors ==========
    
    public ShippingOption() {}
    
    public ShippingOption(Product product, String optionName, BigDecimal shippingCost) {
        this.product = product;
        this.optionName = optionName;
        this.shippingCost = shippingCost; // CORRECTED here too
    }
    
    // ========== Getters and Setters ==========
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public String getOptionName() { return optionName; }
    public void setOptionName(String optionName) { this.optionName = optionName; }
    
    public BigDecimal getShippingCost() { return shippingCost; } // CORRECTED
    public void setShippingCost(BigDecimal shippingCost) { this.shippingCost = shippingCost; } // CORRECTED
    
    public Integer getEstimatedDaysMin() { return estimatedDaysMin; }
    public void setEstimatedDaysMin(Integer estimatedDaysMin) { this.estimatedDaysMin = estimatedDaysMin; }
    
    public Integer getEstimatedDaysMax() { return estimatedDaysMax; }
    public void setEstimatedDaysMax(Integer estimatedDaysMax) { this.estimatedDaysMax = estimatedDaysMax; }
    
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    
    public Boolean getIsFreeShipping() { return isFreeShipping; }
    public void setIsFreeShipping(Boolean isFreeShipping) { this.isFreeShipping = isFreeShipping; }
    
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    
    public BigDecimal getMaxWeightLbs() { return maxWeightLbs; }
    public void setMaxWeightLbs(BigDecimal maxWeightLbs) { this.maxWeightLbs = maxWeightLbs; }
    
    public Boolean getRequiresSignature() { return requiresSignature; }
    public void setRequiresSignature(Boolean requiresSignature) { this.requiresSignature = requiresSignature; }
    
    // ========== Helper Methods ==========
    
    public String getFormattedDeliveryTime() {
        if (estimatedDaysMin == null || estimatedDaysMax == null) {
            return "Varies";
        }
        if (estimatedDaysMin.equals(estimatedDaysMax)) {
            return estimatedDaysMin + " business day" + (estimatedDaysMin != 1 ? "s" : "");
        }
        return estimatedDaysMin + "-" + estimatedDaysMax + " business days";
    }
    
    public boolean isValidForWeight(BigDecimal productWeight) {
        if (maxWeightLbs == null || productWeight == null) return true;
        return productWeight.compareTo(maxWeightLbs) <= 0;
    }
    
    // Helper method to get formatted shipping cost
    public String getFormattedShippingCost() {
        if (isFreeShipping != null && isFreeShipping) {
            return "FREE";
        }
        if (shippingCost == null) {
            return "Calculated at checkout";
        }
        return "$" + shippingCost.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
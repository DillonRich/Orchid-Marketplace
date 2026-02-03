package com.orchid.orchid_marketplace.model;

import com.orchid.orchid_marketplace.model.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "stripe_payment_method_id")
    private String stripePaymentMethodId;
    
    private String brand;
    private String last4;
    
    @Column(name = "exp_month")
    private Integer expMonth;
    
    @Column(name = "exp_year")
    private Integer expYear;
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
    
    // Azure Key Vault for sensitive data
    @Column(name = "key_vault_secret_id")
    private String keyVaultSecretId;
    
    // ========== Constructors ==========
    
    public PaymentMethod() {}
    
    public PaymentMethod(User user, String stripePaymentMethodId, String brand, String last4, 
                         Integer expMonth, Integer expYear) {
        this.user = user;
        this.stripePaymentMethodId = stripePaymentMethodId;
        this.brand = brand;
        this.last4 = last4;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.isDefault = false;
    }
    
    // ========== Getters and Setters ==========
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getStripePaymentMethodId() {
        return stripePaymentMethodId;
    }
    
    public void setStripePaymentMethodId(String stripePaymentMethodId) {
        this.stripePaymentMethodId = stripePaymentMethodId;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getLast4() {
        return last4;
    }
    
    public void setLast4(String last4) {
        this.last4 = last4;
    }
    
    public Integer getExpMonth() {
        return expMonth;
    }
    
    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }
    
    public Integer getExpYear() {
        return expYear;
    }
    
    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public String getKeyVaultSecretId() {
        return keyVaultSecretId;
    }
    
    public void setKeyVaultSecretId(String keyVaultSecretId) {
        this.keyVaultSecretId = keyVaultSecretId;
    }
    
    // Helper methods
    public String getFormattedCard() {
        if (brand == null || last4 == null) {
            return "Unknown Card";
        }
        return brand + " **** **** **** " + last4;
    }
    
    public String getFormattedExpiry() {
        if (expMonth == null || expYear == null) {
            return "N/A";
        }
        String month = expMonth < 10 ? "0" + expMonth : String.valueOf(expMonth);
        return month + "/" + expYear;
    }
    
    public boolean isExpired() {
        if (expMonth == null || expYear == null) {
            return false;
        }
        
        java.time.YearMonth currentYearMonth = java.time.YearMonth.now();
        java.time.YearMonth expiryYearMonth = java.time.YearMonth.of(expYear, expMonth);
        
        return currentYearMonth.isAfter(expiryYearMonth);
    }
    
    public boolean isValidForPayment() {
        return stripePaymentMethodId != null && !stripePaymentMethodId.trim().isEmpty() && !isExpired();
    }
}
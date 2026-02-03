package com.orchid.orchid_marketplace.model;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "street_address")
    private String streetAddress;
    
    private String city;
    private String state;
    private String country;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    @Column(name = "recipient_name")
    private String recipientName;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;
    
    public enum AddressType {
        SHIPPING, BILLING, BOTH
    }
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
    
    // ========== Constructors ==========
    
    public Address() {}
    
    public Address(User user, String streetAddress, String city, String state, 
                   String country, String postalCode, AddressType addressType) {
        this.user = user;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.addressType = addressType;
    }
    
    // ========== Getters and Setters ==========
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getStreetAddress() {
        return streetAddress;
    }
    
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getRecipientName() {
        return recipientName;
    }
    
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public AddressType getAddressType() {
        return addressType;
    }
    
    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    // Helper methods
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        if (recipientName != null && !recipientName.trim().isEmpty()) {
            sb.append(recipientName).append("\n");
        }
        if (streetAddress != null && !streetAddress.trim().isEmpty()) {
            sb.append(streetAddress).append("\n");
        }
        if (city != null && !city.trim().isEmpty()) {
            sb.append(city);
        }
        if (state != null && !state.trim().isEmpty()) {
            sb.append(", ").append(state);
        }
        if (postalCode != null && !postalCode.trim().isEmpty()) {
            sb.append(" ").append(postalCode);
        }
        if (country != null && !country.trim().isEmpty()) {
            sb.append("\n").append(country);
        }
        return sb.toString();
    }
    
    public boolean isShippingAddress() {
        return addressType == AddressType.SHIPPING || addressType == AddressType.BOTH;
    }
    
    public boolean isBillingAddress() {
        return addressType == AddressType.BILLING || addressType == AddressType.BOTH;
    }
    
    public boolean isValidForShipping() {
        return streetAddress != null && !streetAddress.trim().isEmpty() &&
               city != null && !city.trim().isEmpty() &&
               country != null && !country.trim().isEmpty() &&
               postalCode != null && !postalCode.trim().isEmpty();
    }
}
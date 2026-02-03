package com.orchid.orchid_marketplace.model.cosmos;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.orchid.orchid_marketplace.model.Role;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Container(containerName = "Users", autoCreateContainer = true)
public class CosmosUser {
    
    @Id
    private String id;
    
    @PartitionKey
    private String email;
    
    private String passwordHash;
    private String fullName;
    private String phoneNumber;
    private Role role;
    
    // Azure Entra ID Integration
    private String entraId;
    private String entraObjectId;
    
    // Stripe Integration
    private String stripeConnectAccountId;
    private String stripeCustomerId;
    
    // Verification & Status Flags
    private Boolean isEmailVerified = false;
    private Boolean isPhoneVerified = false;
    private Boolean isActive = true;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    // Embedded Address List (No separate container needed)
    private List<EmbeddedAddress> addresses = new ArrayList<>();
    
    // Cache Control
    private Long cacheVersion = 0L;
    
    // Embedded Store Reference (for sellers)
    private String storeId; // Reference to Store container
    
    // ========== Constructors ==========
    
    public CosmosUser() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public CosmosUser(String email, String fullName, Role role) {
        this();
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.isActive = true;
    }
    
    // ========== Getters and Setters ==========
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public String getEntraId() { return entraId; }
    public void setEntraId(String entraId) { this.entraId = entraId; }
    
    public String getEntraObjectId() { return entraObjectId; }
    public void setEntraObjectId(String entraObjectId) { this.entraObjectId = entraObjectId; }
    
    public String getStripeConnectAccountId() { return stripeConnectAccountId; }
    public void setStripeConnectAccountId(String stripeConnectAccountId) { 
        this.stripeConnectAccountId = stripeConnectAccountId; 
    }
    
    public String getStripeCustomerId() { return stripeCustomerId; }
    public void setStripeCustomerId(String stripeCustomerId) { 
        this.stripeCustomerId = stripeCustomerId; 
    }
    
    public Boolean getIsEmailVerified() { return isEmailVerified; }
    public void setIsEmailVerified(Boolean isEmailVerified) { 
        this.isEmailVerified = isEmailVerified; 
    }
    
    public Boolean getIsPhoneVerified() { return isPhoneVerified; }
    public void setIsPhoneVerified(Boolean isPhoneVerified) { 
        this.isPhoneVerified = isPhoneVerified; 
    }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
    
    public List<EmbeddedAddress> getAddresses() { return addresses; }
    public void setAddresses(List<EmbeddedAddress> addresses) { this.addresses = addresses; }
    
    public Long getCacheVersion() { return cacheVersion; }
    public void setCacheVersion(Long cacheVersion) { this.cacheVersion = cacheVersion; }
    
    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    
    // ========== Helper Methods ==========
    
    public void addAddress(EmbeddedAddress address) {
        this.addresses.add(address);
    }
    
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // ========== Embedded Address Class ==========
    
    public static class EmbeddedAddress {
        private String streetAddress;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private Boolean isDefault;
        private Boolean isShipping;
        private Boolean isBilling;
        
        public EmbeddedAddress() {}
        
        public EmbeddedAddress(String streetAddress, String city, String state, 
                               String postalCode, String country) {
            this.streetAddress = streetAddress;
            this.city = city;
            this.state = state;
            this.postalCode = postalCode;
            this.country = country;
        }
        
        // Getters and Setters
        public String getStreetAddress() { return streetAddress; }
        public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
        
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
        
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        
        public Boolean getIsDefault() { return isDefault; }
        public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
        
        public Boolean getIsShipping() { return isShipping; }
        public void setIsShipping(Boolean isShipping) { this.isShipping = isShipping; }
        
        public Boolean getIsBilling() { return isBilling; }
        public void setIsBilling(Boolean isBilling) { this.isBilling = isBilling; }
    }
}

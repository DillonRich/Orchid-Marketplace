package com.orchid.orchid_marketplace.model;

import java.util.HashSet;
import java.util.Set;

import com.orchid.orchid_marketplace.model.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "password_hash")
    private String passwordHash;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    
    // Azure Entra ID Integration
    @Column(name = "entra_id")
    private String entraId;
    
    @Column(name = "entra_object_id")
    private String entraObjectId;
    
    // Stripe Connect Integration
    @Column(name = "stripe_connect_account_id")
    private String stripeConnectAccountId;
    
    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;
    
    // Verification & Status Flags
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;
    
    @Column(name = "is_phone_verified")
    private Boolean isPhoneVerified = false;
    
    // Relationships (Lazy Loaded)
    @OneToOne(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Store store;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();
    
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();
    
    // Payment Methods - NOW UNCOMMENTED
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PaymentMethod> paymentMethods = new HashSet<>();
    
    // Cache Control for Redis
    @Column(name = "cache_version")
    private Long cacheVersion = 0L;
    
    // ========== Constructors ==========
    
    public User() {}
    
    public User(String email, String fullName, Role role) {
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.setIsActive(true);
    }
    
    // ========== Getters and Setters ==========
    
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
    public void setStripeConnectAccountId(String stripeConnectAccountId) { this.stripeConnectAccountId = stripeConnectAccountId; }
    
    public String getStripeCustomerId() { return stripeCustomerId; }
    public void setStripeCustomerId(String stripeCustomerId) { this.stripeCustomerId = stripeCustomerId; }
    
    public Boolean getIsEmailVerified() { return isEmailVerified; }
    public void setIsEmailVerified(Boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }
    
    public Boolean getIsPhoneVerified() { return isPhoneVerified; }
    public void setIsPhoneVerified(Boolean isPhoneVerified) { this.isPhoneVerified = isPhoneVerified; }
    
    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
    
    public Set<Address> getAddresses() { return addresses; }
    public void setAddresses(Set<Address> addresses) { this.addresses = addresses; }
    
    public Set<Order> getOrders() { return orders; }
    public void setOrders(Set<Order> orders) { this.orders = orders; }
    
    // Payment Methods getters/setters - NOW UNCOMMENTED
    public Set<PaymentMethod> getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) { this.paymentMethods = paymentMethods; }
    
    public Long getCacheVersion() { return cacheVersion; }
    public void setCacheVersion(Long cacheVersion) { this.cacheVersion = cacheVersion; }
    
    // ========== Helper Methods ==========
    
    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }
    
    public void addOrder(Order order) {
        orders.add(order);
        order.setBuyer(this);
    }
    
    // NOW UNCOMMENTED
    public void addPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.add(paymentMethod);
        paymentMethod.setUser(this);
    }
    
    // Business logic methods
    public boolean isSeller() {
        return role == Role.SELLER;
    }
    
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
    
    public boolean isCustomer() {
        return role == Role.CUSTOMER;
    }
    
    public boolean hasStore() {
        return store != null;
    }
    
    public boolean hasPaymentMethods() {
        return paymentMethods != null && !paymentMethods.isEmpty();
    }
}